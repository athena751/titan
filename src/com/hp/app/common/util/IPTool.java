package com.hp.app.common.util;

import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class IPTool {
	// 一个IP，是一个３２位无符号的二进制数。故用long的低32表示无符号32位二进制数。
	public static long getIP(InetAddress ip) {
		byte[] b = ip.getAddress();
		long l = b[0] << 24L & 0xff000000L | b[1] << 16L & 0xff0000L
				| b[2] << 8L & 0xff00L | b[3] << 0L & 0xffL;
		return l;
	}

	// 由低32位二进制数构成InetAddress对象
	public static InetAddress toIP(long ip) throws UnknownHostException {
		byte[] b = new byte[4];
		int i = (int) ip;// 低３２位
		b[0] = (byte) ((i >> 24) & 0x000000ff);
		b[1] = (byte) ((i >> 16) & 0x000000ff);
		b[2] = (byte) ((i >> 8) & 0x000000ff);
		b[3] = (byte) ((i >> 0) & 0x000000ff);
		return InetAddress.getByAddress(b);
	}

	public static long ipToLong(String ip) {
		String[] parts = ip.split("[.]");
		if (parts.length != 4) {
			return -1;
		}
		long rtn = 0;
		try {
			for (String part : parts) {
				rtn <<= 8;// rtn *= 256;
				rtn += Integer.parseInt(part.trim());
			}
			return rtn;
		} catch (Exception ex) {
			ex.printStackTrace();
			return -1;
		}
	}

	public static List<String> getIpList(String startIp, String endIp)
			throws UnknownHostException {
		// long Starttime = System.currentTimeMillis();
		List<String> iplist = new ArrayList<String>();
		long ip1 = IPTool.getIP(InetAddress.getByName(startIp));
		long ip2 = IPTool.getIP(InetAddress.getByName(endIp));
		for (long ip = ip1; ip <= ip2; ip++) {
			iplist.add(IPTool.toIP(ip).getHostAddress());
		}
		return iplist;
	}

	public static void main(String[] args) throws UnknownHostException {
		long Starttime = System.currentTimeMillis();
		long ip1 = getIP(InetAddress.getByName("192.168.0.233"));
		long ip2 = getIP(InetAddress.getByName("192.170.2.233"));
		System.out.println("192.168.0.233到192.168.1.12之间所有的IP是：");
		for (long ip = ip1; ip <= ip2; ip++) {
			System.out.println(toIP(ip).getHostAddress());
		}
		System.out.println(ip2 - ip1 + 1);
		// long ip1 = ipToLong("192.168.0.233");
		// long ip2 = getIP(InetAddress.getByName("192.168.0.233"));
		System.out.println(System.currentTimeMillis() - Starttime);
	}
}
