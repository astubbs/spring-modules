/**
 * Created on Jan 23, 2006
 *
 * $Id$
 * $Revision$
 */
package org.springmodules.workflow.jbpm31;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jbpm.context.exe.ContextInstance;
import org.jbpm.graph.def.ActionHandler;
import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.graph.exe.Token;
import org.jbpm.graph.node.DecisionHandler;
import org.jbpm.taskmgmt.def.AssignmentHandler;
import org.jbpm.taskmgmt.def.TaskControllerHandler;
import org.jbpm.taskmgmt.exe.Assignable;
import org.jbpm.taskmgmt.exe.TaskInstance;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.access.BeanFactoryLocator;
import org.springframework.beans.factory.access.BeanFactoryReference;

/**
 * Handler that allow retrieval of beans defined in the Spring container from
 * within jBPM. The class implements the family of jBPM handler interfaces
 * namely: ActionHandler, AssignmentHandler, DecisionHandler,
 * TaskControllerHandler. The class can be used through jBPM delegation
 * facilities, for example:
 * 
 * <pre>
 *   	&lt;action config-type=&quot;beanName&quot; class=&quot;org.springframework.workflow.jbpm.SpringHandler&quot;&gt;
 *  		&lt;beanName&gt;SpringJbpmAction&lt;/beanName&gt;
 *        &lt;factoryKey&gt;myFactoryInstance&lt;factoryName&gt;
 *  	&lt;/action&gt;
 * </pre>
 * 
 * where beanName represents a jBPM actionHandler defined inside Spring
 * container using it's capabilities (IoC, AOP, etc). The optional factoryKey
 * parameter is used to specify the key under which the bean factory can be
 * found; if there is only one JbpmFactoryLocator inside the classloader,
 * factoryKey can be skipped.
 * 
 * @author Costin Leau
 * 
 */
public class JbpmHandlerProxy implements ActionHandler, AssignmentHandler, DecisionHandler, TaskControllerHandler {

	private static final Log logger = LogFactory.getLog(JbpmHandlerProxy.class);

	private String factoryKey;

	/**
	 * Spring beanName name.
	 */
	private String targetBean;

	/**
	 * @return Returns the beanName.
	 */
	public String getTargetBean() {
		return targetBean;
	}

	/**
	 * @param targetBean The beanName to set.
	 */
	public void setTargetBean(String bean) {
		this.targetBean = bean;
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

		try {
			return factory.getFactory();
		}
		finally {
			factory.release();
		}
	}

	/**
	 * Find the beanName inside the Spring container.
	 * 
	 * @return
	 */
	protected Object lookupBean(Class type) {
		return retrieveBeanFactory().getBean(getTargetBean(), type);
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
	 * @see org.jbpm.graph.def.ActionHandler#execute(org.jbpm.graph.exe.ExecutionContext)
	 */
	public void execute(ExecutionContext executionContext) throws Exception {
		ActionHandler action = (ActionHandler) lookupBean(ActionHandler.class);

		if (logger.isDebugEnabled())
			logger.debug("using Spring-managed actionHandler=" + action);

		action.execute(executionContext);
	}

	/**
	 * @see org.jbpm.taskmgmt.def.AssignmentHandler#assign(org.jbpm.taskmgmt.exe.Assignable,
	 * org.jbpm.graph.exe.ExecutionContext)
	 */
	public void assign(Assignable assignable, ExecutionContext executionContext) throws Exception {
		AssignmentHandler handler = (AssignmentHandler) lookupBean(AssignmentHandler.class);
		if (logger.isDebugEnabled())
			logger.debug("using Spring-managed assignmentHandler=" + handler);

		handler.assign(assignable, executionContext);
	}

	/*
	 * (non-Javadoc)
	 * @see org.jbpm.taskmgmt.def.TaskControllerHandler#initializeTaskVariables(org.jbpm.taskmgmt.exe.TaskInstance,
	 * org.jbpm.context.exe.ContextInstance, org.jbpm.graph.exe.Token)
	 */
	public void initializeTaskVariables(TaskInstance taskInstance, ContextInstance contextInstance, Token token) {
		TaskControllerHandler handler = (TaskControllerHandler) lookupBean(TaskControllerHandler.class);
		if (logger.isDebugEnabled())
			logger.debug("using Spring-managed taskControllerHandler=" + handler);
		handler.initializeTaskVariables(taskInstance, contextInstance, token);
	}

	/*
	 * (non-Javadoc)
	 * @see org.jbpm.taskmgmt.def.TaskControllerHandler#submitTaskVariables(org.jbpm.taskmgmt.exe.TaskInstance,
	 * org.jbpm.context.exe.ContextInstance, org.jbpm.graph.exe.Token)
	 */
	public void submitTaskVariables(TaskInstance taskInstance, ContextInstance contextInstance, Token token) {
		TaskControllerHandler handler = (TaskControllerHandler) lookupBean(TaskControllerHandler.class);
		if (logger.isDebugEnabled())
			logger.debug("using Spring-managed taskControllerHandler=" + handler);
		handler.submitTaskVariables(taskInstance, contextInstance, token);
	}

}
