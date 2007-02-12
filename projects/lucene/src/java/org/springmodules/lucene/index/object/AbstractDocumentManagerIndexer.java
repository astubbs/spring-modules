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

package org.springmodules.lucene.index.object;

import org.springmodules.lucene.index.document.handler.DocumentHandler;
import org.springmodules.lucene.index.document.handler.DocumentHandlerManager;
import org.springmodules.lucene.index.document.handler.DocumentMatching;
import org.springmodules.lucene.index.document.handler.file.ExtensionDocumentHandlerManager;

/**
 * This is the base abstract class for every indexers. It is used
 * to specify the IndexFactory to use.
 * 
 * @author Thierry Templier
 */
public abstract class AbstractDocumentManagerIndexer extends AbstractIndexer {

	private DocumentHandlerManager documentHandlerManager;

	protected void init(DocumentHandlerManager documentHandlerManager) {
		if( documentHandlerManager==null ) {
			this.documentHandlerManager=new ExtensionDocumentHandlerManager();
			this.documentHandlerManager.registerDefaultHandlers();
		} else {
			this.documentHandlerManager=documentHandlerManager;
		}
	}

	/**
	 * This method is used to register a document handler. It is associated to
	 * a DocumentMatching instance to determine when this handler must be used.
	 * As the indexer works on directories and files, it is used to determine
	 * which document handler will be used to index a file.
	 * 
	 * @param matching the DocumentMatching associated with the handler
	 * @param handler the handler to register
	 */
	public void registerDocumentHandler(DocumentMatching matching, DocumentHandler handler) {
		documentHandlerManager.registerDocumentHandler(matching,handler);
	}

	/**
	 * This method is used to unregister the document handler corresponding the
	 * mapping.
	 * 
	 * @param matching the DocumentMatching associated with the handler
	 */ 
	public void unregisterDocumentHandler(DocumentMatching matching) {
		documentHandlerManager.unregisterDocumentHandler(matching);
	}

	/**
	 * This method is used to get the DocumentHandler implementation
	 * corresponding to the fileName passed as parameter.
	 * 
	 * @param fileName the name of the file to index
	 * @return the handler to index the file
	 */
	public DocumentHandler getDocumentHandler(String fileName) {
		return documentHandlerManager.getDocumentHandler(fileName);
	}

}
