/**
 * 
 */

package org.springmodules.jsr94;

/**
 * Maps checked InvalidHandleException to Jsr94InvalidHandleException
 *
 * @author janm
 * @see javax.rules.InvalidHandleException
 */
public class Jsr94InvalidHandleException extends Jsr94Exception {

	/**
	 * The serialVersionUIDs
	 */
	private static final long serialVersionUID = 3257846571638142771L;

	/**
	 * @param message
	 * @param cause
	 */
	public Jsr94InvalidHandleException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public Jsr94InvalidHandleException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public Jsr94InvalidHandleException(Throwable cause) {
		super(cause);
	}

}
