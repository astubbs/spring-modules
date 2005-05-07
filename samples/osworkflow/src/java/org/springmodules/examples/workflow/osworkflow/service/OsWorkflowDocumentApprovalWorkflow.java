
package org.springmodules.examples.workflow.osworkflow.service;

import java.util.ArrayList;
import java.util.List;

import com.opensymphony.workflow.query.Expression;
import com.opensymphony.workflow.query.FieldExpression;
import com.opensymphony.workflow.query.NestedExpression;
import com.opensymphony.workflow.query.WorkflowExpressionQuery;
import com.opensymphony.workflow.spi.Step;
import com.opensymphony.workflow.spi.WorkflowEntry;
import org.springmodules.examples.workflow.osworkflow.model.Comment;
import org.springmodules.examples.workflow.osworkflow.model.Document;
import org.springmodules.workflow.osworkflow.OsWorkflowTemplate;

/**
 * @author robh
 */
public class OsWorkflowDocumentApprovalWorkflow implements DocumentApprovalWorkflow {

	public static final String KEY_DOCUMENT = "document";

	public static final String KEY_COMMENTS = "comments";

	private static final int ACTION_UPLOAD = 1;

	private static final int ACTION_APPROVE = 2;

	private static final int ACTION_PASS_BACK = 3;

	private OsWorkflowTemplate template;

	public void setTemplate(OsWorkflowTemplate template) {
		this.template = template;
	}

	public void startNewWorkflow() {
		template.initialize();
	}

	public void uploadDocument(Document document) {
		template.doAction(ACTION_UPLOAD, KEY_DOCUMENT, document);
	}

	public void passBack(Comment comment) {
		List comments = (List) this.template.getPropertySet().getAsActualType(KEY_COMMENTS);
		if (comments == null) {
			comments = new ArrayList();
		}
		comments.add(comment);
		template.doAction(ACTION_PASS_BACK, KEY_COMMENTS, comments);
	}

	public void approveCurrentDocument() {
		template.doAction(ACTION_APPROVE);
	}

	public List getCurrentStepDescriptors() {
		return this.template.getCurrentStepDescriptors();
	}

	public List getHistoryStepDescriptors() {
		return this.template.getHistoryStepDescriptors();
	}

	public List getAvailableActionDescriptors() {
		return this.template.getAvailableActionDescriptors();
	}

	public String getCurrentState() {
		return ((Step) this.template.getCurrentSteps().get(0)).getStatus();
	}

	public List getUnderwayWorkflows() {
		Expression queryRight = new FieldExpression(FieldExpression.STATE,
						FieldExpression.ENTRY,
						FieldExpression.EQUALS,
						new Integer(WorkflowEntry.ACTIVATED));

		Expression queryLeft = new FieldExpression(FieldExpression.NAME,
						FieldExpression.ENTRY,
						FieldExpression.EQUALS,
						this.template.getWorkflowName());

		WorkflowExpressionQuery query = new WorkflowExpressionQuery(
						new NestedExpression(new Expression[]{queryRight, queryLeft}, NestedExpression.AND));

		return this.template.query(query);
	}

	public Document getCurrentDocument() {
		return (Document) template.getPropertySet().getAsActualType(KEY_DOCUMENT);
	}

	public List getCurrentComments() {
		return (List) template.getPropertySet().getAsActualType(KEY_COMMENTS);
	}
}
