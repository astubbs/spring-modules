
package org.springmodules.workflow.osworkflow;

/**
 * @author robh
 */
public interface WorkflowContextManager {

	void clear();
	
	void setCaller(String caller);
	String getCaller();

	void setInstanceId(long instanceId);
	long getInstanceId();

	boolean isInstanceIdBound();
	boolean isCallerBound();
}
