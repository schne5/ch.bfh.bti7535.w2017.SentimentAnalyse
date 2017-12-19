package helper;

import java.text.SimpleDateFormat;
import java.util.Date;

public final class Util {
	
	private Util(){}
	
	/**
	 * write time stamp and value to standard output stream
	 * @param value Value to write
	 */
	public static void print(String value) {
		String timeStamp = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date());
		System.out.format("%s: %s %n", timeStamp, value);
	}
}
