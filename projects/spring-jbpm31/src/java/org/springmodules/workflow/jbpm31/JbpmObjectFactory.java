/**
 * Created on Feb 28, 2006
 *
 * $Id: JbpmObjectFactory.java,v 1.1 2006/03/01 16:55:24 costin Exp $
 * $Revision: 1.1 $
 */
package org.springmodules.workflow.jbpm31;

import org.jbpm.configuration.ObjectFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

/**
 * Spring aware ObjectFactory. Allows configuration of jBPM inside Spring application
 * context.
 * 
 * @author Costin Leau
 *
 */
public class JbpmObjectFactory implements ObjectFactory, BeanFactoryAware {

	private BeanFactory beanFactory;

	/**
	 * @see org.jbpm.configuration.ObjectFactory#createObject(java.lang.String)
	 */
	public Object createObject(String name) {
		return beanFactory.getBean(name);
	}

	/**
	 * @see org.jbpm.configuration.ObjectFactory#hasObject(java.lang.String)
	 */
	public boolean hasObject(String name) {
		return beanFactory.containsBean(name);
	}

	/**
	 * @see org.springframework.beans.factory.BeanFactoryAware#setBeanFactory(org.springframework.beans.factory.BeanFactory)
	 */
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

}
