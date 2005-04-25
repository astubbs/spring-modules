package org.springmodules.util.dateparser;

/**
 * <p>Exception thrown when date parsing fails.
 * 
 * @author Steven Devijver
 * @since 25-04-2005
 */
public class DateParseException extends Exception {

	public DateParseException() {
		super();
	}

	public DateParseException(String arg0) {
		super(arg0);
	}

	public DateParseException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public DateParseException(Throwable arg0) {
		super(arg0);
	}

}
