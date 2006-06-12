/*
* Copyright 2006 GigaSpaces, Inc.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
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
