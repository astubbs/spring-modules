
package org.springmodules.workflow.osworkflow.support;

import org.springmodules.workflow.WorkflowException;

/**
 * @author robh
 */
public class InvalidWorkflowContextStateException extends WorkflowException {

	/**
	 * Construct a <code>WorkflowException</code> with the specified detail message.
	 *
	 * @param msg the detail message
	 */
	public InvalidWorkflowContextStateException(String msg) {
		super(msg);
	}

	public InvalidWorkflowContextStateException(String msg, Throwable ex) {
		super(msg, ex);
	}
}
