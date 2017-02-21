package org.openmrs.module.chaiemr;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CommonUtils {
	static DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static Date parseDate(String s) throws ParseException {
		if (s == null || s.length() == 0) {
			return null;
		} else {
			if (s.length() == 10) {
				s += " 00:00:00";
			}
			return df.parse(s);
		}
	}
	
	public static String format(Date date) {
		return df.format(date);
	}
}
