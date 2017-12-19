package helper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public final class Util {
	
	private Util(){}
	
	/**
	 * write value to standard output with timestamp
	 * @param value
	 */
	public static void print(String value) {
		String timeStamp = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date());
		System.out.format("%s: %s %n", timeStamp, value);
	}
}
