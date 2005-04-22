
package org.springmodules.workflow.osworkflow;

import java.util.Properties;
import java.util.Map;
import java.util.HashMap;

import com.opensymphony.workflow.Workflow;
import com.opensymphony.workflow.WorkflowException;
import com.opensymphony.workflow.basic.BasicWorkflow;
import junit.framework.TestCase;
import org.springmodules.workflow.osworkflow.configuration.ConfigurationBean;
import org.easymock.MockControl;

import org.springframework.beans.FatalBeanException;

/**
 * @author robh
 */
public class OsWorkflowTemplateTests extends TestCase {

	private static final String WAKE_UP = "wakeup";

	private static final String CALLER = "robh";

	private static final long MOCK_INSTANCE_ID = 90L;

	private ConfigurationBean configuration;

	public void setUp() {
		WorkflowContext.clear();
		WorkflowContext.setCaller("robh");

		Properties workflowLocations = new Properties();
		workflowLocations.put(WAKE_UP, "classpath:org/springmodules/workflow/osworkflow/wakeUp.xml");

		this.configuration = new ConfigurationBean();
		this.configuration.setWorkflowLocations(workflowLocations);

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
		MockControl ctl = createMockWorkflowControl();
		final Workflow workflow = (Workflow) ctl.getMock();
		final Object returnObject = new Object();

		OsWorkflowTemplate template = new OsWorkflowTemplate() {
			protected Workflow createWorkflow(String caller) throws WorkflowException {
				assertEquals("Incorrect caller", CALLER, caller);
				return workflow;
			}
		};

		Object retVal = template.execute(new OsWorkflowCallback() {
					public Object doWithWorkflow(Workflow innerWorkflow) throws WorkflowException {
						assertSame("Incorrect workflow object passed in", workflow, innerWorkflow);
						return returnObject;
					}
				});

		assertSame("Incorrect value returned by execute", retVal, returnObject);
	}

	public void testDefaultCreatWorkflowImplementation() {
		OsWorkflowTemplate template = new OsWorkflowTemplate();
		setProperties(template);

		template.execute(new OsWorkflowCallback() {
			public Object doWithWorkflow(Workflow workflow) throws WorkflowException {
				assertTrue(workflow instanceof BasicWorkflow);
				assertSame(((BasicWorkflow) workflow).getConfiguration(), OsWorkflowTemplateTests.this.configuration);
				return null;
			}
		});
	}

	public void testInitialize() {
		MockControl ctl = createMockWorkflowControl();
		OsWorkflowTemplate template = createMockTemplateForInitialize(ctl, 0, null);

		setProperties(template);
		template.initialize();

		ctl.verify();

		assertContextHasMockInstanceId();
	}


	public void testInitializeWithSpecificInitialAction() {
		int initialAction = 0;
		MockControl ctl = createMockWorkflowControl();

		OsWorkflowTemplate template = createMockTemplateForInitialize(ctl, initialAction, null);

		setProperties(template);
		template.setInitialAction(new Integer(3));
		template.initialize(initialAction);

		ctl.verify();

		assertContextHasMockInstanceId();
	}


	public void testInitializeWithSpecificInitialActionAndInputs() throws Exception {
		Map inputs = new HashMap();
		int initialAction = 0;
		MockControl ctl = createMockWorkflowControl();

		OsWorkflowTemplate template = createMockTemplateForInitialize(ctl, initialAction, inputs);

		setProperties(template);
		template.setInitialAction(new Integer(3));
		template.initialize(initialAction, inputs);

		ctl.verify();

		assertContextHasMockInstanceId();
	}

	public void testInitializeWithInputs() throws Exception {
		Map inputs = new HashMap();
		MockControl ctl = createMockWorkflowControl();

		OsWorkflowTemplate template = createMockTemplateForInitialize(ctl, 0, inputs);

		setProperties(template);
		template.initialize(inputs);

		ctl.verify();

		assertContextHasMockInstanceId();
	}

	public void testDoAction() throws Exception {
		final int actionId = 1;
		final MockControl ctl = createMockWorkflowControl();
		WorkflowContext.setInstanceId(MOCK_INSTANCE_ID);
		OsWorkflowTemplate template = createMockTemplateForDoAction(ctl, actionId, null);

		setProperties(template);
		template.doAction(actionId);

		ctl.verify();
	}

	public void testDoActionWithInputs() throws Exception {
		final int actionId = 1;
		final MockControl ctl = createMockWorkflowControl();
		final Map inputs = new HashMap();
		WorkflowContext.setInstanceId(MOCK_INSTANCE_ID);
		OsWorkflowTemplate template = createMockTemplateForDoAction(ctl, actionId, inputs);

		setProperties(template);
		template.doAction(actionId, inputs);

		ctl.verify();
	}

	public void testGetWorkflowDescriptor() {
		WorkflowContext.setInstanceId(MOCK_INSTANCE_ID);

		final MockControl ctl = createMockWorkflowControl();

		OsWorkflowTemplate template = new OsWorkflowTemplate(){
			protected Workflow createWorkflow(String caller) throws WorkflowException {
				Workflow workflow = (Workflow)ctl.getMock();
				workflow.setConfiguration(OsWorkflowTemplateTests.this.configuration);
				workflow.getWorkflowDescriptor(WAKE_UP);
				ctl.setReturnValue(null);
        ctl.replay();

				return workflow;
			}
		};

		setProperties(template);
		assertNull(template.getWorkflowDescriptor());
		ctl.verify();
	}


	private void assertContextHasMockInstanceId() {
		assertEquals("InstanceId is incorrect", MOCK_INSTANCE_ID, WorkflowContext.getInstanceId());
	}

	private MockControl createMockWorkflowControl() {
		return MockControl.createControl(Workflow.class);
	}

	private void setProperties(OsWorkflowTemplate template) {
		template.setConfiguration(configuration);
		template.setWorkflowName(WAKE_UP);
	}

	private OsWorkflowTemplate createMockTemplateForDoAction(final MockControl ctl, final int actionId, final Map inputs) {
		return new OsWorkflowTemplate() {
			protected Workflow createWorkflow(String caller) throws WorkflowException {
				Workflow workflow = (Workflow) ctl.getMock();
				// check that configuration is set
				workflow.setConfiguration(OsWorkflowTemplateTests.this.configuration);

				// check that doAction is called as expected
				workflow.doAction(MOCK_INSTANCE_ID, actionId, inputs);
				ctl.replay();
				return workflow;
			}
		};
	}

	private OsWorkflowTemplate createMockTemplateForInitialize(final MockControl control, final int initialAction, final Map inputs) {
		return new OsWorkflowTemplate() {
			protected Workflow createWorkflow(String caller) throws WorkflowException {

				// check that correct caller is passed in
				assertEquals("Caller is incorrect", CALLER, caller);

				Workflow workflow = (Workflow) control.getMock();

				// configuration is always set first
				workflow.setConfiguration(OsWorkflowTemplateTests.this.configuration);

				// expect the initialize call and return the mock instance id
				workflow.initialize(WAKE_UP, initialAction, inputs);
				control.setReturnValue(MOCK_INSTANCE_ID);

				control.replay();
				return workflow;
			}
		};
	}

}
