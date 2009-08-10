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

package org.springmodules.lucene.index.document.handler;

/**
 * This implementation of the DocumentMatching interface determines
 * when a document handler must be used to index a document thanks
 * to a regular expression.
 * 
 * @author Thierry Templier
 */
public abstract class AbstractRegexpDocumentMatching implements DocumentMatching {

	/**
	 * Construct a new RegexpDocumentMatching with the regular
	 * expression to match.
	 */
	public AbstractRegexpDocumentMatching(String regularExpression) {
		initRegExpr(regularExpression);
	}

	/**
	 * This method can be overloaded to init a regular expression
	 * according to its pattern.
	 *  
	 * @param regularExpression the regular expression
	 */
	protected void initRegExpr(String regularExpression) {
	}

	/**
	 * This method delegates to regexp  the extension of the name parameter
	 * which must be a filename and determines if it matches with
	 * the internal extension property of the instance.
	 * 
	 * @param name the name to check
	 * @return if the check to the regular expression
	 * @see org.springmodules.lucene.index.object.DocumentMatching#match(java.lang.String)
	 */
	public boolean match(String name) {
		return matchRegularExpression(name);
	}

	/**
	 * The method to implement using different strategies.
	 * 
	 * @param name the name to check
	 * @return if the check to the regular expression
	 */
	protected abstract boolean matchRegularExpression(String name);

}
