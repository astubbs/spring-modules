
package org.springmodules.jsr94.factory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

public abstract class AbstractInitializingFactoryBean implements FactoryBean, InitializingBean {

	/**
		 * Logger interface for the factory bean
		 */
	protected final Log logger = LogFactory.getLog(AbstractInitializingFactoryBean.class);

	/**
	 * Set to true to disallow singleton = false settings
	 */
	protected boolean mustBeSingleton = false;

	private boolean singleton = true;

	private Object singletonInstance;

	/**
		 * Implementations must override this method to create an instance of the target object
		 *
		 * @return The target object instance
		 * @throws Exception If the target cannot be instantiated
		 */
	protected abstract Object createInstance() throws Exception;

	/**
		 * Implementations can override this method to perform additional initialization. This method
		 * is called as first in afterPropertiesSet()
		 *
		 * @throws Exception If the initialization fails
		 */
	protected void preInitialize() throws Exception {
		// noop
	}

	/**
		 * Implementations can override this method to perform additional initialization. This method
		 * is called as last in afterPropertiesSet()
		 *
		 * @throws Exception If the initialization fails
		 */
	protected void postInitialize() throws Exception {
		// noop
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public final void afterPropertiesSet() throws Exception {
		preInitialize();

		if (singleton) {
			singletonInstance = createInstance();
		}

		postInitialize();
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#getObject()
	 */
	public final Object getObject() throws Exception {
		if (singleton) {
			return singletonInstance;
		}
		else {
			return createInstance();
		}
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#isSingleton()
	 */
	public final boolean isSingleton() {
		return singleton;
	}

	/**
	 * @param singleton The singleton to set.
	 */
	public final void setSingleton(boolean singleton) {
		if (mustBeSingleton && !singleton) {
			logger.warn("Ignoring singleton = false");
			this.singleton = true;
		}
		else {
			this.singleton = singleton;
		}
	}


}
