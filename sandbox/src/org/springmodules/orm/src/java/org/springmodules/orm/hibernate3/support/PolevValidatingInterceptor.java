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
package org.springmodules.orm.hibernate3.support;

import java.io.Serializable;
import java.util.Iterator;

import org.hibernate.CallbackException;
import org.hibernate.EntityMode;
import org.hibernate.Interceptor;
import org.hibernate.Transaction;
import org.hibernate.type.Type;
import org.springmodules.orm.support.validation.ValidatingSupport;

/**
 * <p>Hibernate 3 interceptor that implements the persistent object life-cycle event validation (POLEV) pattern on three life-cyle events
 * within the Hibernate session:
 * 
 * <ul>
 * 		<li>onFlushDirty - occurs before a domain object is flushed to the database. This should be considered as an update of an existing object.
 * 		<li>onSave - occurs before a domain object is saved to the database. This should be considered as an insertion of a new object.
 * 		<li>onDelete - occurs before a domain object is removed from the database.
 * </ul>
 *
 * <p>Please note the onLoad life-cyle event is not supported since persistent objects are not yet initialized when this event occurs.
 *  
 * <p>Making validation of domain classes configurable is the main motivation for this interceptor. Especially for the onFlushDirty event this pattern is
 * the only way to validate domain objects before they are saved. In general this pattern shift domain object validation away from business components.
 * 
 * 
 * @author Steven Devijver
 * @since Jun 16, 2005
 * @see org.springmodules.orm.support.validation.ValidatingSupport
 */
public class PolevValidatingInterceptor implements Interceptor {
	
	private static final String ON_LOAD_EVENT = "onLoad";
	private static final String ON_FLUSH_DIRTY_EVENT = "onFlushDirty";
	private static final String ON_SAVE_EVENT = "onSave";
	private static final String ON_DELETE_EVENT = "onDelete";
	
	public PolevValidatingInterceptor() {
		super();
	}

	public boolean onLoad(Object entity, Serializable id, Object[] state,
			String[] propertyNames, Type[] types) throws CallbackException {
		ValidatingSupport.validate(entity, ON_LOAD_EVENT);
		return false;
	}

	public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState,
			Object[] previousState, String[] propertyNames, Type[] types) throws CallbackException {
		ValidatingSupport.validate(entity, ON_FLUSH_DIRTY_EVENT);
		return false;
	}

	public boolean onSave(Object entity, Serializable id, Object[] state,
			String[] propertyNames, Type[] type) throws CallbackException {
		ValidatingSupport.validate(entity, ON_SAVE_EVENT);
		return false;
	}

	public void onDelete(Object entity, Serializable id, Object[] state,
			String[] propertyNames, Type[] type) throws CallbackException {
		ValidatingSupport.validate(entity, ON_DELETE_EVENT);
	}

	public void preFlush(Iterator arg0) throws CallbackException {

	}

	public void postFlush(Iterator arg0) throws CallbackException {

	}

	public Boolean isTransient(Object arg0) {
		return null;
	}

	public int[] findDirty(Object arg0, Serializable arg1, Object[] arg2,
			Object[] arg3, String[] arg4, Type[] arg5) {
		return null;
	}

	public Object instantiate(String arg0, EntityMode arg1, Serializable arg2)
			throws CallbackException {
		return null;
	}

	public String getEntityName(Object arg0) throws CallbackException {
		return null;
	}

	public Object getEntity(String arg0, Serializable arg1)
			throws CallbackException {
		return null;
	}

	public void afterTransactionBegin(Transaction arg0) {

	}

	public void beforeTransactionCompletion(Transaction arg0) {

	}

	public void afterTransactionCompletion(Transaction arg0) {

	}

}
