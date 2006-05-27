package org.springmodules.validation.util.date;

/**
 * <p>Exception thrown when date parsing fails.
 *
 * @author Steven Devijver
 * @since 25-04-2005
 */
public class DateParseException extends Exception {

	public DateParseException(String message) {
		super(message);
	}

	public DateParseException(String message, Throwable cause) {
		super(message, cause);
	}

}