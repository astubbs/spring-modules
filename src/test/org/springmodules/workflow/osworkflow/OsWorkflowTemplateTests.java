
package org.springmodules.workflow.osworkflow;

import java.util.Properties;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import com.opensymphony.workflow.Workflow;
import com.opensymphony.workflow.WorkflowException;
import com.opensymphony.workflow.loader.StepDescriptor;
import com.opensymphony.workflow.loader.ActionDescriptor;
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

	private WorkflowContextManager manager = new ThreadLocalWorkflowContextManager();

	public void setUp() {
		this.manager.clear();
		this.manager.setCaller("robh");

		Properties workflowLocations = new Properties();
		workflowLocations.put(WAKE_UP, "classpath:org/springmodules/workflow/osworkflow/wakeUp.xml");

		this.configuration = new ConfigurationBean();
		this.configuration.setWorkflowLocations(workflowLocations);

	}

	public void testWithoutWorkflowName() throws Exception {
		OsWorkflowTemplate template = new OsWorkflowTemplate();
		template.setContextManager(this.manager);
		try {
			template.afterPropertiesSet();
			fail("Cannot create OsWorkflowTemplate without workflow name");
		}
		catch (FatalBeanException ex) {
			//success
		}
	}

	public void testWithoutManager() throws Exception {
    OsWorkflowTemplate template = new OsWorkflowTemplate();
		template.setWorkflowName(WAKE_UP);
		try {
			template.afterPropertiesSet();
			fail("Cannot create OsWorkflowTemplate without context manager");
		}
		catch (FatalBeanException ex) {
			//success
		}
	}

	public void testExecute()  throws Exception{
		MockControl ctl = createMockWorkflowControl();
		final Workflow workflow = (Workflow) ctl.getMock();
		final Object returnObject = new Object();

		OsWorkflowTemplate template = new OsWorkflowTemplate() {
			protected Workflow createWorkflow(String caller) throws WorkflowException {
				assertEquals("Incorrect caller", CALLER, caller);
				return workflow;
			}
		};

		template.setContextManager(this.manager);

		Object retVal = template.execute(new OsWorkflowCallback() {
					public Object doWithWorkflow(Workflow innerWorkflow) throws WorkflowException {
						assertSame("Incorrect workflow object passed in", workflow, innerWorkflow);
						return returnObject;
					}
				});

		assertSame("Incorrect value returned by execute", retVal, returnObject);
	}

	public void testDefaultCreatWorkflowImplementation() throws Exception {
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

	public void testInitialize()  throws Exception{
		MockControl ctl = createMockWorkflowControl();
		OsWorkflowTemplate template = createMockTemplateForInitialize(ctl, 0, null);

		setProperties(template);
		template.initialize();

		ctl.verify();

		assertContextHasMockInstanceId();
	}


	public void testInitializeWithSpecificInitialAction()  throws Exception{
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
		bindMockInstanceIdToContext();

		final int actionId = 1;
		final MockControl ctl = createMockWorkflowControl();
		OsWorkflowTemplate template = createMockTemplateForDoAction(ctl, actionId, null);

		setProperties(template);
		template.doAction(actionId);

		ctl.verify();
	}

	public void testDoActionWithInputs() throws Exception {
		bindMockInstanceIdToContext();

		final int actionId = 1;
		final MockControl ctl = createMockWorkflowControl();
		final Map inputs = new HashMap();
		OsWorkflowTemplate template = createMockTemplateForDoAction(ctl, actionId, inputs);

		setProperties(template);
		template.doAction(actionId, inputs);

		ctl.verify();
	}

	public void testGetWorkflowDescriptor()  throws Exception{
		bindMockInstanceIdToContext();

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

	public void testGetHistorySteps() throws Exception {
		bindMockInstanceIdToContext();

		final MockControl ctl = createMockWorkflowControl();
		final List mockSteps = new ArrayList();

		OsWorkflowTemplate template = new OsWorkflowTemplate(){
			protected Workflow createWorkflow(String caller) throws WorkflowException {
				Workflow workflow = (Workflow)ctl.getMock();
				workflow.setConfiguration(OsWorkflowTemplateTests.this.configuration);
				workflow.getHistorySteps(MOCK_INSTANCE_ID);
				ctl.setReturnValue(mockSteps);
        ctl.replay();

				return workflow;
			}
		};

		setProperties(template);

		assertEquals("Incorrect value returned for history steps", mockSteps, template.getHistorySteps());

		ctl.verify();
	}

	public void testGetHistoryStepDescriptors()  throws Exception{
		OsWorkflowTemplate template = new OsWorkflowTemplate();
		setProperties(template);

		List historyStepDescriptors = null;

		template.initialize();
		template.doAction(1);

		historyStepDescriptors = template.getHistoryStepDescriptors();

		assertEquals(1, historyStepDescriptors.size());
		assertEquals("Decision Time", ((StepDescriptor)historyStepDescriptors.get(0)).getName());

		template.doAction(3);

		historyStepDescriptors = template.getHistoryStepDescriptors();

		assertEquals(2, historyStepDescriptors.size());
		assertEquals("Spruce Up", ((StepDescriptor)historyStepDescriptors.get(1)).getName());

	}

	public void testGetCurrentStepDescriptors() throws Exception {
		OsWorkflowTemplate template = new OsWorkflowTemplate();
		setProperties(template);

		List currentStepDescriptors = null;

		template.initialize();

		currentStepDescriptors = template.getCurrentStepDescriptors();
		assertEquals(1, currentStepDescriptors.size());
		assertEquals("Decision Time", ((StepDescriptor)currentStepDescriptors.get(0)).getName());

		template.doAction(1);

		currentStepDescriptors = template.getCurrentStepDescriptors();
		assertEquals(1, currentStepDescriptors.size());
		assertEquals("Spruce Up", ((StepDescriptor)currentStepDescriptors.get(0)).getName());
	}

	public void testGetCurrentSteps()  throws Exception{
		bindMockInstanceIdToContext();

		final MockControl ctl = createMockWorkflowControl();
		final List mockSteps = new ArrayList();

		OsWorkflowTemplate template = new OsWorkflowTemplate(){
			protected Workflow createWorkflow(String caller) throws WorkflowException {
				Workflow workflow = (Workflow)ctl.getMock();
				workflow.setConfiguration(OsWorkflowTemplateTests.this.configuration);
				workflow.getCurrentSteps(MOCK_INSTANCE_ID);
				ctl.setReturnValue(mockSteps);
        ctl.replay();

				return workflow;
			}
		};

		setProperties(template);

		assertEquals("Incorrect value returned for current steps", mockSteps, template.getCurrentSteps());

		ctl.verify();
	}

	public void testGetEntryState() throws Exception {
		bindMockInstanceIdToContext();

		final MockControl ctl = createMockWorkflowControl();
		final int mockEntryState = 98;

		OsWorkflowTemplate template = new OsWorkflowTemplate(){
			protected Workflow createWorkflow(String caller) throws WorkflowException {
				Workflow workflow = (Workflow)ctl.getMock();

				// expect setConfiguration
				workflow.setConfiguration(OsWorkflowTemplateTests.this.configuration);
				workflow.getEntryState(MOCK_INSTANCE_ID);
				ctl.setReturnValue(mockEntryState);

				ctl.replay();
				return workflow;
			}
		};

		setProperties(template);

		assertEquals("Incorrect entry state returned", mockEntryState, template.getEntryState());
		ctl.verify();
	}

	public void testGetAvailableActions() throws Exception {
		OsWorkflowTemplate template = new OsWorkflowTemplate();
		setProperties(template);

		template.initialize();

		int[] actions = null;

		actions = template.getAvailableActions();

		assertEquals("Invalid number of actions", 2, actions.length);
		assertEquals("Invalid action", 1, actions[0]);
		assertEquals("Invalid action", 2, actions[1]);

		template.doAction(1);

		actions = template.getAvailableActions();
		assertEquals(1, actions.length);
		assertEquals(3, actions[0]);
	}

	public void testGetAvailableActionDescriptors() throws Exception {
		OsWorkflowTemplate template = new OsWorkflowTemplate();
		setProperties(template);

		template.initialize();

		List actionDescriptors = null;

		actionDescriptors = template.getAvailableActionDescriptors();

		assertEquals(2, actionDescriptors.size());
		assertEquals("Get out of Bed", ((ActionDescriptor)actionDescriptors.get(0)).getName());
		assertEquals("Go Back to Sleep", ((ActionDescriptor)actionDescriptors.get(1)).getName());

		template.doAction(1);

		actionDescriptors = template.getAvailableActionDescriptors();
		assertEquals(1, actionDescriptors.size());
		assertEquals("Get Dressed", ((ActionDescriptor)actionDescriptors.get(0)).getName());
	}

	private void bindMockInstanceIdToContext() {
		this.manager.setInstanceId(MOCK_INSTANCE_ID);
	}


	private void assertContextHasMockInstanceId() {
		assertEquals("InstanceId is incorrect", MOCK_INSTANCE_ID, this.manager.getInstanceId());
	}

	private MockControl createMockWorkflowControl() {
		return MockControl.createControl(Workflow.class);
	}

	private void setProperties(OsWorkflowTemplate template) throws Exception {
		template.setConfiguration(configuration);
		template.setWorkflowName(WAKE_UP);
		template.setContextManager(this.manager);
		template.afterPropertiesSet();
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
