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

import java.sql.SQLException;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerException;
import org.apache.ojb.broker.accesslayer.LookupException;

/**
 * Callback interface for OJB PersistenceBroker code.
 * To be used with PersistenceBrokerTemplate's execute method,
 * assumably often as anonymous classes within a method implementation.
 *
 * <p>The typical implementation will call PersistenceBroker CRUD to
 * perform some operations on persistent objects.
 *
 * @author Juergen Hoeller
 * @since 1.1
 * @see PersistenceBrokerTemplate
 */
public interface PersistenceBrokerCallback {

	/**
	 * Gets called by <code>PersistenceBrokerTemplate.execute</code> with an active
	 * PersistenceBroker. Does not need to care about activating or closing the
	 * PersistenceBroker, or handling transactions.
	 *
	 * <p>Allows for returning a result object created within the callback,
	 * i.e. a domain object or a collection of domain objects.
	 * A thrown RuntimeException is treated as application exception,
	 * it gets propagated to the caller of the template.
	 *
	 * @param pb active PersistenceBroker
	 * @return a result object, or <code>null</code> if none
	 * @throws PersistenceBrokerException in case of OJB errors
	 * @throws LookupException if thrown by OJB lookup methods
	 * @throws SQLException in case of errors on direct JDBC access
	 * @see PersistenceBrokerTemplate#execute
	 */
	Object doInPersistenceBroker(PersistenceBroker pb)
	    throws PersistenceBrokerException, LookupException, SQLException;

}
