package org.essentialframework.core.utility;

/**
 * Convenient helper class for {@link String} inspection.
 * 
 * @author dmalis
 * @version 1.0
 */
public class StringUtils {

	/**
	 * No instantiation.
	 */
	private StringUtils() {}
	
	/**
	 * Checks if specified string is null or empty.
	 * @param s
	 * @return {@code true/false}
	 */
	public static boolean isEmpty(final String s) {
		return s == null || s.length() <= 0;
	}
	
	/**
	 * Checks if specified string contains some characters
	 * other than whitespace.
	 * @param s
	 * @return {@code true/false}
	 */
	public static boolean hasText(final String s) {
		if(isEmpty(s)) {
			return false;
		}
		for( int i=0; i<s.length(); i++ ) {
			if( !Character.isWhitespace(s.charAt(i)) ){
				return true;
			}
		}
		return false;
	}
	
}
