package com.hp.app.common.util;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class DateUtils {

	private static Log log = LogFactory.getLog(DateUtils.class);
	private static String defaultDatePattern = "yyyy-MM-dd";
	private static String defaultDTPattern = "yyyy-MM-dd HH:mm:ss";
	private static DateFormat pageCanlenderFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
	private static String numDTPattern = "yyMMddHHmmss";
	
	private static String timePattern = "HH:mm";

	public static String getDatePattern() {
		return defaultDatePattern;
	}

	public static final String getDate(Date aDate) {
		SimpleDateFormat df = null;
		String returnValue = "";

		if (aDate != null) {
			df = new SimpleDateFormat(getDatePattern());
			returnValue = df.format(aDate);
		}

		return returnValue;
	}

	public static final Date convertStringToDate(String aMask, String strDate)
			throws ParseException {
		SimpleDateFormat df = null;
		Date date = null;
		df = new SimpleDateFormat(aMask);
		try {
			date = df.parse(strDate);
		} catch (ParseException pe) {
			throw new ParseException(pe.getMessage(), pe.getErrorOffset());
		}

		return date;
	}

	public static String getTimeNow(Date theTime) {
		return getDateTime(timePattern, theTime);
	}
	
	public static String getTimeMD(Date theTime) {
		return getDateTime(numDTPattern, theTime).substring(2, 6);
	}

	public static Calendar getToday() throws ParseException {
		Date today = new Date();
		SimpleDateFormat df = new SimpleDateFormat(getDatePattern());

		String todayAsString = df.format(today);
		Calendar cal = new GregorianCalendar();
		cal.setTime(convertStringToDate(todayAsString));

		return cal;
	}

	public static final String getDateTime(String aMask, Date aDate) {
		SimpleDateFormat df = null;
		String returnValue = "";

		if (aDate == null) {
			log.error("aDate is null!");
		} else {
			df = new SimpleDateFormat(aMask);
			returnValue = df.format(aDate);
		}

		return returnValue;
	}

	public static final String convertDateToString(Date aDate) {
		return getDateTime(getDatePattern(), aDate);
	}
	
	public static final String date2Sz(Date aDate) {
		return getDateTime(defaultDTPattern, aDate);
	}

	public static final String dateNumSz(Date aDate) {
		return getDateTime(numDTPattern, aDate);
	}
	
	public static final String convertDateToString(String aMask, Date aDate) {
		return getDateTime(aMask, aDate);
	}
	
	public static Date convertSz2Date(String szDate)
		throws ParseException{
		Date aDate = null;
		try {
			aDate = convertStringToDate(defaultDTPattern, szDate);
		} catch (ParseException pe) {
			log.error("Could not convert '" + szDate
					+ "' to a date, throwing exception");
		}

		return aDate;
	}

	public static Date convertStringToDate(String strDate)
			throws ParseException {
		Date aDate = null;
		try {
			aDate = convertStringToDate(getDatePattern(), strDate);
		} catch (ParseException pe) {
			aDate = getDateFormPageCanlender(strDate);
		}

		return aDate;
	}

	public static final Date getDateFromString(String aMask, String strDate) {
		SimpleDateFormat df = null;
		Date date = null;
		df = new SimpleDateFormat(aMask);
		try {
			if ((strDate != null) && (strDate.length() > 1)) {
				date = df.parse(strDate);
			}
		} catch (ParseException pe) {
			pe.printStackTrace();
		}

		return date;
	}

	public static final Date getDateFromString(String strDate) {
		return getDateFromString("yyyy-MM-dd", strDate);
	}

	public static final String getStrYearMonth() {
		Date date = new Date();
		String strDate = getDateTime("yyyyMM", date);
		return strDate;
	}

	public static Date getDiffDay(Date theDate, int diff) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(theDate);
		calendar.add(6, diff);
		return calendar.getTime();
	}

	public static String convertMinToDay(long min) {
		BigDecimal bdMin = new BigDecimal(min);
		BigDecimal bdHour = bdMin.divide(new BigDecimal(60), 2, 4);
		BigDecimal ltHour = bdHour.setScale(0, 1);
		BigDecimal bdDay = ltHour.divide(new BigDecimal(24), 2, 4);
		BigDecimal ltDay = bdDay.setScale(0, 1);

		String resutl = ltDay
				+ "days"
				+ bdDay.subtract(ltDay).multiply(new BigDecimal(24)).setScale(
						0, 4)
				+ "hours"
				+ bdHour.subtract(ltHour).multiply(new BigDecimal(60))
						.setScale(0, 4) + "minutes";
		return resutl;
	}

	public static String getCurrentYearString() {
		Calendar c = Calendar.getInstance();
		String currentYear = String.valueOf(c.get(1));
		return currentYear;
	}

	public static String getCurrentMonthString() {
		Calendar c = Calendar.getInstance();
		String currentMonth = String.valueOf(c.get(2) + 1);
		if (currentMonth.length() != 2)
			currentMonth = "0" + currentMonth;
		return currentMonth;
	}

	public static String getCurrentDayString() {
		Calendar c = Calendar.getInstance();
		String day = String.valueOf(c.get(5));
		if (day.length() != 2)
			day = "0" + day;
		return day;
	}
	
	public static Date getDateFormPageCanlender(String date) throws ParseException{
		try {
			return pageCanlenderFormat.parse(date.trim());
		} catch (ParseException e) {
			try {
				return new SimpleDateFormat("yyyy/MM/dd").parse(date.trim());
			} catch (ParseException e1) {
				return  new SimpleDateFormat("yyyy/MM/dd hh:mm").parse(date.trim());
			}
		}
		
	}
	
	public static Timestamp getCurrentDate(){
		Date currentTime = new Date();   
//		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");   
//		String dateString = formatter.format(currentTime);
		Timestamp nousedate = new Timestamp(currentTime.getTime());    
		return nousedate;
	}
	
	public static void main(String [] agrs){
		System.out.println(new Timestamp(System.currentTimeMillis()));
	}
}
