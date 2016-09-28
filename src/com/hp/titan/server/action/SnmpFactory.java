package com.hp.titan.server.action;


import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import snmp.SNMPObject;
import snmp.SNMPSequence;
import snmp.SNMPVarBindList;
import snmp.SNMPv1CommunicationInterface;

/**
 * SNMP Management Class
 */
public class SnmpFactory {

	private static final Log log = LogFactory.getLog(SnmpFactory.class);
	
	private static int version = 0; // SNMP Version, 0 represant 1
	
	private static String protocol = "udp"; // protocol if it's monitoring
	
	private static String port = "161"; // port if it's monitoring
		
	/**
	 * SnmpWalk 
	 * 
	 * @param ipAddress Target IP
	 * @param community community
	 * @param oid target of node start object
	 * @return String[] walk result
	 * @throws AppException
	 */
	public String[] snmpWalk(String ipAddress, String community, String oid) throws AppException {
		String[] returnValueString = null; 
		
		SNMPv1CommunicationInterface comInterface = null;
		try {
			InetAddress hostAddress = InetAddress.getByName(ipAddress);
			comInterface = new SNMPv1CommunicationInterface(
					version, hostAddress, community);
			comInterface.setSocketTimeout(2000);
			
			//	return paramenter value of oid management
			SNMPVarBindList tableVars = comInterface.retrieveMIBTable(oid);
			returnValueString = new String[tableVars.size()];
			
			//  settle value of oid return
			for (int i = 0; i < tableVars.size(); i++) {
				SNMPSequence pair = (SNMPSequence) tableVars.getSNMPObjectAt(i); 
				SNMPObject snmpValue = pair.getSNMPObjectAt(1); 
				String typeString = snmpValue.getClass().getName(); 
				if (typeString.equals("snmp.SNMPOctetString")) {
					String snmpString = snmpValue.toString();
					int nullLocation = snmpString.indexOf('\0');
					if (nullLocation >= 0)
						snmpString = snmpString.substring(0, nullLocation);
					returnValueString[i] = snmpString;
				} else {
					returnValueString[i] = snmpValue.toString();
				}
			}
		} catch (SocketTimeoutException ste) {
			if (log.isErrorEnabled()) {
				log.error("Walk IP: " + ipAddress + ", OID: " + oid + " Overtime!");
			}
			returnValueString = null;
		} catch (Exception e) {
			throw new AppException("Snmp Exception When Step!", e);
		} finally {
			if (comInterface != null) {
				try {
					comInterface.closeConnection();
				} catch (SocketException e) {
					comInterface = null;
				}
			}
		}
		
		return returnValueString;
	}
} 

