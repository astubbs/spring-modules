/**
 * Created on Feb 23, 2006
 *
 * $Id: SpringContextWiringTests.java,v 1.1 2006/03/02 14:56:04 costin Exp $
 * $Revision: 1.1 $
 */
package org.springmodules.workflow.jbpm31;

import org.jbpm.graph.def.Action;
import org.jbpm.graph.def.ActionHandler;
import org.jbpm.graph.def.ProcessDefinition;
import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.graph.exe.ProcessInstance;
import org.jbpm.graph.exe.Token;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

/**
 * @author Costin Leau
 *
 */
public class SpringContextWiringTests extends AbstractDependencyInjectionSpringContextTests {
	private ProcessDefinition processDefinition;
	
	/**
	 * @see org.springframework.test.AbstractDependencyInjectionSpringContextTests#getConfigLocations()
	 */
	protected String[] getConfigLocations() {
		return new String[] { "org/springmodules/workflow/jbpm31/applicationContext.xml" };
	}

	/**
	 * @see org.springframework.test.AbstractDependencyInjectionSpringContextTests#onSetUp()
	 */
	protected void onSetUp() throws Exception {
		super.onSetUp();
	}

	/**
	 * @see org.springframework.test.AbstractDependencyInjectionSpringContextTests#onTearDown()
	 */
	protected void onTearDown() throws Exception {
		super.onTearDown();
	}

	public void testRetrievalFromSpringContext() throws Exception {
		Action action = processDefinition.getAction("myAction");
		ActionHandler delegate = (ActionHandler) action.getActionDelegation().getInstance();

		// create the context and pass it on to the action
		ProcessInstance instance = processDefinition.createProcessInstance();
		// we have to use transient variables or otherwise HB will get in the way
		instance.getContextInstance().setTransientVariable(DummyActionHandler.TEST_LABEL,
				applicationContext.getBean("jbpmAction"));
		Token token = instance.getRootToken();

		delegate.execute(new ExecutionContext(token));
	}

	/**
	 * @return Returns the processDefinition.
	 */
	public ProcessDefinition getProcessDefinition() {
		return processDefinition;
	}

	/**
	 * @param processDefinition The processDefinition to set.
	 */
	public void setProcessDefinition(ProcessDefinition processDefinition) {
		this.processDefinition = processDefinition;
	}

}
