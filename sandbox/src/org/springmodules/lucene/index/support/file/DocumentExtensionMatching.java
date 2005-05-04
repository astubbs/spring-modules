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

package org.springmodules.lucene.index.support.file;

/**
 * @author Thierry Templier
 */
public class DocumentExtensionMatching extends AbstractionDocumentMatching implements DocumentMatching {

	private String extension;

	public DocumentExtensionMatching(String extension) {
		this.extension=extension;
	}

	/**
	 * @see org.springmodules.lucene.index.object.DocumentMatching#match(java.lang.String)
	 */
	public boolean match(String name) {
		int index=-1;
		if( (index=name.lastIndexOf("."))!=-1 ) {
			return matchExtension(name.substring(index+1));
		} else {
			return false;
		}
	}

	public boolean matchExtension(String extension) {
		if( extension.equals(this.extension) ) {
			return true;
		} else {
			return false;
		}
	}

}
