package com.hp.titan.server.dao;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.Query;

import snmp.SNMPOctetString;
import com.hp.app.dao.DefaultBaseDao;
import com.hp.app.exception.BaseDaoException;
import com.hp.titan.common.constants.TitanContent;
import com.hp.titan.server.action.AppException;
import com.hp.titan.server.action.SnmpFactory;
import com.hp.titan.server.model.Reservation;
import com.hp.titan.server.model.Server;
import com.hp.titan.server.vo.ServerVo;

public class ServerDao extends DefaultBaseDao<Server, String> {

	private static final String SERVER_SYSTEM_DESC = "1.3.6.1.2.1.1.1";
    private static final String SERVER_MEMORY = "1.3.6.1.4.1.232.11.2.13.1";
    private static final String SERVER_SERVER_NAME = "1.3.6.1.4.1.232.11.2.2.12";
    private static final String SERVER_PROCESSORS = "1.3.6.1.4.1.232.1.2.2.4";
    private static final String SERVER_STORAGE = "1.3.6.1.4.1.232.3.2.5.1.1.45";
    private static final String SERVER_HOST_OS = "1.3.6.1.4.1.232.11.2.2.3";
    private static final String SERVER_OS_NAME = "1.3.6.1.4.1.232.11.2.2.1";
    private static final String SERVER_SERIAL_NUMBER = "1.3.6.1.4.1.232.2.2.2.1";
    private static final String SERVER_CUP_NAME = "1.3.6.1.4.1.232.1.2.2.1.1.3";
    private static final String SERVER_CUP_SPEED = "1.3.6.1.4.1.232.1.2.2.1.1.4";
    private static final String SERVER_CUP_CORE = "1.3.6.1.4.1.232.1.2.2.1.1.15";
    private static final String SERVER_CUP_THREAD = "1.3.6.1.4.1.232.1.2.2.1.1.25";
    private static final String SERVER_MAC_ADDRESS = "1.3.6.1.4.1.232.18.2.3.1.1.4";
    private static final String COMMUNITY = "public";
    private SnmpFactory mgr ;
	public Server findById(String serverId) throws BaseDaoException{
		return (Server) getHibernateTemplate().get(Server.class, serverId);
	}

	public Server fillStringArray(String IP){
		Map<String,String> map = new HashMap<String,String>();
		map.put(SERVER_SYSTEM_DESC,"System desc");
		map.put(SERVER_MEMORY, "Memory");
		map.put(SERVER_SERVER_NAME,"Host name");
		map.put(SERVER_PROCESSORS,"Processors");
		map.put(SERVER_STORAGE,"Storage");
		map.put(SERVER_HOST_OS,"Host OS");
		map.put(SERVER_OS_NAME,"OS Name");
		map.put(SERVER_SERIAL_NUMBER,"Serial number");
		map.put(SERVER_CUP_NAME, "CPU name");
		map.put(SERVER_CUP_SPEED, "CPU speed");
		map.put(SERVER_CUP_CORE, "CPU core");
		map.put(SERVER_CUP_THREAD, "CPU thread");
		map.put(SERVER_MAC_ADDRESS, "Mac address");
		
		Map<String, String> cpuMap = new HashMap<String, String>();
		
		Iterator<String> it = map.keySet().iterator();
		InetAddress inetHost = null;
		String[] sysDescs=null;
		Server server = new Server();
		server.setConsoleIp(IP);
		while (it.hasNext()) {
			String oid = it.next();
			try {
				mgr = new SnmpFactory();
				sysDescs = mgr.snmpWalk(IP, COMMUNITY, oid);
				if (sysDescs == null || sysDescs.length == 0) {
					continue;
				}else{
					if(oid.equals(SERVER_SERVER_NAME)){
						server.setServerName(sysDescs[0]);
						if(!sysDescs[0].endsWith(".test")){
							inetHost = InetAddress.getByName(sysDescs[0].concat(".test"));
						}else{
							inetHost = InetAddress.getByName(sysDescs[0]);
						}
						server.setServerIp(inetHost.getHostAddress());
					}
					else if(oid.equals(SERVER_SYSTEM_DESC)){
						server.setDescription(sysDescs[0]);
					}else if(oid.equals(SERVER_MEMORY)){
						server.setMemory(sysDescs[0]);
					}else if(oid.equals(SERVER_HOST_OS)){
						server.setDistro(sysDescs[0]);
					}
					else if(oid.equals(SERVER_SERIAL_NUMBER)){
						server.setSn(sysDescs[0]);
					}
					else if(oid.equals(SERVER_MAC_ADDRESS)){
						SNMPOctetString snmpo = new SNMPOctetString(sysDescs[0]);
						String macAddress = snmpo.toHexString();
						if(macAddress.endsWith(",")){
							macAddress = macAddress.substring(0,macAddress.length()-1);
						}
						server.setMacAddr(macAddress);
					}
					else if(oid.equals(SERVER_CUP_NAME)){
						cpuMap.put("name", sysDescs[0]);
					}
					else if(oid.equals(SERVER_CUP_SPEED)){
						cpuMap.put("speed", sysDescs[0]);
					}
					else if(oid.equals(SERVER_CUP_CORE)){
						cpuMap.put("core", sysDescs[0]);
					}
					else if(oid.equals(SERVER_CUP_THREAD)){
						cpuMap.put("thread", sysDescs[0]);
					}
				}
			} catch (AppException e) {
				e.printStackTrace();
			}
			catch (UnknownHostException e) {
				IP ="Unknown";
				e.printStackTrace();
			}
		}
		if(cpuMap.size() != 0){
			server.setCpu(cpuMap.get("name") + "@" +cpuMap.get("speed") + "GHz");	
		}
		return server;
	}
	public List<Server> findAllServer() {
		String hql = "from com.hp.titan.server.model.Server as server";
		return this.getHibernateTemplate().find(hql);
	}
	
