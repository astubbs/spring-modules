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

package org.springmodules.lucene.index.document.handler.object;

/**
 * Interface which represents the contract of an index attribute
 * and allows to get the following informations: name, type and
 * exclusion of the attribute.
 * 
 * @author Thierry Templier
 */
public interface IndexAttribute {

	/**
	 * Returns the name of the attribute.
	 * 
	 * @return the name of the attribute
	 */
	String getName();
	
	/**
	 * Returns the type of the attribute.
	 * 
	 * @return the type of the attribute
	 */
	String getType();
	
	/**
	 * Returns if the attribute must be excluded of the indexing.
	 * 
	 * @return true if the attribute must be excluded
	 */
	boolean isExcluded();
}
