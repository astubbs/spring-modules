
package org.springmodules.workflow.osworkflow;

import com.opensymphony.workflow.Workflow;
import com.opensymphony.workflow.WorkflowException;

/**
 * @author robh
 */
public interface OsWorkflowCallback {

	Object doWithWorkflow(Workflow workflow) throws WorkflowException;
}