	public List<Server> getServerByOwner(Integer userId)throws BaseDaoException {
		String hql = "select s from Server s where s.ownerId = ? and s.isValid=0";
		return this.getHibernateTemplate().find(hql,userId);
	}
	public List<Server> getServerByType(String serverType)throws BaseDaoException {
		String hql = "select s from Server s where s.serverType = ? and s.isValid=0";
		return this.getHibernateTemplate().find(hql,serverType);
	}
	public List<ServerVo> findAllServer(ServerVo serverVo, String type)throws BaseDaoException {
		List<ServerVo> rtn = new ArrayList<ServerVo>();
		String sql = "select s.SERVER_ID, s.SERVER_NAME, s.SERVER_IP, s.DISTRO, s.SERVER_STATE, s.OWNER_ID, s.PROJECT_ID, s.DESCRIPTION, "
			    + "u.USER_CODE, p.PROJECT_ID, p.PROJECT_NAME, "
			    + "s.LOCATION, s.SN, s.MAC_ADDR, s.SERVER_TYPE, s.MEMORY, s.CPU ,s.CONSOLE_IP ,s.UPDATE_DATE,s.CREATE_DATE,s.PURCHASE_DATE,s.HASTAB "
			    + "from server s, titan_user u, PROJECT p "
			    + "where s.OWNER_ID=u.USER_ID and s.PROJECT_ID=p.PROJECT_ID and s.IS_VALID=0";
		
		if(type.equals("list")) sql +=" and s.KEY_SERVER=0";
		if (serverVo != null) {
			
			if (serverVo.getServerName() != null && !serverVo.getServerName().equals("")){
				sql += " and s.Server_NAME like '%" + serverVo.getServerName() + "%'";
			}
			if (serverVo.getServerIp() != null && !serverVo.getServerIp().equals("")){
				sql += " and s.Server_IP like '%" + serverVo.getServerIp() + "%'";
			}
			if (serverVo.getDistro() != null && !serverVo.getDistro().equals("")){
				sql += " and s.DISTRO like '%" + serverVo.getDistro() + "%'";
			}
			if (serverVo.getServerStatus() != null && !serverVo.getServerStatus().equals("")){
				sql += " and s.SERVER_STATE like '%" + serverVo.getServerStatus() + "%'";
			}
			if (serverVo.getOwnerId() != null && !serverVo.getOwnerId().equals("")){
				sql += " and s.OWNER_ID='" + serverVo.getOwnerId() + "'";
			}
			if (serverVo.getProjectId() != null && !serverVo.getProjectId().equals("")){
				sql += " and s.PROJECT_ID='" + serverVo.getProjectId() + "'";
			}
			if (serverVo.getGroupId()!= null && !serverVo.getGroupId().equals("")){
				sql += " and s.GROUP_ID='" + serverVo.getGroupId() + "'";
			}
			if (serverVo.getLocation()!= null && !serverVo.getLocation().equals("")){
				sql += " and s.LOCATION like '%" + serverVo.getLocation() + "%'";
			}
			if (serverVo.getSn()!= null && !serverVo.getSn().equals("")){
				sql += " and s.SN like '%" + serverVo.getSn() + "%'";
			}
			if (serverVo.getMacAddr()!= null && !serverVo.getMacAddr().equals("")){
				sql += " and s.MAC_ADDR like '%" + serverVo.getMacAddr() + "%'";
			}
			if (serverVo.getServerType()!= null && !serverVo.getServerType().equals("")){
				sql += " and s.SERVER_TYPE like '%" + serverVo.getServerType() + "%'";
			}
			if (serverVo.getMemory()!= null && !serverVo.getMemory().equals("")){
				sql += " and s.MEMORY like '%" + serverVo.getMemory() + "%'";
			}
			if (serverVo.getCpu()!= null && !serverVo.getCpu().equals("")){
				sql += " and s.CPU like '%" + serverVo.getCpu() + "%'";
			}
			if (serverVo.getConsoleIp()!= null && !serverVo.getConsoleIp().equals("")){
				sql += " and s.CONSOLE_IP like '%" + serverVo.getConsoleIp() + "%'";
			}
			if (serverVo.getStrLastUpdateDate()!= null && !serverVo.getStrLastUpdateDate().equals("")){
				sql += " and s.UPDATE_DATE like '%" + serverVo.getStrLastUpdateDate() + "%'";
			}
			if (serverVo.getStrCreateDate()!= null && !serverVo.getStrCreateDate().equals("")){
				sql += " and s.CREATE_DATE like '%" + serverVo.getStrCreateDate() + "%'";
			}
			if (serverVo.getPurchaseDate()!= null && !serverVo.getPurchaseDate().equals("")){
				sql += " and s.PURCHASE_DATE like '%" + serverVo.getPurchaseDate() + "%'";
			}
			if (serverVo.getHastab()!= null && !serverVo.getHastab().equals("")){
				sql += " and s.HASTAB like '%" + serverVo.getHastab() + "%'";
			}
		}
		
		sql +=" order by s.UPDATE_DATE desc";
		
		Query query = this.getSession().createSQLQuery(sql.replace(",)", ")"));		
		List<Object[]> objects = query.list();
		Server server = new Server();
		for (Object[] object : objects){
			ServerVo vo = new ServerVo();
//			vo.setServerId(object[0].toString());
//			vo.setServerName(object[1].toString());
			if (object[0] !=null && !"".equals(object[0].toString())){
				vo.setServerId(object[0].toString());
			}
			if (object[1] !=null && !"".equals(object[1].toString())){
				vo.setServerName(object[1].toString());
			}
			if (object[2] !=null && !"".equals(object[2].toString())){
				vo.setServerIp(object[2].toString());
			}
			if (object[3] !=null && !"".equals(object[3].toString())){
				vo.setDistro((object[3].toString()));
			}
			if (object[4] !=null && !"".equals(object[4].toString())){
				vo.setServerStatus(object[4].toString());
			}			
			if (object[7] != null && !"".equals(object[7].toString())){
			    vo.setRemark(object[7].toString());
			}	
			if (object[8] !=null && !"".equals(object[8].toString())){
				vo.setOwner(object[8].toString());		
				}
			if (object[10] != null && !"".equals(object[10].toString())){
				vo.setProject(object[10].toString());
			}
			if (object[11] != null && !"".equals(object[11].toString())){
				vo.setLocation(object[11].toString());
			}
			if (object[12] != null && !"".equals(object[12].toString())){
				vo.setSn(object[12].toString());
			}
			if (object[13] != null && !"".equals(object[13].toString())){
				vo.setMacAddr(object[13].toString());
			}
			if (object[14] != null && !"".equals(object[14].toString())){
				vo.setServerType(object[14].toString());
			}
			if (object[15] != null && !"".equals(object[15].toString())){
				vo.setMemory(object[15].toString());
			}
			if (object[16] != null && !"".equals(object[16].toString())){
				vo.setCpu(object[16].toString());
			}
			if (object[17] != null && !"".equals(object[17].toString()) && !"Unknown".equalsIgnoreCase(object[17].toString())){
				vo.setConsoleIp(object[17].toString());
			}
			if (object[18] != null && !"".equals(object[18].toString())) {
				vo.setStrLastUpdateDate(object[18].toString());
			}
			if (object[19] != null && !"".equals(object[19].toString())) {
				vo.setStrCreateDate(object[19].toString());
			}
			if (object[20] != null && !"".equals(object[20].toString())) {
				String dateLong = object[20].toString();
				String dateShort = dateLong.substring(0, dateLong.length()-10);
				vo.setPurchaseDate(dateShort);
			}
			if (object[21] != null && !"".equals(object[21].toString())) {
				String bool = object[21].toString().equals(TitanContent.SERVER_NOT_HAVE_TAB)?"not have":"have";
				vo.setHastab(bool);
			}
			if (object[4].toString().equals(TitanContent.SERVER_STATUS_RESERVED)){
				
				String hql = "from com.hp.titan.server.model.Reservation as re where re.serverId = ? and re.reservationType='1' and re.isValid=0";
				List<Reservation> reservationList = this.getHibernateTemplate().find(hql,object[0].toString().toUpperCase());	
				if(reservationList.size()!=0) vo.setResNote(reservationList.get(0).getRemark());
				}			
			
			rtn.add(vo);
		}
		return rtn;
	}
	
	public ServerVo setServerVoIfChange(){
		ServerVo vo= new ServerVo();
		return vo;
	}

	public void saveServer(Server server)throws BaseDaoException  {
		this.getHibernateTemplate().saveOrUpdate(server);
	}

	public void saveOrUpdate(Server server)throws BaseDaoException {
		try {
			this.getHibernateTemplate().saveOrUpdate(server);
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	public Server getServerByServerName(String serverName) {
		String hql = "select s from Server s where s.serverName = ? and s.isValid=0";
		List<Server> server = this.getHibernateTemplate().find(hql,serverName.toUpperCase());
		return server.size()>0?server.get(0):null;
	}
	
	public boolean isIpv4(String ipAddress) {
        String ip = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
        Pattern pattern = Pattern.compile(ip);
        Matcher matcher = pattern.matcher(ipAddress);
        return matcher.matches();
    }
	
	public void setMgr(SnmpFactory mgr) {
		this.mgr = mgr;
	}

	public SnmpFactory getMgr() {
		return mgr;
	}
}
