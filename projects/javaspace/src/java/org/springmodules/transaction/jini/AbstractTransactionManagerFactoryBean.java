/**
 * Created on Mar 13, 2006
 *
 * $Id$
 * $Revision$
 */
package org.springmodules.transaction.jini;

import net.jini.core.transaction.server.TransactionManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * Base class for creating Jini TransactionManagers.
 * 
 * @see org.springmodules.jini.JiniServiceFactoryBean
 * @author Costin Leau
 *
 */
public abstract class AbstractTransactionManagerFactoryBean implements DisposableBean, FactoryBean,
		InitializingBean {

	protected final Log log = LogFactory.getLog(getClass());

	private TransactionManager tm;
	/**
	 * @see org.springmodules.beans.factory.DisposableBean#destroy()
	 */
	public void destroy() throws Exception {
	}

	/**
	 * @see org.springmodules.beans.factory.FactoryBean#getObject()
	 */
	public Object getObject() throws Exception {
		return tm;
	}

	/**
	 * @see org.springmodules.beans.factory.FactoryBean#getObjectType()
	 */
	public Class getObjectType() {
		return (tm == null ? TransactionManager.class : tm.getClass());
	}

	/**
	 * @see org.springmodules.beans.factory.FactoryBean#isSingleton()
	 */
	public boolean isSingleton() {
		return true;
	}

	/**
	 * @see org.springmodules.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		// create the TM
		tm = createTransactionManager();
	}

	/**
	 * Subclasses need to implement this method to create or lookup the Transaction Manager.
	 * 
	 * @return
	 * @throws Exception
	 */
	protected abstract TransactionManager createTransactionManager() throws Exception;

}
