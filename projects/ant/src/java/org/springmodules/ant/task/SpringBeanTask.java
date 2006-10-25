package org.springmodules.ant.task;
import java.util.Map;

import ognl.Ognl;
import ognl.OgnlException;

import org.apache.tools.ant.BuildException;
import org.springframework.beans.factory.BeanFactory;


/**
 * General purpose Ant task that allows us to call methods on Spring beans.
 * Works by evaluating an OGNL expression with the bean as root.  To use it
 * there needs to be a beanRefContext.xml on the classpath with a BeanFactory
 * configured as a bean identified by the factoryKey parameter.
 * 
 * @author Dave Syer
 *
 */
public class SpringBeanTask extends AbstractSpringBeanFactoryTask {

	private String name;

	private String expression;

	private String property;

	/**
	 * The name of the property to set with the generated result. Ignored if
	 * null;
	 * 
	 * @param property
	 */
	public void setProperty(String property) {
		this.property = property;
	}

	/**
	 * An expression to execute on the target bean. The expression will be
	 * evaluated in OGNL with the bean as root.
	 * 
	 * @param expression
	 */
	public void setExpression(String expression) {
		this.expression = expression;
	}

	/**
	 * Setter for the bean name property (must be a valid bean)
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/*
	 * @see org.apache.tools.ant.Task#execute()
	 */
	public void execute() throws BuildException {
		if (this.name == null) {
			throw new BuildException("Cannot locate a null bean name");
		}

		if (this.expression == null) {
			throw new BuildException("Cannot execute a null expression");
		}

		BeanFactory beanFactory = getBeanFactory();

		if ((name == null) || !beanFactory.containsBean(name)) {
			throw new BuildException(
					"The BeanFactory does not contain the required bean: ["
							+ name + "]");
		}

		Object target = beanFactory.getBean(name);

		// TODO: set properties on the bean first (or just use OGNL)
		try {
			Map context = Ognl.createDefaultContext(target);
			context.put("project", getProject());

			Object result = Ognl.getValue(expression, context, target);

			if (result != null) {
				getProject().setUserProperty(property, result.toString());
			}
		} catch (OgnlException e) {
			throw new BuildException("Invalid OGNL expresson [" + expression
					+ "] on object[" + target + "]", e);
		}
	}

}

