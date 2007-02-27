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

package org.springmodules.lucene.index.document.handler.file;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.lucene.document.Document;
import org.springmodules.lucene.index.document.handler.AbstractDocumentHandler;

/**
 * Default abstract class of the DocumentHandler interface dedicated to create
 * Lucene document basing on an input stream. This class implements supports
 * method in order to specify that all the sub classes must use an InputStream
 * instance to crerate a Lucene document.
 * 
 * To use this abstract, you need to implement a sub class which defines the
 * doGetDocumentWithInputStream method.
 * 
 * @author Thierry Templier
 *
 */
public abstract class AbstractInputStreamDocumentHandler extends AbstractDocumentHandler {

	public final static String FILENAME="filename";

	public final boolean supports(Class clazz) {
		return (InputStream.class).isAssignableFrom(clazz);
	}

	protected final Document doGetDocument(Map description, Object object) throws Exception {
		return doGetDocumentWithInputStream(description, (InputStream)object);
	}

	protected abstract Document doGetDocumentWithInputStream(Map description, InputStream inputStream) throws IOException;
}
