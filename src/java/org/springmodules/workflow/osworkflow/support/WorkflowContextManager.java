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
package org.springmodules.workflow.osworkflow.support;

import java.util.HashMap;
import java.util.Map;
import java.util.Collections;

/**
 * @author Rob Harrop
 */
public abstract class WorkflowContextManager {

	private static final ThreadLocal resources = new ThreadLocal() {
		protected Object initialValue() {
			return new HashMap();
		}
	};

	public static Object getResource(Object key) {
		Map map = (Map) resources.get();
		return map.get(key);
	}

	public static boolean hasResource(Object key) {
		return ((Map) resources.get()).containsKey(key);
	}

	public static void bindResource(Object key, Object value) {
		Map map = (Map) resources.get();
		map.put(key, value);
	}

	public static void unbindResource(Object key) {
		Map map = (Map) resources.get();
		map.remove(key);
	}

	public static Map getResourcesAsMap() {
		return Collections.unmodifiableMap((Map) resources.get());
	}

	public static void clearContext() {
		((Map) resources.get()).clear();
	}
}
