
package org.springmodules.workflow.osworkflow;

import junit.framework.TestCase;

import org.springframework.beans.FatalBeanException;

import org.springmodules.workflow.osworkflow.support.WorkflowContextManager;

/**
 * @author robh
 */
public class OsWorkflowTemplateTests extends TestCase {

	public void testWithoutWorkflowName() throws Exception {
		try {
			new OsWorkflowTemplate().afterPropertiesSet();
			fail("Cannot create OsWorkflowTemplate without workflow name");
		}
		catch (FatalBeanException ex) {
			//success
		}
	}


	private void setCaller() {
		WorkflowContextManager.bindResource("Caller", "robh");
	}

}
