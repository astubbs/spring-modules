package org.springmodules.ant.task;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.springframework.beans.factory.BeanFactory;
import org.springmodules.ant.util.BeanFactoryLoader;


/**
 * Base class for tasks that need a bean factory.  To use it
 * there needs to be a beanRefContext.xml on the classpath with a BeanFactory
 * configured as a bean identified by the factoryKey parameter.
 * 
 * @author Dave Syer
 *
 */
public abstract class AbstractSpringBeanFactoryTask extends Task {

	protected String contextRef = BeanFactoryLoader.DEFAULT_CONTEXT_REF;

	protected String factoryKey = "beanFactory";

	/**
	 * The key to look up context of bean factories. Default value is 
	 * BeanFactoryLoader.DEFAULT_CONTEXT_REF.
	 * 
	 * @param contextRef
	 */
	public void setContextRef(String contextRef) {
		this.contextRef = contextRef;
	}

	/**
	 * The key to look up bean factory. Default value is "beanFactory".
	 * 
	 * @param factoryKey
	 */
	public void setFactoryKey(String factoryKey) {
		this.factoryKey = factoryKey;
	}

	/**
	 * Load the bean factory.  Utility method for sub classes.
	 */
	public BeanFactory getBeanFactory() throws BuildException {
		return BeanFactoryLoader.getBeanFactory(contextRef, factoryKey);
	}

}

