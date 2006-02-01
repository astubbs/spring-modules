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

import java.util.Map;

import org.apache.lucene.document.Document;

/**
 * This is the base interface to define different implementations
 * in order to index different objects or ressources.
 * 
 * @author Thierry Templier
 * @see org.springmodules.lucene.index.support.handler.AbstractDocumentHandler
 * @see org.springmodules.lucene.index.support.handler.file.TextDocumentHandler
 * @see org.springmodules.lucene.index.support.handler.object.ReflectiveDocumentHandler
 */
public interface DocumentHandler {

	/**
	 * Return whether or not this object can create a document from an
	 * instance of the given class.
	 */
	boolean supports(Class clazz);
	
	/**
	 * This method indexes an object and specifies some additional
	 * properties on the Lucene document basing the description parameter.
	 * 
	 * The object to index can be either a POJO or a stream on a resource.
	 * 
	 * @param description the description of the resource to index
	 * @param inputStream the input stream which will be used to index
	 */
	Document getDocument(Map description,Object object) throws Exception;
}
