/**
 * 
 */

package org.springmodules.jsr94;

import org.springframework.core.NestedRuntimeException;

/**
 * Core Jsr94Exception; All JSR-94 checked exceptions are mapped to subclasses
 * of this class
 *
 * @author janm
 * @see org.springmodules.jsr94.Jsr94InvalidHandleException
 * @see org.springmodules.jsr94.Jsr94InvalidRuleSessionException
 * @see org.springmodules.jsr94.Jsr94RemoteException
 * @see org.springmodules.jsr94.Jsr94RuleExecutionSetNotFoundException
 * @see org.springmodules.jsr94.Jsr94RuleSessionCreateException
 * @see org.springmodules.jsr94.Jsr94RuleSessionTypeUnsupportedException
 */
public class Jsr94Exception extends NestedRuntimeException {

	/**
	 * The serialVersionUID
	 */
	private static final long serialVersionUID = 3258125838984492341L;

	/**
		 * Creates instance of Jsr94Exception with message set
		 *
		 * @param message The message
		 */
	public Jsr94Exception(String message) {
		super(message);
	}

	/**
		 * Creates instance of Jsr94Exception with message and cause set
		 *
		 * @param message The message
		 * @param cause The cause
		 */
	public Jsr94Exception(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Creates instance of Jsr94Exception with cause set
	 * @param cause The cause
	 */
	public Jsr94Exception(Throwable cause) {
		super(cause.getMessage(), cause);
	}

}
