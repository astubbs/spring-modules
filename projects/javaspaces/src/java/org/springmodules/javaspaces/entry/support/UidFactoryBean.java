/**
 * 
 */
package org.springmodules.javaspaces.entry.support;

import java.io.Serializable;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springmodules.javaspaces.entry.UidFactory;

/**
 * Wrapper class for Spring application context around uidFactory interface.
 * 
 * @author Costin Leau
 * 
 */
public class UidFactoryBean implements InitializingBean, FactoryBean {

	private UidFactory uidFactory;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springmodules.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		if (uidFactory == null)
			throw new IllegalArgumentException("uidFactory is required");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springmodules.beans.factory.FactoryBean#getObject()
	 */
	public Object getObject() throws Exception {
		return uidFactory.generateUid();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springmodules.beans.factory.FactoryBean#getObjectType()
	 */
	public Class getObjectType() {
		return Serializable.class;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springmodules.beans.factory.FactoryBean#isSingleton()
	 */
	public boolean isSingleton() {
		return true;
	}

	/**
	 * @return Returns the uidFactory.
	 */
	public UidFactory getUidFactory() {
		return uidFactory;
	}

	/**
	 * @param uidFactory
	 *            The uidFactory to set.
	 */
	public void setUidFactory(UidFactory uidFactory) {
		this.uidFactory = uidFactory;
	}

}
