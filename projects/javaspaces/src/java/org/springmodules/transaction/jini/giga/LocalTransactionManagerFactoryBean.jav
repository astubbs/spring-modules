/**
 * Created on Mar 13, 2006
 *
 * $Id$
 * $Revision$
 */
package org.springmodules.transaction.jini.giga;

import org.springmodules.transaction.jini.AbstractTransactionManagerFactoryBean;

import net.jini.core.transaction.server.TransactionManager;
import net.jini.space.JavaSpace;

import com.j_spaces.core.IJSpace;
import com.j_spaces.core.client.LocalTransactionManager;

/**
 * GigaSpace LocalTransactionManager FactoryBean.
 * 
 * @author Costin Leau
 *
 */
public class LocalTransactionManagerFactoryBean extends AbstractTransactionManagerFactoryBean {

	private JavaSpace javaSpace;

	/**
	 * @see org.springmodules.transaction.jini.AbstractTransactionManagerFactoryBean#createTransactionManager()
	 */
	public TransactionManager createTransactionManager() throws Exception {
		if (javaSpace == null)
			throw new IllegalArgumentException("javaSpace property is required");
		if (!(javaSpace instanceof IJSpace))
			throw new IllegalArgumentException(
					"javaSpace implementation doesn't seem to be GigaSpace (it does not implement "
							+ IJSpace.class.getName());

		return LocalTransactionManager.getInstance((IJSpace) javaSpace);
	}

	/**
	 * @return Returns the javaSpace.
	 */
	public JavaSpace getJavaSpace() {
		return javaSpace;
	}

	/**
	 * @param javaSpace The javaSpace to set.
	 */
	public void setJavaSpace(JavaSpace javaSpace) {
		this.javaSpace = javaSpace;
	}
}
