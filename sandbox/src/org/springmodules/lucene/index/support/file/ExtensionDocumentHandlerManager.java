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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.springmodules.lucene.index.support.file.handler.TextDocumentHandler;
import org.springmodules.samples.lucene.index.file.handlers.AbstractDocumentHandler;

/**
 * This class is the document handler manager which is based
 * on the file extensions.
 * 
 * <p>It only adds the text document handler as default handlers.
 * 
 * @author Thierry Templier
 */
public class ExtensionDocumentHandlerManager extends DefaultDocumentHandlerManager implements DocumentHandlerManager {

	/**
	 * Construct a new ExtensionDocumentHandlerManager.
	 */
	public ExtensionDocumentHandlerManager() {
		super();
	}

	/**
	 * This method adds the text document handler as default handlers.
	 * @see org.springmodules.lucene.index.object.file.DocumentHandlerManager#registerDefautHandlers()
	 */
	public void registerDefautHandlers() {
		//Register a default handler for text file (.txt)
		registerDocumentHandler(new DocumentExtensionMatching("txt"),new TextDocumentHandler());
	}

}
