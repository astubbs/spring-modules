
package org.springmodules.examples.workflow.osworkflow.service.functions;

import java.util.List;
import java.util.Map;

import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;
import org.springmodules.examples.workflow.osworkflow.service.OsWorkflowDocumentApprovalWorkflow;

/**
 * @author robh
 */
public class StoreCommentsFunction implements FunctionProvider {

	public void execute(Map transientVars, Map args, PropertySet ps) throws WorkflowException {
		Object comments = transientVars.get(OsWorkflowDocumentApprovalWorkflow.KEY_COMMENTS);
		if (comments != null && comments instanceof List) {
			ps.setAsActualType(OsWorkflowDocumentApprovalWorkflow.KEY_COMMENTS, comments);
		}
	}
}
