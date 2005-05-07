
package org.springmodules.examples.workflow.osworkflow.service.functions;

import java.util.Map;

import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;
import org.springmodules.examples.workflow.osworkflow.model.Document;
import org.springmodules.examples.workflow.osworkflow.service.OsWorkflowDocumentApprovalWorkflow;

/**
 * @author robh
 */
public class StoreDocumentFunction implements FunctionProvider {

	public void execute(Map transientVars, Map args, PropertySet ps) throws WorkflowException {
		Object document = transientVars.get(OsWorkflowDocumentApprovalWorkflow.KEY_DOCUMENT);
		if (document != null && document instanceof Document) {
			ps.setAsActualType(OsWorkflowDocumentApprovalWorkflow.KEY_DOCUMENT, document);
		}
	}
}
