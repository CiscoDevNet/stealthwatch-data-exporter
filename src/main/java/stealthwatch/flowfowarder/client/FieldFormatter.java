package stealthwatch.flowfowarder.client;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The Field Formatter utilities.
 */
public class FieldFormatter {

	/**
	 * The Enum FieldFormatterType.
	 */
	public enum FieldFormatterType {
		
		/** IP address. */
		IP,
		
		/** Mac address. */
		MAC,
		
		/** Date. */
		DATE;
	}

	/** The Constant for DOT. */
	private static final String DOT = ".";
	
	/** The Constant for COLON. */
	private static final String COLON = ":";
	
	/**
	 * Format IP address.
	 *
	 * @param first the first value from the protobuf
	 * @param second the second from the protobuf
	 * @return the formatted string
	 */
	public static String formatIPAddress(long first, long second) {
		StringBuffer result = new StringBuffer("");
		if (second == 0 & (first & 0xFFFFFFFF00000000L) == 0) {
			// IPv4
			int ip1 = (int) (first >>> 24);
			int ip2 = ((int) (first >>> 16)) & 0xFF;
			int ip3 = ((int) (first >>> 8)) & 0xFF;
			int ip4 = ((int) first) & 0xFF;
			result.append(ip1).append(DOT).append(ip2).append(DOT).append(ip3).append(DOT).append(ip4);
		} else {
			// IPv6
			ByteBuffer buf = ByteBuffer.allocate(16);
			buf.putLong(first);
			buf.putLong(first);
			try {
				result.append(InetAddress.getByAddress(buf.array()).getHostAddress());
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		}
		return result.toString();
	}

	/**
	 * Format mac address.
	 *
	 * @param value the value
	 * @return the string
	 */
	public static String formatMacAddress(long value) {
		StringBuffer result = new StringBuffer("");
		int m1 = ((int) (value >>> 40)) & 0xFF;
		int m2 = ((int) (value >>> 32)) & 0xFF;
		int m3 = ((int) (value >>> 24)) & 0xFF;
		int m4 = ((int) (value >>> 16)) & 0xFF;
		int m5 = ((int) (value >>> 8)) & 0xFF;
		int m6 = ((int) (value)) & 0xFF;
		result.append(String.format("%02X", m1)).append(COLON).append(String.format("%02X", m2));
		result.append(COLON).append(String.format("%02X", m3)).append(COLON).append(String.format("%02X", m4));
		result.append(COLON).append(String.format("%02X", m5)).append(COLON).append(String.format("%02X", m6));
		return result.toString();
	}

	/**
	 * Format date from microseconds value.
	 *
	 * @param microseconds the microseconds
	 * @return the string
	 */
	public static String formatDateFromMicroseconds(long microseconds) {
		long milliseconds = microseconds / 1000L;
		long remaining = microseconds % 1000L;
		Date date = new Date(milliseconds);
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		return format.format(date) + String.format("%03d", remaining);
	}

}
