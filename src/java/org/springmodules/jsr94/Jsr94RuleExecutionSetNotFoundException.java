/**
 * 
 */
package org.springmodules.jsr94;

/**
 * Maps checked RuleExecutionSetNotFoundException to unchecked exception
 * 
 * @author janm
 */
public class Jsr94RuleExecutionSetNotFoundException extends Jsr94Exception {

	/**
	 * The serialVersionUID
	 */
	private static final long serialVersionUID = 3257853194510808628L;

	/**
	 * @param message
	 * @param cause
	 */
	public Jsr94RuleExecutionSetNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public Jsr94RuleExecutionSetNotFoundException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public Jsr94RuleExecutionSetNotFoundException(Throwable cause) {
		super(cause);
	}

}
