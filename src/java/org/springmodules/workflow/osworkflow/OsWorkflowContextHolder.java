
package org.springmodules.workflow.osworkflow;

/**
 * 
 * @author robh
 */
public abstract class OsWorkflowContextHolder {


	private static ThreadLocal workflowContextHolder = new ThreadLocal() {
		protected Object initialValue() {
			return new OsWorkflowContext();
		}
	};

	public static OsWorkflowContext getWorkflowContext() {
		return (OsWorkflowContext) workflowContextHolder.get();
	}

	public static void clearWorkflowContext() {
		workflowContextHolder.set(new OsWorkflowContext());
	}
}
