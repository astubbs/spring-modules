/*
 * Copyright 2006 GigaSpaces Technologies. All rights reserved.
 *
 * THIS SOFTWARE IS PROVIDED "AS IS," WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED INCLUDING BUT NOT LIMITED TO WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMENT. GIGASPACES WILL NOT BE LIABLE FOR ANY DAMAGE OR
 * LOSS IN CONNECTION WITH THE SOFTWARE.
 */
package org.springmodules.javaspaces.gigaspaces.transaction;

import net.jini.core.transaction.server.TransactionManager;
import net.jini.space.JavaSpace;

import org.springframework.aop.framework.Advised;
import org.springmodules.transaction.jini.AbstractTransactionManagerFactoryBean;

import com.j_spaces.core.IJSpace;
import com.j_spaces.core.client.LocalTransactionManager;

/**
 * GigaSpace LocalTransactionManager FactoryBean.
 *
 * Copyright:    Copyright 2006 GigaSpaces Technologies. All rights reserved.
 * Company:      Gigaspaces Technologies
 * @author       Lior Ben Yizhak
 * @version      5.0
 *
 */
public class GigaSpacesLocalTransactionManagerFactoryBean extends AbstractTransactionManagerFactoryBean {

	private JavaSpace javaSpace;

	/**
	 * @see org.springframework.transaction.jini.AbstractTransactionManagerFactoryBean#createTransactionManager()
	 */
	public TransactionManager createTransactionManager() throws Exception {
		if (javaSpace == null)
			throw new IllegalArgumentException("javaSpace property is required");
		if (!(javaSpace instanceof IJSpace))
			throw new IllegalArgumentException(
					"javaSpace implementation doesn't seem to be GigaSpace (it does not implement "
							+ IJSpace.class.getName());

		while(javaSpace instanceof Advised) {
			javaSpace = (IJSpace)((Advised)javaSpace).getTargetSource().getTarget();
		}
		return  LocalTransactionManager.getInstance((IJSpace) javaSpace);
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
