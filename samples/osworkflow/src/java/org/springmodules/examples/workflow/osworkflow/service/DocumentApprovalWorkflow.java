
package org.springmodules.examples.workflow.osworkflow.service;

import java.util.List;

import org.springmodules.examples.workflow.osworkflow.model.Comment;
import org.springmodules.examples.workflow.osworkflow.model.Document;

/**
 * @author robh
 */
public interface DocumentApprovalWorkflow {

	void startNewWorkflow();

	void uploadDocument(Document document);

	void passBack(Comment comment);

	void approveCurrentDocument();

	List getCurrentStepDescriptors();

	List getHistoryStepDescriptors();

	List getAvailableActionDescriptors();

	String getCurrentState();

	List getUnderwayWorkflows();

	Document getCurrentDocument();

	List getCurrentComments();
}
