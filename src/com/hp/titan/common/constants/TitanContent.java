package com.hp.titan.common.constants;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.hp.app.common.util.DateUtils;

public class TitanContent {
	
	/** System role */
    public static String ROLE_ADMIN="ADMIN";
	public static String ROLE_MANAGER="MANAGER";
	public static String ROLE_DEVELOPER="DEVELOPER";
	public static String ROLE_QA="QA";
	public static String ROLE_JENKINS="JENKINS";
	
	/** User status */
	public static String USER_STATUS_APPLYING = "0";
	public static String USER_STATUS_NORMAL = "1";
	

	/** Language */
	public static String LOCALE_LANGUAGE = "locale_language";
	public static String LOCALE_CH = "zh_CN";
	public static String LOCALE_EN = "en_US";
	public static String LOCALE_MAP = "locale_map";
	public static Map<String, String> map_ch = new HashMap<String, String>();
	public static Map<String, String> map_en = new HashMap<String, String>();
	
	/** Date format */
	public static DateFormat dateFormartLong = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	public static DateFormat dateFormartShort = new SimpleDateFormat("yyyy-MM-dd");
	
	/** Testcase type */
	public static String TESTCASE_MANUAL = "manual";
	public static String TESTCASE_AUTO = "auto";
	
	/** Testplan type */
	public static String TESTPLAN_MANUAL = "Manual";
	public static String TESTPLAN_AUTO = "Automated";
	
	/** Testjob type */
	public static String TESTJOB_MANUAL = "Manual";
	public static String TESTJOB_AUTO = "Automated";
	
	/** Testcase parameter type */
	public static String TESTCASE_PARAM_INPUT = "INPUT";
	public static String TESTCASE_PARAM_PASS = "PASS";
	public static String TESTCASE_PARAM_FAIL = "FAIL";

	/** Testcase input parameter value type */
	public static String INPUT_PARAM_HOSTNAME = "$hostname";
	public static String INPUT_PARAM_IP = "$ip";
	public static String INPUT_PARAM_ILONAME = "$iloname";
	public static String INPUT_PARAM_ILOIP = "$iloip";
	
	/** Testjob status */
	public static String TEST_JOB_STATUS_RUNNING = "RUNNING";
	public static String TEST_JOB_STATUS_SUCCESS = "PASS";
	public static String TEST_JOB_STATUS_FAIL = "FAIL";
	public static String TEST_JOB_STATUS_ACTIVE = "ACTIVE";
	public static String TEST_JOB_STATUS_PENDING = "PENDING";
	public static String TEST_JOB_STATUS_ABORT = "ABORT";
	
	/** Runjob status */
	public static String RUN_JOB_STATUS_SUCCESS = "PASS";
	public static String RUN_JOB_STATUS_FAIL = "FAIL";
	public static String RUN_JOB_STATUS_SKIP = "SKIPPED";
	public static String RUN_JOB_STATUS_ACTIVE = "ACTIVE";
	public static String RUN_JOB_STATUS_ABORT = "ABORT";
	
	/** RunCase status */
	public static String RUN_CASE_STATUS_NOTRUN = "NOTRUN";
	public static String RUN_CASE_STATUS_SKIP = "SKIPPED";
	public static String RUN_CASE_STATUS_SUCCESS = "PASS";
	public static String RUN_CASE_STATUS_FAIL = "FAIL";
	public static String RUN_CASE_STATUS_RUNNING = "RUNNING";
	
	/** RunTestjob is done */
	public static Integer RUN_TEST_JOB_STATUS_SUCCESS = 1;
	public static Integer RUN_TEST_JOB_STATUS_FAIL = 0;
	
	/** Parameter data type */
	public static String PARAMETER_TYPE_HOST = "host";
	public static String PARAMETER_TYPE_IP  = "ip";
	public static String PARAMETER_TYPE_USERNAME = "username";
	public static String PARAMETER_TYPE_PWD  = "password";
	public static String PARAMETER_TYPE_OTHER  = "other";
	public static String PARAMETER_TYPE_NONE  = "none";
	
	/** Case defect type */
	public static String CASE_DEFECT_TYPE_RALLY = "rally";
	public static String CASE_DEFECT_TYPE_QUIX = "quix";
	
