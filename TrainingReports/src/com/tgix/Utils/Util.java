package com.tgix.Utils;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class Util {

	public static boolean isEmpty(String s) {
		if (s == null)
			return true;
		return (s.trim().length() == 0);
	}

	/**
	 * @param c
	 *            Collection
	 * @return <code>true</code> if Collection (List, Map, Set, etc.) is empty
	 *         or is null.
	 */
	public static boolean isEmpty(Collection c) {
		return (c == null) || (c.size() == 0);
	}

	public static String toEmpty(String str) {
		if (str == null || Util.isEmpty(str)) {
			return " ";
		}
		return str;
	}

	public static String objectToString(Object obj) {
		if (obj == null || Util.isEmpty(obj.toString())) {
			return " ";
		}
		return obj.toString();

	}

	// Checks if the input is one of the list.
	public static boolean IsOneOf(String[] theList, String sInput) {
		if (theList == null || sInput == null)
			return false;

		for (int iCtr = 0; iCtr < theList.length; iCtr++) {
			String sCurr = theList[iCtr];
			if (sCurr != null && sCurr.equalsIgnoreCase(sInput))
				return true;
		}
		return false;
	}

	public static String toEmptyNA(String str) {
		if (str == null || Util.isEmpty(str)) {
			return "N/A";
		}
		return str;
	}

	public static String toEmptyNC(String str) {
		if (str == null || Util.isEmpty(str)) {
			return "NC";
		}
		return str;
	}

	public static String toEmptyL(String str) {
		if (str == null || Util.isEmpty(str)) {
			return "L";
		}
		return str;
	}

	public static String toEmptyNotComplete(String str) {
		if (str == null || Util.isEmpty(str)) {
			return "Not Complete";
		}
		return str;
	}

	public static String toEmptyBlank(String str) {
		if (str == null || Util.isEmpty(str)) {
			return "";
		}
		return str;
	}

	public static String toEmptyNBSP(String str) {
		if (Util.isEmpty(str)) {
			return "&nbsp;";
		}
		return str;
	}

	public static String toNullNBSP(String str) {
		if (str == null || Util.isEmpty(str)) {
			return "&nbsp;";
		}
		return str;
	}

	public static String capitalizeFirst(String s) {
		String firstLetter = s.substring(0, 1);
		return firstLetter.toUpperCase() + s.substring(1, s.length());
	}

	public static Date parseStandardUSDate(String s) {
		if (s == null)
			return null;
		try {
			DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
			return df.parse(s);
		} catch (Exception e) {
			return null;
		}
	}

	public static String formatDateShort(Date date) {
		if (date == null)
			return "";
		DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
		return df.format(date);
	}

	public static String formatDateLong(Date date) {
		if (date == null)
			return "";
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

		return sdf.format(date);
	}

	public static String truncate(String s, int limit) {
		if (limit < 0) {
			throw new IllegalArgumentException("Invalid limit: " + limit);
		}

		if (s != null) {
			if (s.length() > limit) {
				s = s.substring(0, limit);
			}
		}

		return s;
	}

	public static long parseLong(Object o) {
		if (o == null)
			return 0;
		long l = 0;
		try {
			String s = String.valueOf(o);
			l = Long.parseLong(s, 10);
			return l;
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	/**
	 * Converts an Object to an int. null or unparseable becomes 0. Decimals are
	 * ignored.
	 * 
	 * @param o
	 *            Object to convert
	 * @return int value
	 */
	public static int parseInt(Object o) {
		int n = 0;

		if (o != null) {
			String s = o.toString();

			if (s.indexOf(".") > -1) {
				// Chop off the decimal and anything else
				s = truncate(s, s.indexOf("."));
			}

			try {
				n = Integer.parseInt(s, 10);
			} catch (NumberFormatException e) {
				// do nothing, let it return 0
			}
		}

		return n;
	}

	/**
	 * Converts collection of objects into a String delimited by separator. Null
	 * list returns null String, empty list returns empty String.
	 */
	public static String delimit(Collection c, String separator) {
		String s = null; // Your mom was a StringBuffer.

		if (c != null) { // Empty list returns empty string...arbitrary...Makes
							// sense.
			s = "";

			boolean first = true;
			for (Iterator i = c.iterator(); i.hasNext();) {
				String part = String.valueOf(i.next());

				if (first) {
					s += part;
					first = false;
				} else {
					s += separator + part;
				}
			}
		}

		return s;
	}

	public static List parseList(String s, String separator) {
		List list = new ArrayList();

		StringTokenizer ips = new StringTokenizer(s, separator);
		while (ips.hasMoreTokens()) {
			list.add(ips.nextToken());
		}

		return list;
	}

	/**
	 * Preprocess a string literal for inclusion in a dynamic SQL/DQL query by
	 * replacing all single quote characters (') with two adjacent single quote
	 * characters ('').
	 */
	public static String makeSafeQLString(String a_str) {
		return replace(a_str, "'", "''");
	}

	public static final String replace(String srcStr, String oldString,
			String newString) {
		if (srcStr == null) {
			return null;
		}
		int i = 0;
		if ((i = srcStr.indexOf(oldString, i)) >= 0) {
			char[] srcStr2 = srcStr.toCharArray();
			char[] newString2 = newString.toCharArray();
			int oLength = oldString.length();
			StringBuffer buf = new StringBuffer(srcStr2.length);
			buf.append(srcStr2, 0, i).append(newString2);
			i += oLength;
			int j = i;
			while ((i = srcStr.indexOf(oldString, i)) > 0) {
				buf.append(srcStr2, j, i - j).append(newString2);
				i += oLength;
				j = i;
			}
			buf.append(srcStr2, j, srcStr2.length - j);
			return buf.toString().trim();
		}
		return srcStr.trim();
	}

	public static List getDatesBetween(Date startDate, Date endDate) {
		List datesList = new ArrayList();
		Date tmpDate = null;
		DateUtils dUtils = new DateUtils();

		if (startDate != null && endDate != null
				&& dUtils.dateIsGreaterEqual(endDate, startDate)) {
			tmpDate = startDate;
			while (dUtils.dateIsGreaterEqual(endDate, tmpDate)) {
				datesList.add(tmpDate);
				tmpDate = dUtils.incrementByDay(tmpDate);
			}
		}
		return datesList;
	}

	public static String formatStrExcel(String str) {
		if (str != null)
			return "=\"" + str + "\"";
		else
			return "";
	}

	// This returns HashMap(string, blank) for the comma delimited set of
	// strings passed as input
	public static HashMap hashMapFromcommaDelimitedStrings(String sKeyList,
			String sValueList) {
		HashMap hash = new HashMap();
		StringTokenizer stKeyInput = new StringTokenizer(sKeyList, ",");
		StringTokenizer stValueInput = new StringTokenizer(sValueList, ",");

		while (stKeyInput.hasMoreTokens() && stValueInput.hasMoreTokens()) {
			String sCurrKey = stKeyInput.nextToken();
			String sCurrValue = stValueInput.nextToken();
			hash.put(sCurrKey, sCurrValue);
		}
		return hash;
	}

	// This returns the DIFF between two hash maps
	public static HashMap hashMapDiff(HashMap hashFirst, HashMap hashSecond) {
		HashMap hashRet = new HashMap();

		// PreCondition.
		if (hashFirst == null || hashSecond == null)
			return null;

		Iterator iterFirst = hashFirst.entrySet().iterator();
		while (iterFirst.hasNext()) {
			Map.Entry entry = (Map.Entry) iterFirst.next();
			String sFirstKey = (String) entry.getKey();
			String sFirstValue = (String) entry.getValue();

			String sSecondValue = (String) hashSecond.get(sFirstKey);

			if ((sFirstKey != null) && (sFirstValue != null)
					&& (sSecondValue != null)) {
				// There is a difference deteced, Add to result
				if (sFirstValue.equalsIgnoreCase(sSecondValue) == false) {
					hashRet.put(sFirstKey, sSecondValue);
				}
			}
		}
		return hashRet;
	}

	public static String ifNull(Object o, String ifNullStr) {
		if (o != null) {
			return o.toString();
		}
		return ifNullStr;
	}

	public static String ifNull(Object o) {
		return ifNull(o, "");
	}

	public static String ifNull(Object number, NumberFormat formatter,
			String ifNullStr) {
		if (number != null) {
			if (number instanceof Number) {
				return formatter.format((Number) number);
			}
			return number.toString();
		}

		return ifNullStr;
	}

	public static String ifNull(Object number, NumberFormat formatter) {
		return ifNull(number, formatter, "");
	}

	public static String ifNull(Object date, DateFormat formatter,
			String ifNullStr) {
		if (date != null) {
			if (date instanceof Date) {
				return formatter.format((Date) date);
			}
			return date.toString();
		}

		return ifNullStr;
	}

	public static String ifNull(Object date, DateFormat formatter) {
		return ifNull(date, formatter, "");
	}

	/* Added for RBU */
	public static boolean splitFields(String mutipleObj, String toFindObj) {
		String temp = null;
		String[] arr = null;
		if (mutipleObj != null) {
			if (mutipleObj.equalsIgnoreCase("All")) {
				return true;
			}
			/* Adding else if for SCE Feedback Form enhancement */
			else if (mutipleObj.equalsIgnoreCase("None")) {
				return false;
			}
			/* End of addition */
			else {
				if (mutipleObj.equalsIgnoreCase(toFindObj)) {
					// System.out.println("Inside if");
					return true;
				} else {
					arr = mutipleObj.split(";");
					// System.out.println("TEMP------"+arr.length);
					for (int x = 0; x < arr.length; x++) {
						if (arr[x].equalsIgnoreCase(toFindObj)) {
							// System.out.println("Matched"+arr[x]);
							return true;
						}
					}
				}

			}
		}
		return false;
	}

	public static String toAll(String str) {
		if (str == null || Util.isEmpty(str)) {
			return "None";
		}
		return str;
	}

	public static String formatDateSqlObj(Object o) {
		Date tmp = (Date) o;
		return Util.formatDateShort(tmp);
	}

}
