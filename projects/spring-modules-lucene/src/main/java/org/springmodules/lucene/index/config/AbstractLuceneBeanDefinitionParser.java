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

package org.springmodules.lucene.index.config;

import org.w3c.dom.Element;

/**
 * Abstract root class for all BeanDefinitionParser implemented
 * in order to configure indexing.
 * 
 * @author Thierry Templier
 */
public abstract class AbstractLuceneBeanDefinitionParser {

	/**
	 * Determines if an attribute of a tag is set basing on its name.
	 * 
	 * @param element the element corresponding to the tag
	 * @param name the name of the attribute
	 * @return if the attribute is set
	 */
	protected boolean hasAttributeSet(Element element, String name) {
		String value = element.getAttribute(name);
		
		if( value!=null && !"".equals(value) ) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Determines if an attribute of a tag has a specified value basing on its name.
	 * 
	 * @param element the element corresponding to the tag
	 * @param name the name of the attribute
	 * @param value the value to check
	 * @return if the attribute has the specified value
	 */
	protected boolean hasAttributeValue(Element element, String name, String value) {
		String currentValue = element.getAttribute(name);
		
		if( currentValue!=null && currentValue.equals(value) ) {
			return true;
		} else {
			return false;
		}
	}
	
}
