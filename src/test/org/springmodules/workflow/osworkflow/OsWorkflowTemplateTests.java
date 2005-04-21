
package org.springmodules.workflow.osworkflow;

import java.util.Properties;

import com.opensymphony.workflow.Workflow;
import com.opensymphony.workflow.WorkflowException;
import com.opensymphony.workflow.config.Configuration;
import junit.framework.TestCase;
import org.springmodules.workflow.osworkflow.configuration.ConfigurationBean;
import org.springmodules.workflow.osworkflow.support.WorkflowContext;

import org.springframework.beans.FatalBeanException;

/**
 * @author robh
 */
public class OsWorkflowTemplateTests extends TestCase {

	private static final String WAKE_UP = "wakeup";

	private OsWorkflowTemplate template;

	public void setUp() {
		WorkflowContext.setCaller("robh");
		this.template = new OsWorkflowTemplate();
		this.template.setWorkflowName(WAKE_UP);

		ConfigurationBean configuration = new ConfigurationBean();

		Properties workflowLocations = new Properties();
		workflowLocations.put(WAKE_UP, "classpath:org/springmodules/workflow/osworkflow/wakeUp.xml");
		configuration.setWorkflowLocations(workflowLocations);

		this.template.setConfiguration(configuration);
	}

	public void testWithoutWorkflowName() throws Exception {
		try {
			new OsWorkflowTemplate().afterPropertiesSet();
			fail("Cannot create OsWorkflowTemplate without workflow name");
		}
		catch (FatalBeanException ex) {
			//success
		}
	}

	public void testExecute() {
		this.template.execute("robh", new OsWorkflowCallback() {
			public Object doWithWorkflow(Workflow workflow) throws WorkflowException {
				assertNotNull("Workflow should not be null", workflow);
				String[] workflowNames = workflow.getWorkflowNames();
				assertEquals("wakeup", workflowNames[0]);
				return null;
			}
		});
	}

	public void testInitialize() {
		this.template.initialize();
		assertContextHasInstanceId();
	}


	public void testWithSpecificInitialAction() {
		this.template.setInitialAction(new Integer(3));
		this.template.initialize(0);
		assertContextHasInstanceId();
	}

	private void assertContextHasInstanceId() {
		assertTrue("WorkflowInstance not set in context", WorkflowContext.getInstanceId() > -1);
	}
}
