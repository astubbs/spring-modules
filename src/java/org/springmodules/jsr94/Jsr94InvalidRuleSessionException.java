/**
 * 
 */
package org.springmodules.jsr94;

/**
 * Maps checked InvalidRuleSessionException to unchecked Jsr94InvalidRuleSessionException
 * 
 * @see javax.rules.InvalidRuleSessionException
 * @author janm
 */
public class Jsr94InvalidRuleSessionException extends Jsr94Exception {

	/**
	 * The serialVersionUID
	 */
	private static final long serialVersionUID = 3256722896524489778L;

	/**
	 * @param message
	 * @param cause
	 */
	public Jsr94InvalidRuleSessionException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public Jsr94InvalidRuleSessionException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public Jsr94InvalidRuleSessionException(Throwable cause) {
		super(cause);
	}

}
