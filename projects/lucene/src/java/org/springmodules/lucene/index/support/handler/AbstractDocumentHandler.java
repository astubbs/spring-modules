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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.document.Document;
import org.springmodules.lucene.index.DocumentHandlerException;

/**
 * 
 * @author Thierry Templier
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
