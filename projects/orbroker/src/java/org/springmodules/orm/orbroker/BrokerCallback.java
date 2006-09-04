/*
 * Copyright 2002-2006 the original author or authors.
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

package org.springmodules.orm.orbroker;

import net.sourceforge.orbroker.BrokerException;
import net.sourceforge.orbroker.Executable;

/**
 * Callback interface for data access code that works on an O/R Broker Exeutable.
 * To be used with BrokerTemplate's execute method, assumably often as anonymous
 * classes within a method implementation.
 *
 * @author Omar Irbouh
 * @see BrokerTemplate#execute(BrokerCallback)
 * @since 2005.06.02
 */
public interface BrokerCallback {

	/**
	 * Gets called by <code>BrokerTemplate.execute</code> with an active Executable.
	 *
	 * @param executable O/R broker Executable
	 * @return a result object, or <code>null</code> if none
	 * @throws BrokerException if thrown by Executable methods
	 */
	Object doInBroker(Executable executable) throws BrokerException;

}