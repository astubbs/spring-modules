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

package org.springmodules.lucene.index.support.handler;

/**
 * This is the base interface to determine when a document
 * handler must be used to index a document.
 * 
 * @author Thierry Templier
 */
public interface DocumentMatching {

	/**
	 * This method determines if the name parameter matches
	 * according to a mechanism specified in an implementation
	 * of this interface.
	 * @param name the name to test
	 * @return if the name matches
	 */
	public boolean match(String name);
}
