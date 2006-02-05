/**
 * 
 */

package org.springmodules.jsr94;

/**
 * Maps checked RuleSessionCreateException to unchecked Jsr94Exception
 *
 * @author janm
 * @see javax.rules.RuleSessionCreateException
 */
public class Jsr94RuleSessionCreateException extends Jsr94Exception {

	/**
	 * The serialVersionUID
	 */
	private static final long serialVersionUID = 3833462929042715955L;

	/**
	 * @param message
	 * @param cause
	 */
	public Jsr94RuleSessionCreateException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public Jsr94RuleSessionCreateException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public Jsr94RuleSessionCreateException(Throwable cause) {
		super(cause);
	}

}
