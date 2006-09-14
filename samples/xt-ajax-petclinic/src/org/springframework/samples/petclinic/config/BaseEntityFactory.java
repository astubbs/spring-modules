package org.springframework.samples.petclinic.config;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.samples.petclinic.BaseEntity;

/**
 * @author <a href="mailto:irbouh@gmail.com">Omar Irbouh</a>
 * @since Aug 31, 2006
 */
public abstract class BaseEntityFactory implements FactoryBean {

	protected abstract BaseEntity getEntity();

	public void setId(Integer id) {
		getEntity().setId(id);
	}

	public Object getObject() throws Exception {
		return getEntity();
	}

	public Class getObjectType() {
		return getEntity().getClass();
	}

	public boolean isSingleton() {
		return true;
	}

}