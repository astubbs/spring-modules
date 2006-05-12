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
 * This implementation of the DocumentMatching interface determines
 * when a document handler must be used to index a document thanks
 * to a name.
 * 
 * This class supports the wild card '*' to match to all the elements.
 * 
 * @author Thierry Templier
 */
public class IdentityDocumentMatching implements DocumentMatching {

	private String name;
	
	public final static String WILD_CARD="*";

	/**
	 * Construct a new DocumentExtensionMatching with the name
	 * to match.
	 */
	public IdentityDocumentMatching(String name) {
		this.name=name;
	}

	/**
	 * This method extracts the extension of the name parameter
	 * which must be a filename and determines if it matches with
	 * the internal extension property of the instance.
	 * 
	 * @see org.springmodules.lucene.index.object.DocumentMatching#match(java.lang.String)
	 */
	public boolean match(String name) {
		if( this.name.equals(WILD_CARD) ) {
			return true;
		} else {
			return (this.name.equals(name));
		}
	}

	
	/**
	 * This method returns the name to match.
	 * 
	 * @return the name to match
	 */
	public String getName() {
		return name;
	}

	public boolean equals(Object obj) {
		if( !(obj instanceof IdentityDocumentMatching) ) {
			return false;
		}
		
		IdentityDocumentMatching matching = (IdentityDocumentMatching)obj;
		return name.equals(matching.getName());
	}

	public int hashCode() {
		int result = 17;
		result = 37*result + name.hashCode();
		return result;
	}
}
