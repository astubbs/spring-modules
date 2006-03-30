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

import javax.sql.DataSource;

import org.springframework.test.AbstractDependencyInjectionSpringContextTests;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springmodules.orm.support.dao.UserDao;
import org.springmodules.orm.support.domain.User;
import org.springmodules.orm.support.validation.ValidationException;

/**
 * 
 * 
 * @author Steven Devijver
 * @since Jun 19, 2005
 */
public abstract class AbstractValidatingInterceptorTests extends
		AbstractDependencyInjectionSpringContextTests {

	protected DataSource dataSource = null;
	private TransactionStatus transactionStatus = null;
	
	public AbstractValidatingInterceptorTests() {
		super();
		setPopulateProtectedVariables(true);
	}

	protected String[] getConfigLocations() {
		return new String[] { "org/springmodules/orm/support/validating-interceptor-tests.xml" };
	}

	protected abstract PlatformTransactionManager getPlatformTransactionManager();
	
	protected abstract UserDao getUserDao();
	
	private void startTx() {
		this.transactionStatus = getPlatformTransactionManager().getTransaction(new DefaultTransactionDefinition());
	}
	
	private void endTx() {
		getPlatformTransactionManager().rollback(this.transactionStatus);
	}
	
	private User getRob() {
		User rob = new User();
		rob.setFirstName("Rob");
		rob.setLastName("Harrop");
		rob.setUsername("robh");
		rob.setPassword("");
		rob.setAdmin(true);
		return rob;
	}
	
	public void testPlatformTransactionManager() {
		try {
			startTx();
		} finally {
			endTx();
		}
	}
	
	public void testFailOnSave() {
		User rob = getRob();
		try {
			getUserDao().addUser(rob);
			fail();
		} catch (ValidationException e) {
			// expected.
		}
	}
	
	public void testSuccesOnSave() {
		try {
			startTx();
			User rob = getRob();
			rob.setPassword("xxx");
			getUserDao().addUser(rob);
		} finally {
			endTx();
		}
	}
	
	public void testFailOnFlushDirty() {
		try {
			startTx();
			User rob = getRob();
			rob.setPassword("xxx");
			getUserDao().addUser(rob);
			rob.setPassword("");
			getUserDao().saveUser(rob);
			fail();
		} catch (ValidationException e) {
			// expected
		} finally {
			endTx();
		}
	}
	
	public void testSuccessOnFlushDirty() {
		try {
			startTx();
			User rob = getRob();
			rob.setPassword("xxx");
			getUserDao().addUser(rob);
			rob.setPassword("xyz");
			getUserDao().saveUser(rob);
		} finally {
			endTx();
		}
	}
	
	public void testFailOnDelete() {
		try {
			startTx();
			User rob = getRob();
			rob.setPassword("xxx");
			getUserDao().addUser(rob);
			rob.setPassword("xyz");
			getUserDao().saveUser(rob);
			getUserDao().removeUser(rob);
			fail();
		} catch (ValidationException e) {
			// expected
		} finally {
			endTx();
		}		
	}
	
	public void testSuccessOnDelete() {
		try {
			startTx();
			User rob = getRob();
			rob.setPassword("xxx");
			getUserDao().addUser(rob);
			rob.setAdmin(false);
			getUserDao().removeUser(rob);
		} finally {
			endTx();
		}
	}
}
