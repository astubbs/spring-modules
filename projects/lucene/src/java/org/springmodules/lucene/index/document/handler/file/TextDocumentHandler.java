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

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.springmodules.lucene.index.document.handler.DocumentHandler;

/**
 * This class is the default DocumentHandler implementation to
 * index text.
 * 
 * <p>It sets the "contents" document property and this property
 * is not store in the index. Moreover it eventually sets the
 * "type" and "filename" properties if the informations are
 * contained in the description parameter of the getDocument
 * method.
 * 
 * <p>It can be associated with the "txt" file extension and can
 * be used to configure the ExtensionDocumentHandlerManager with
 * the ExtensionDocumentHandlerManagerFactoryBean factory bean.
 *  
 * @author Thierry Templier
 * @see DocumentHandler
 * @see ExtensionDocumentHandlerManagerFactoryBean
 */
public class TextDocumentHandler extends AbstractInputStreamDocumentHandler {

	/**
	 * This method indexes an InputStream and specifies some additional
	 * properties on the Lucene document basing the description parameter.
	 * 
	 * <p>This method indexes the text from the InputStream in the "contents"
	 * property which is not stored in the index.
	 * 
	 * <p>If the FILENAME description property is specified
	 * in the description map, the "type" keyword property is set with the "file"
	 * value and the "filename" keyword property is set with the DocumentHandler.FILENAME
	 * description property.
	 * 
	 * @see org.springmodules.lucene.index.object.FileDocumentHandler#getDocument(java.io.File,java.io.FileReader)
	 */
	protected Document doGetDocumentWithInputStream(Map description,InputStream inputStream) {
		Document document = new Document();
		//The text is analyzed and indexed but not stored
		document.add(new Field("contents", new InputStreamReader(inputStream)));
		if( description.get(FILENAME)!=null ) {
			document.add(new Field("type", "file", Field.Store.YES, Field.Index.UN_TOKENIZED));
			document.add(new Field("filename", (String)description.get(FILENAME), Field.Store.YES, Field.Index.UN_TOKENIZED));
		}
		return document;
	}

}
