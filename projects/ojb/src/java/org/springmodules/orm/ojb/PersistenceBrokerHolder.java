/*
 * Copyright 2002-2005 the original author or authors.
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

package org.springmodules.orm.ojb;

import org.apache.ojb.broker.PersistenceBroker;

import org.springframework.transaction.support.ResourceHolderSupport;
import org.springframework.util.Assert;

/**
 * Holder wrapping an OJB PersistenceBroker.
 * PersistenceBrokerTransactionManager binds instances of this class
 * to the thread, for a given PBKey.
 *
 * <p>Note: This is an SPI class, not intended to be used by applications.
 *
 * @author Juergen Hoeller
 * @since 1.1
 * @see PersistenceBrokerTransactionManager
 * @see OjbFactoryUtils
 */
public class PersistenceBrokerHolder extends ResourceHolderSupport {

	private final PersistenceBroker persistenceBroker;


	/**
	 * Create a new PersistenceBrokerHolder for the given OJB PersistenceBroker.
	 * @param persistenceBroker the OJB PersistenceBroker
	 */
	public PersistenceBrokerHolder(PersistenceBroker persistenceBroker) {
		Assert.notNull(persistenceBroker, "PersistenceBroker must not be null");
		this.persistenceBroker = persistenceBroker;
	}

	/**
	 * Return this holder's OJB PersistenceBroker.
	 */
	public PersistenceBroker getPersistenceBroker() {
		return persistenceBroker;
	}

}
