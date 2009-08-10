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

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.document.Document;
import org.springmodules.lucene.index.DocumentHandlerException;

/**
 * Default base abstract class of the DocumentHandler interface. This class
 * tests if the document handler supports the class of the object used to
 * build a Lucene document.
 * 
 * In order to check this, the method supports of the DocumentHandler interface
 * is used.
 * 
 * To use this abstract, you need to implement a sub class which defines the
 * doGetDocument method.
 * 
 * @author Thierry Templier
 * @see DocumentHandler#supports(Class)
 */
public abstract class AbstractDocumentHandler implements DocumentHandler {

	protected final Log logger = LogFactory.getLog(getClass());

	public final Document getDocument(Map description, Object object)
			throws Exception {
		if (supports(object.getClass())) {
			return doGetDocument(description, object);
		} else {
			throw new DocumentHandlerException(
					"The document handler does not support the class "
							+ object.getClass());
		}
	}

	protected abstract Document doGetDocument(Map description, Object object)
			throws Exception;
}
