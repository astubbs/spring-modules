
package org.springmodules.workflow.osworkflow;

/**
 * @author robh
 */
public class OsWorkflowContext {


	private Long instanceId;

	private String caller;

	public long getInstanceId() {
		return instanceId.longValue();
	}

	public void setInstanceId(long instanceId) {
		this.instanceId = new Long(instanceId);
	}

	public String getCaller() {
		return caller;
	}

	public void setCaller(String caller) {
		this.caller = caller;
	}

	public boolean hasInstanceId() {
		return (instanceId != null);
	}
}
