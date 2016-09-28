package com.hp.titan.common.util;

public class UnitCodeUtil {

	/**
	 * Unit Code = prefix string + 4 digit
	 * @param prefix
	 * @param maxCode
	 * @return String
	 */
	public static String generateUnitCode(String prefix, String maxCode){
		
		if(prefix == null) prefix = "";
		if(maxCode == null) maxCode = prefix + "0000";
		
		prefix = maxCode.substring(0, maxCode.length()-4);
		
		int codeNum = Integer.parseInt(maxCode.substring(maxCode.length()-4)) + 1;
		
		String newCode = null;
		if(codeNum<10000) newCode = prefix + String.format("%04d", codeNum);
		
		return newCode;
	}
}
