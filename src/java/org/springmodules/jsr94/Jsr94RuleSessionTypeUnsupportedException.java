/**
 * 
 */
package org.springmodules.jsr94;

/**
 * Maps checked RuleSessionTypeUnsupportedException to unchecked Jsr94Exception subclass 
 * @see RuleSessionTypeUnsupportedException
 * @author janm
 */
public class Jsr94RuleSessionTypeUnsupportedException extends Jsr94Exception {

	/**
	 * The serialVersionUID
	 */
	private static final long serialVersionUID = 3617294540814300470L;

	/**
	 * @param message
	 * @param cause
	 */
	public Jsr94RuleSessionTypeUnsupportedException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public Jsr94RuleSessionTypeUnsupportedException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public Jsr94RuleSessionTypeUnsupportedException(Throwable cause) {
		super(cause);
	}

}
