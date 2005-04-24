
package org.springmodules.examples.workflow.osworkflow.service;

import java.util.List;

/**
 * @author robh
 */
public interface DocumentApprovalWorkflow {

	void start();

	void uploadDocument();

	List getHistorySteps();

	List getAvailableActionDescriptors();

	int getState();
}
