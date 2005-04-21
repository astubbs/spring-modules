
package org.springmodules.workflow;

import org.springframework.core.NestedRuntimeException;

/**
 * @author robh
 */
public class WorkflowException extends NestedRuntimeException {

	/**
	 * Construct a <code>WorkflowException</code> with the specified detail message.
	 *
	 * @param msg the detail message
	 */
	public WorkflowException(String msg) {
		super(msg);
	}

	public WorkflowException(String msg, Throwable ex) {
		super(msg, ex);
	}
}
