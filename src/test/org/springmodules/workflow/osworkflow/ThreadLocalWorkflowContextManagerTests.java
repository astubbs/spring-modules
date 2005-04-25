
package org.springmodules.workflow.osworkflow;

import junit.framework.TestCase;

/**
 * @author robh
 */
public class ThreadLocalWorkflowContextManagerTests extends AbstractWorkflowContextManagerTests {

	protected WorkflowContextManager getManager() {
		return new ThreadLocalWorkflowContextManager();
	}
}