	/** Case defect para */
	public static String CASE_DEFECT_PARA_RALLY_URL = "https://rally1.rallydev.com";
	public static String CASE_DEFECT_PARA_RALLY_USERNAME = "xu.yang@hp.com";
	public static String CASE_DEFECT_PARA_RALLY_PWD = "stainless637082!";
	public static String CASE_DEFECT_PARA_RALLY_PROXY = "http://web-proxy.cce.hp.com:8080/";
//	public static String CASE_DEFECT_PARA_RALLY_PROXY = null;
	public static String CASE_DEFECT_PARA_RALLY_VERSION = "v2.0";
	
	/** Card status */
	public static String CARD_STATUS_FREE = "FREE";
	public static String CARD_STATUS_RESERVED = "RESERVED";
	public static String CARD_STATUS_TAKEOVER = "TAKEOVER";
	public static String CARD_STATUS_KEY = "KEYSERVER";
	
	/** Server status */
	public static String SERVER_STATUS_FREE = "FREE";
	public static String SERVER_STATUS_RESERVED = "RESERVED";
	public static String SERVER_STATUS_TAKEOVER = "TAKEOVER";
	public static String SERVER_STATUS_KEY = "KEYSERVER";
	
	/** Server reservation */
	public static String SERVER_ACTION_RESERVE= "reserved";
	public static String SERVER_ACTION_RELEASE = "released";
	public static String SERVER_ACTION_TAKEOVER = "taken over";
	public static String SERVER_ACTION_RETURN = "returned";
	
	/** Card reservation */
	public static String CARD_ACTION_RESERVE= "reserved";
	public static String CARD_ACTION_RELEASE = "released";
	public static String CARD_ACTION_TAKEOVER = "taken over";
	public static String CARD_ACTION_RETURN = "returned";
	
	/** titan url */
	public static String TITAN_URL = "http://titan.chn.hp.com/titan/indexmain.do";
	
	/**rally url */
	public static String RALLY_URL = "https://rally1.rallydev.com/slm/#/detail/defect/";
	
	/** Weekly report status */
	public static String WEEKLY_REPORT_DRAFT= "draft";
	public static String WEEKLY_REPORT_SENT = "sent";
	
	/** Weekly report task status */
	public static String WEEKLY_REPORT_TASK_RALLY= "rally";
	public static String WEEKLY_REPORT_TASK_QUIX= "quix";
	public static String WEEKLY_REPORT_TASK_CUSTOM = "custom";
	
	/**devices type */
	public static Integer META_DATA_TYPE_STORAGE=3;
	public static Integer META_DATA_TYPE_CARD=2;
	public static Integer META_DATA_TYPE_SERVER=1;
	
	/** x86 server*/
	public static String SERVER_TYPE_X86="x86";
	
	/** server hastab*/
	public static String SERVER_HAVE_TAB="1";
	public static String SERVER_NOT_HAVE_TAB="0";
	
	/** reservation type*/
	public static String RESERVATION_TYPE_SERVER="1";
	public static String RESERVATION_TYPE_CARD="2";
	
	/** reser_hist type*/
	public static String RESER_HIST_SERVER="1";
	public static String RESER_HIST_CARD="2";
	
	/** test case report*/
	public static String CASE_REPORT_TOALL="total";
	public static String CASE_REPORT_PASS="Pass";
	public static String CASE_REPORT_FAIL="Fail";

	/** consume status*/
	public static String CONSUME_SUBMITTED="submitted";
	public static String CONSUME_INPROCESS="inprocess";
	public static String CONSUME_FINISHED="finished";
	
	/** inbox*/
	public static Integer INBOX_READED_YES=0;
	public static Integer INBOX_READED_NO=1;
	
	/** inbox message*/
	public static Integer INBOX_MSG_TREASURE=0;
	/**
	 * 精确到毫秒的时间+6位随机数 生成订单号
	 * @return
	 */
	public static String makeOrderCode(){
		String dateTime = DateUtils.dateNumSz(new Date());
		Double dRandom = new Double(10000*Math.random());
		Integer iRandom = new Integer(dRandom.intValue());
		String sRandom =iRandom.toString().toLowerCase();
	 	 //把得到的数增加为固定长度,为4位   
        while(sRandom.length()<4){   
        	sRandom="0"+sRandom;   
        } 
	 	String orderCode=dateTime+sRandom;
		return orderCode;
	}
	
	/**
	 * 生成3位随机数
	 * @return
	 */
	public static String makeRandomCount(){
		Double dRandom = new Double(1000*Math.random());
		Integer iRandom = new Integer(dRandom.intValue());
		String sRandom =iRandom.toString().toLowerCase();
		while(sRandom.length()<3){   
			sRandom="0"+sRandom;   
		} 
		return sRandom;
	}
}
