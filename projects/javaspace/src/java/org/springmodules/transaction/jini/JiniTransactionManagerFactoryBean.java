/**
 * Created on Mar 14, 2006
 *
 * $Id$
 * $Revision$
 */
package org.springmodules.transaction.jini;

import net.jini.core.transaction.server.TransactionManager;

import org.springmodules.jini.JiniServiceFactoryBean;

/**
 * Generic FactoryBean for retrieving a TransactionManager from the JINI environment. It uses 
 * JiniServiceFactoryBean internally, which should be used in case more options are 
 * required. 
 * 
 * @see org.springmodules.jini.JiniServiceFactoryBean
 * @author Costin Leau
 *
 */
public class JiniTransactionManagerFactoryBean extends AbstractTransactionManagerFactoryBean {

	private String transactionManagerName;

	/**
	 * @see org.springmodules.transaction.jini.AbstractTransactionManagerFactoryBean#createTransactionManager()
	 */
	protected TransactionManager createTransactionManager() throws Exception {
		JiniServiceFactoryBean serviceFactory = new JiniServiceFactoryBean();
		serviceFactory.setServiceClass(TransactionManager.class);
		serviceFactory.setServiceName(getTransactionManagerName());
		serviceFactory.afterPropertiesSet();
		
		TransactionManager txManager = (TransactionManager) serviceFactory.getObject();
		return txManager;
	}

	/**
	 * @return Returns the transactionManagerName.
	 */
	public String getTransactionManagerName() {
		return transactionManagerName;
	}

	/**
	 * @param transactionManagerName The transactionManagerName to set.
	 */
	public void setTransactionManagerName(String transactionManagerName) {
		this.transactionManagerName = transactionManagerName;
	}
}
