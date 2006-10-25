package org.springmodules.ant.type;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.DataType;
import org.springframework.beans.factory.BeanFactory;
import org.springmodules.ant.util.BeanFactoryLoader;

/**
 * Custom ant type that can be used to expose a Spring bean as a project
 * reference.  The bean with the given name is exposed as a reference with the
 * given id.  It can then be referred to in a script.
 * 
 * @author Dave Syer
 */
public class SpringBean extends DataType {

	private String contextRef = BeanFactoryLoader.DEFAULT_CONTEXT_REF;

	private String factoryKey = "beanFactory";

	private String name;
	
	private Object value;

	/**
	 * Setter for the bean name property (must be a valid bean)
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

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
	 * Locate the bean in the supplied factory if possible.
	 * @return the bean identified by the parameters.
	 * @throws BuildException if the bean cannot be located
	 */
	public Object getValue() throws BuildException {
		if (value!=null) return value;
		BeanFactory beanFactory = BeanFactoryLoader.getBeanFactory(contextRef, factoryKey);
		if ((name == null) || !beanFactory.containsBean(name)) {
			throw new BuildException(
					"The BeanFactory does not contain the required bean: ["
							+ name + "]");
		}
		value = beanFactory.getBean(name);
		return value;
	}
	
	/**
	 * If the value can be located return its toString() decorated to show that it
	 * is a Spring bean.  Otherwise try to return something sensible.
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		try {
			Object value = getValue(); // We know it's not null
			return "SpringBean:[value="+value.toString()+"]";
		} catch (BuildException e) {
			return "SpringBean:[value undefined]";
		}
	}
	
}
