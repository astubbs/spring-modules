package org.springmodules.ant.task;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

/**
 * General purpose Ant task that allows us to autowire a task in the project
 * from a Spring container. To use it there needs to be a beanRefContext.xml on
 * the classpath with a BeanFactory configured as a bean identified by the
 * factoryKey parameter.
 * 
 * @author Dave Syer
 * 
 */
public class SpringDependencyInjectorTask extends AbstractSpringBeanFactoryTask {

	/**
	 * Legal value of autorwire attribute for autowire by name.
	 */
	public static final String AUTOWIRE_BY_NAME = "byName";

	/**
	 * Legal value of autorwire attribute for autowire by type.
	 */
	public static final String AUTOWIRE_BY_TYPE = "byType";

	private String taskref;

	private String autowire = AUTOWIRE_BY_NAME;

	/**
	 * Setter for the task reference
	 * 
	 * @param name
	 */
	public void setTaskRef(String name) {
		this.taskref = name;
	}

	/**
	 * Setter for the autowire property. Legal values are byName (default) and
	 * byType.
	 * 
	 * @param autowire
	 */
	public void setAutowire(String autowire) {
		this.autowire = autowire;
	}

	/**
	 * Grab the task referred to by the taskref attribute, and autowire it from
	 * the bean factory discovered from context ref and factory key.
	 * 
	 * @see org.apache.tools.ant.Task#execute()
	 */
	public void execute() throws BuildException {

		BeanFactory parentBeanFactory = getBeanFactory();
		Project project = getProject();

		if ((taskref == null) || !project.getReferences().containsKey(taskref)) {
			throw new BuildException(
					"The project does not contain the required reference: ["
							+ taskref + "]");
		}

		// Check that the reference we were given is a valid Task instance
		Object ref = project.getReference(taskref);
		if (!(ref instanceof Task)) {
			throw new BuildException("Reference (" + taskref
					+ ") does not refer to a task");
		}
		Task target = (Task) ref;

		// Assign the autowire value for the bean factory
		int autowireValue = AutowireCapableBeanFactory.AUTOWIRE_BY_NAME;
		if (AUTOWIRE_BY_TYPE.equals(autowire)) {
			autowireValue = AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE;
		}

		DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
		beanFactory.setParentBeanFactory(parentBeanFactory);
		beanFactory.registerSingleton(taskref, target);

		log("Autowiring reference: " + taskref);

		beanFactory.autowireBeanProperties(target, autowireValue, false);
	}

}
