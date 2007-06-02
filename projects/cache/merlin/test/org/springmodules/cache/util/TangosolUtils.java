/*
 * Created on Jan 26, 2006
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 * Copyright @2007 the original author or authors.
 */
package org.springmodules.cache.util;

import com.tangosol.net.NamedCache;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

/**
 * Utility class for using <a href="http://www.tangosol.com">Tangosol Coherence</a> API.
 *
 * @author <a href="mailto:irbouh@gmail.com">Omar Irbouh</a>
 */
public abstract class TangosolUtils {

	private static final String CACHE_FACTORY_CLASS_NAME = "com.tangosol.net.CacheFactory";

	private static final String GET_CACHE_METHOD_NAME = "getCache";

	private static final String SHUTDOWN_METHOD_NAME = "shutdown";

	private static final boolean tangosolApiPresent = ClassUtils.isPresent(CACHE_FACTORY_CLASS_NAME);

	private static Class cacheFactoryClass;

	private static Method getCacheMethod;

	private static Method shutdownMethod;

	/**
	 * Returns Tangosol CacheFactory class.
	 *
	 * @return CacheFactory class.
	 */
	public static synchronized Class getCacheFactoryClass() {
		if (cacheFactoryClass == null) {
			try {
				cacheFactoryClass = ClassUtils.forName(CACHE_FACTORY_CLASS_NAME);
			} catch (Exception ex) {
				throw new IllegalStateException(
						"Could not initialize CoherenceFacade because Tangosol API classes are not available: " + ex);
			}
		}

		return cacheFactoryClass;
	}

	/**
	 * Returns whether Tangosol CacheFactory class is present in classpath.
	 *
	 * @return <code>true</code> if Tangosol's CacheFactory class is found in classpath.
	 */
	public static boolean isApiPresent() {
		return tangosolApiPresent;
	}

	/**
	 * Returns a Tangosol NamedCache.
	 *
	 * @param name NamedCache name.
	 * @return NamedCache.
	 */
	public static synchronized NamedCache getNamedCache(String name) {
		if (getCacheMethod == null) {
			Class clazz = getCacheFactoryClass();
			getCacheMethod = ReflectionUtils.findMethod(clazz, GET_CACHE_METHOD_NAME, new Class[]{String.class});
		}

		return (NamedCache) ReflectionUtils.invokeMethod(getCacheMethod, null, new String[]{name});
	}

	/**
	 * Shuts down Tangosol CacheFactory.
	 */
	public static synchronized void shutdownCacheFactory() {
		if (cacheFactoryClass != null) {
			if (shutdownMethod == null) {
				shutdownMethod = ReflectionUtils.findMethod(cacheFactoryClass, SHUTDOWN_METHOD_NAME, new Class[0]);
			}
			ReflectionUtils.invokeMethod(shutdownMethod, null);
		}
	}

}
