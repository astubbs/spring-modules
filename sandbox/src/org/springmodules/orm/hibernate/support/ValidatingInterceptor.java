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
package org.springmodules.orm.hibernate.support;

import java.io.Serializable;
import java.util.Iterator;

import net.sf.hibernate.CallbackException;
import net.sf.hibernate.Interceptor;
import net.sf.hibernate.type.Type;

import org.springmodules.orm.support.validation.ValidatingSupport;

/**
 * <p>Hibernate 2 interceptor that implements the persistent object life-cycle event validation pattern (POLEV). 
 * The semantics of this interceptor are identical to the peer Hibernate 3 interceptor. 
 * 
 * @author Steven Devijver
 * @since Jun 17, 2005
 * @see org.springmodules.orm.hibernate3.support.ValidatingInterceptor
 */
public class ValidatingInterceptor implements Interceptor {

	private static final String ON_FLUSH_DIRTY_EVENT = "onFlushDirty";
	private static final String ON_SAVE_EVENT = "onSave";
	private static final String ON_DELETE_EVENT = "onDelete";
	
	public ValidatingInterceptor() {
		super();
	}
	
	public boolean onLoad(Object entity, Serializable id, Object[] state,
			String[] propertyNames, Type[] types) throws CallbackException {
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

	public Boolean isUnsaved(Object arg0) {
		return null;
	}

	public int[] findDirty(Object arg0, Serializable arg1, Object[] arg2,
			Object[] arg3, String[] arg4, Type[] arg5) {
		return null;
	}

	public Object instantiate(Class arg0, Serializable arg1)
			throws CallbackException {
		return null;
	}

}
