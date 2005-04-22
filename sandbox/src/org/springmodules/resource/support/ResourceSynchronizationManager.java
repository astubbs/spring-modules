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

package org.springmodules.resource.support;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.core.OrderComparator;

/**
 * 
 * @author Juergen Hoeller
 * @author Thierry Templier
 */
public abstract class ResourceSynchronizationManager {

	private static final Log logger = LogFactory.getLog(ResourceSynchronizationManager.class);

	private static final ThreadLocal resources = new ThreadLocal();

	private static final ThreadLocal synchronizations = new ThreadLocal();

	private static final Comparator synchronizationComparator = new OrderComparator();

	private static final ThreadLocal currentTransactionReadOnly = new ThreadLocal();

	private static final ThreadLocal synchronizationActive = new ThreadLocal();

	//-------------------------------------------------------------------------
	// Management of resource-associated resource handles
	//-------------------------------------------------------------------------

	/**
	 * Return all resources that are bound to the current thread.
	 * <p>Mainly for debugging purposes. Resource managers should always invoke
	 * hasResource for a specific resource key that they are interested in.
	 * @return Map with resource keys and resource objects,
	 * or empty Map if currently none bound
	 * @see #hasResource
	 */
	public static Map getResourceMap() {
		Map map = (Map) resources.get();
		if (map == null) {
			map = new HashMap();
		}
		return Collections.unmodifiableMap(map);
	}

	/**
	 * Check if there is a resource for the given key bound to the current thread.
	 * @param key key to check
	 * @return if there is a value bound to the current thread
	 */
	public static boolean hasResource(Object key) {
		Map map = (Map) resources.get();
		return (map != null && map.containsKey(key));
	}

	/**
	 * Retrieve a resource for the given key that is bound to the current thread.
	 * @param key key to check
	 * @return a value bound to the current thread, or null if none
	 */
	public static Object getResource(Object key) {
		Map map = (Map) resources.get();
		if (map == null) {
			return null;
		}
		Object value = map.get(key);
		if (value != null && logger.isDebugEnabled()) {
			logger.debug("Retrieved value [" + value + "] for key [" + key + "] bound to thread [" +
					Thread.currentThread().getName() + "]");
		}
		return value;
	}

	/**
	 * Bind the given resource for the given key to the current thread.
	 * @param key key to bind the value to
	 * @param value value to bind
	 * @throws IllegalStateException if there is already a value bound to the thread
	 */
	public static void bindResource(Object key, Object value) throws IllegalStateException {
		Map map = (Map) resources.get();
		// set ThreadLocal Map if none found
		if (map == null) {
			map = new HashMap();
			resources.set(map);
		}
		if (map.containsKey(key)) {
			throw new IllegalStateException("Already value [" + map.get(key) + "] for key [" + key +
					"] bound to thread [" + Thread.currentThread().getName() + "]");
		}
		map.put(key, value);
		if (logger.isDebugEnabled()) {
			logger.debug("Bound value [" + value + "] for key [" + key + "] to thread [" +
					Thread.currentThread().getName() + "]");
		}
	}

	/**
	 * Unbind a resource for the given key from the current thread.
	 * @param key key to check
	 * @return the previously bound value
	 * @throws IllegalStateException if there is no value bound to the thread
	 */
	public static Object unbindResource(Object key) throws IllegalStateException {
		Map map = (Map) resources.get();
		if (map == null || !map.containsKey(key)) {
			throw new IllegalStateException(
					"No value for key [" + key + "] bound to thread [" + Thread.currentThread().getName() + "]");
		}
		Object value = map.remove(key);
		// remove entire ThreadLocal if empty
		if (map.isEmpty()) {
			resources.set(null);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Removed value [" + value + "] for key [" + key + "] from thread [" +
					Thread.currentThread().getName() + "]");
		}
		return value;
	}


	//-------------------------------------------------------------------------
	// Management of resource synchronizations
	//-------------------------------------------------------------------------

	/**
	 * Return if transaction synchronization is active for the current thread.
	 * Can be called before register to avoid unnecessary instance creation.
	 * @see #registerSynchronization
	 */
	public static boolean isSynchronizationActive() {
		return (synchronizationActive.get() != null);
	}

	public static void setSynchronizationActive(boolean _synchronizationActive) {
		if( _synchronizationActive ) {
			synchronizationActive.set(Boolean.TRUE);
		} else {
			synchronizationActive.set(null);
		}
	}

	/**
	 * Activate transaction synchronization for the current thread.
	 * Called by a transaction manager on transaction begin.
	 * @throws IllegalStateException if synchronization is already active
	 */
	public static void initSynchronization() throws IllegalStateException {
		if (isSynchronizationActive()) {
			throw new IllegalStateException("Cannot activate transaction synchronization - already active");
		}
		logger.debug("Initializing transaction synchronization");
		synchronizations.set(new LinkedList());
		setSynchronizationActive(true);
	}

	/**
	 * Register a new transaction synchronization for the current thread.
	 * Typically called by resource management code.
	 * <p>Note that synchronizations can implemented the Ordered interface.
	 * They will be executed in an order according to their order value (if any).
	 * @throws IllegalStateException if synchronization is not active
	 * @see org.springframework.core.Ordered
	 */
	public static void registerSynchronization(ResourceSynchronization synchronization)
	    throws IllegalStateException {
		if (!isSynchronizationActive()) {
			throw new IllegalStateException("Transaction synchronization is not active");
		}
		System.err.println("### registerSynchronization ###");
		List synchs = (List) synchronizations.get();
		synchs.add(synchronization);
		Collections.sort(synchs, synchronizationComparator);
	}

	/**
	 * Return an unmodifiable snapshot list of all registered synchronizations
	 * for the current thread.
	 * @return unmodifiable List of TransactionSynchronization instances
	 * @throws IllegalStateException if synchronization is not active
	 * @see TransactionSynchronization
	 */
	public static List getSynchronizations() throws IllegalStateException {
		if (!isSynchronizationActive()) {
			throw new IllegalStateException("Transaction synchronization is not active");
		}
		List synchs = (List) synchronizations.get();
		return Collections.unmodifiableList(new LinkedList(synchs));
	}

	/**
	 * Deactivate transaction synchronization for the current thread.
	 * Called by transaction manager on transaction cleanup.
	 * @throws IllegalStateException if synchronization is not active
	 */
	public static void clearSynchronization() throws IllegalStateException {
		if (!isSynchronizationActive()) {
			throw new IllegalStateException("Cannot deactivate transaction synchronization - not active");
		}
		logger.debug("Clearing transaction synchronization");
		synchronizations.set(null);
	}

}
