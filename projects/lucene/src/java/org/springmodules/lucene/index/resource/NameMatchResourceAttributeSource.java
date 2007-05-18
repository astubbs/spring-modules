/*
 * Copyright 2002-2007 the original author or authors.
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

package org.springmodules.lucene.index.resource;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.PatternMatchUtils;

/**
 * @author Thierry Templier
 */
public class NameMatchResourceAttributeSource implements ResourceAttributeSource {
	/**
	 * Logger available to subclasses.
	 * <p>Static for optimal serialization.
	 */
	protected static final Log logger = LogFactory.getLog(NameMatchResourceAttributeSource.class);

	private Map resourceAttributes = new HashMap();

	public void setProperties(Properties resourceAttributes) {
		ResourceAttributeEditor rae = new ResourceAttributeEditor();
		for(Iterator i = resourceAttributes.keySet().iterator(); i.hasNext(); ) {
			String methodName = (String)i.next();
			String value = resourceAttributes.getProperty(methodName);
			rae.setAsText(value);
			ResourceAttribute attr = (ResourceAttribute) rae.getValue();
			addResourceMethod(methodName, attr);
		}
	}

	public void setResourceAttributes(Map attributes) {
		for(Iterator i = attributes.keySet().iterator(); i.hasNext(); ) {
			String methodName = (String)i.next();
			ResourceAttribute attr = (ResourceAttribute)attributes.get(methodName);
			addResourceMethod(methodName, attr);
		}
	}

	public Map getResourceAttributes() {
		return this.resourceAttributes;
	}

	public void addResourceMethod(String methodName, ResourceAttribute attr) {
		if (logger.isDebugEnabled()) {
			logger.debug("Adding resource method [" + methodName + "] with attribute [" + attr + "]");
		}
		this.resourceAttributes.put(methodName, attr);
	}

	public ResourceAttribute getResourceAttribute(Method method, Class targetClass) {
		// look for direct name match
		String methodName = method.getName();
		ResourceAttribute attr = (ResourceAttribute) this.resourceAttributes.get(methodName);

		if (attr == null) {
			// Look for most specific name match.
			String bestNameMatch = null;
			for (Iterator it = this.resourceAttributes.keySet().iterator(); it.hasNext();) {
				String mappedName = (String) it.next();
				if (isMatch(methodName, mappedName) &&
						(bestNameMatch == null || bestNameMatch.length() <= mappedName.length())) {
					attr = (ResourceAttribute) this.resourceAttributes.get(mappedName);
					bestNameMatch = mappedName;
				}
			}
		}

		return attr;
	}

	/**
	 * Return if the given method name matches the mapped name.
	 * <p>The default implementation checks for "xxx*", "*xxx" and "*xxx*" matches,
	 * as well as direct equality. Can be overridden in subclasses.
	 * @param methodName the method name of the class
	 * @param mappedName the name in the descriptor
	 * @return if the names match
	 * @see org.springframework.util.PatternMatchUtils#simpleMatch(String, String)
	 */
	protected boolean isMatch(String methodName, String mappedName) {
		return PatternMatchUtils.simpleMatch(mappedName, methodName);
	}


}
