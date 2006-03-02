/**
 * Created on Jan 23, 2006
 *
 * $Id$
 * $Revision$
 */
package org.springmodules.workflow.jbpm30;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jbpm.graph.def.ActionHandler;
import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.graph.node.DecisionHandler;
import org.jbpm.taskmgmt.def.AssignmentHandler;
import org.jbpm.taskmgmt.exe.Assignable;
import org.jbpm.taskmgmt.exe.TaskInstance;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.access.BeanFactoryLocator;
import org.springframework.beans.factory.access.BeanFactoryReference;

/**
 * Handler that allow retrieval of beans defined in the Spring container from within
 * jBPM. The class implements the family of jBPM handler interfaces namely: ActionHandler,
 * AssignmentHandler, DecisionHandler.
 * The class can be used through jBPM delegation facilities, for example:
 * <pre>
 * 	&lt;action config-type="beanName" class="org.springframework.workflow.jbpm.SpringHandler">
 *		&lt;beanName>SpringJbpmAction&lt;/beanName>
 *      &lt;factoryKey>myFactoryInstance&lt;factoryName>
 *	&lt;/action>
 * </pre>
 * 
 * where beanName represents a jBPM actionHandler defined inside Spring container using
 * it's capabilities (IoC, AOP, etc). The optional factoryKey parameter is used to specify
 * the key under which the bean factory can be found; if there is only one 
 * JbpmFactoryLocator inside the classloader, factoryKey can be skipped. 
 * 
 * @author Costin Leau
 *
 */
public class JbpmHandler implements ActionHandler, AssignmentHandler, DecisionHandler {

	private static final Log logger = LogFactory.getLog(JbpmHandler.class);

	private String factoryKey;

	/**
	 * Spring beanName name.
	 */
	private String beanName;

	/**
	 * @return Returns the beanName.
	 */
	public String getBeanName() {
		return beanName;
	}

	/**
	 * @param beanName The beanName to set.
	 */
	public void setBeanName(String bean) {
		this.beanName = bean;
	}

	/**
	 * @return the bean factory key
	 */
	public String getFactoryKey() {
		return factoryKey;
	}

	/**
	 * @param factoryKey the bean factory key
	 */
	public void setFactoryKey(String factoryKey) {
		this.factoryKey = factoryKey;
	}

	/**
	 * Retrieves the bean factory.
	 * 
	 * @return
	 */
	protected BeanFactory retrieveBeanFactory() {
		BeanFactoryLocator factoryLocator = new JbpmFactoryLocator();
		BeanFactoryReference factory = factoryLocator.useBeanFactory(factoryKey);
		if (factory == null)
			throw new IllegalArgumentException("no beanFactory found under key=" + factoryKey);

		return factory.getFactory();
	}

	/**
	 * Find the beanName inside the Spring container.
	 * 
	 * @return
	 */
	protected Object lookupBean(Class type) {
		return retrieveBeanFactory().getBean(getBeanName(), type);
	}

	/**
	 * @see org.jbpm.graph.node.DecisionHandler#decide(org.jbpm.graph.exe.ExecutionContext)
	 */
	public String decide(ExecutionContext executionContext) throws Exception {
		DecisionHandler handler = (DecisionHandler) lookupBean(DecisionHandler.class);
		if (logger.isDebugEnabled())
			logger.debug("using Spring-managed decisionHandler=" + handler);
		return handler.decide(executionContext);
	}

	/**
	 * @see org.jbpm.taskmgmt.def.TaskControllerHandler#getTaskFormParameters(org.jbpm.taskmgmt.exe.TaskInstance)
	 */
	public List getTaskFormParameters(TaskInstance taskInstance) {
		return null;
	}

	/**
	 * @see org.jbpm.taskmgmt.def.TaskControllerHandler#submitParameters(java.util.Map, org.jbpm.taskmgmt.exe.TaskInstance)
	 */
	public void submitParameters(Map parameters, TaskInstance taskInstance) {
	}

	/**
	 * @see org.jbpm.graph.def.ActionHandler#execute(org.jbpm.graph.exe.ExecutionContext)
	 */
	public void execute(ExecutionContext executionContext) throws Exception {
		ActionHandler action = (ActionHandler) lookupBean(ActionHandler.class);

		if (logger.isDebugEnabled())
			logger.debug("using Spring-managed actionHandler=" + action);

		action.execute(executionContext);
	}

	/**
	 * @see org.jbpm.taskmgmt.def.AssignmentHandler#assign(org.jbpm.taskmgmt.exe.Assignable, org.jbpm.graph.exe.ExecutionContext)
	 */
	public void assign(Assignable assignable, ExecutionContext executionContext) throws Exception {
		AssignmentHandler handler = (AssignmentHandler) lookupBean(AssignmentHandler.class);
		if (logger.isDebugEnabled())
			logger.debug("using Spring-managed assignmentHandler=" + handler);

		handler.assign(assignable, executionContext);
	}

}
