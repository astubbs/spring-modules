/**
 * 
 */

package org.springmodules.jsr94;

import org.springframework.transaction.TransactionException;

/**
 * Jsr94TransactionException; thrown from Jsr94TransactionManager when transaction processing
 * fails.
 *
 * @author janm
 * @see org.springframework.transaction.TransactionException
 * @see org.springmodules.jsr94.Jsr94TransactionManager
 */
public class Jsr94TransactionException extends TransactionException {

	/**
	 * The serialVersionUID
	 */
	private static final long serialVersionUID = 3761684589533607216L;

	/**
		 * Creates new instance using message and cause
		 *
		 * @param message The message
		 * @param cause The cause
		 */
	public Jsr94TransactionException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
		 * Creates new instance using message
		 *
		 * @param message The message
		 */
	public Jsr94TransactionException(String message) {
		super(message);
	}

	/**
	 * Creates new instance using cause 
	 * @param cause The cause
	 */
	public Jsr94TransactionException(Throwable cause) {
		super(cause.getMessage(), cause);
	}

}
