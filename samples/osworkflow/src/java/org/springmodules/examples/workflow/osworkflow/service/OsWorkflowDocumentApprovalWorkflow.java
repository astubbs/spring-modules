
package org.springmodules.examples.workflow.osworkflow.service;

import java.util.List;

import org.springmodules.workflow.osworkflow.OsWorkflowTemplate;
import org.springmodules.workflow.osworkflow.WorkflowContext;

/**
 * @author robh
 */
public class OsWorkflowDocumentApprovalWorkflow implements DocumentApprovalWorkflow {

	private static final int UPLOAD_DOCUMENT = 1;
	
	private OsWorkflowTemplate workflowTemplate;

	public void setWorkflowTemplate(OsWorkflowTemplate workflowTemplate) {
		this.workflowTemplate = workflowTemplate;
	}

	public void start() {
		WorkflowContext.setCaller("robh");
		this.workflowTemplate.initialize();
	}

	public void uploadDocument() {
		WorkflowContext.setCaller("robh");
		this.workflowTemplate.doAction(UPLOAD_DOCUMENT);
	}

	public List getHistorySteps() {
		WorkflowContext.setCaller("robh");
		return this.workflowTemplate.getHistorySteps();
	}

	public int getState() {
		return this.workflowTemplate.getEntryState();
	}
}
