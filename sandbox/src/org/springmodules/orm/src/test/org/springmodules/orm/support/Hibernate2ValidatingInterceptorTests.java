/*
 * Copyright 2004-2005 the original author or authors.
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
package org.springmodules.orm.support;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;
import org.springmodules.orm.support.dao.UserDao;

/**
 * 
 * 
 * @author Steven Devijver
 * @since Jun 23, 2005
 */
public class Hibernate2ValidatingInterceptorTests extends
		AbstractValidatingInterceptorTests {

	protected AbstractPlatformTransactionManager hibernate2TransactionManager = null;
	protected UserDao hibernate2UserDao = null;
	
	public Hibernate2ValidatingInterceptorTests() {
		super();
	}

	protected PlatformTransactionManager getPlatformTransactionManager() {
		return this.hibernate2TransactionManager;
	}

	protected UserDao getUserDao() {
		return this.hibernate2UserDao;
	}

}
