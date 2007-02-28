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

package org.springmodules.samples.lucene.dao.indexing.handlers;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.pdfbox.searchengine.lucene.LucenePDFDocument;
import org.springmodules.lucene.index.document.handler.file.AbstractInputStreamDocumentHandler;

/**
 * @author Thierry Templier
 */
public class PdfBoxDocumentHandler extends AbstractInputStreamDocumentHandler {

	protected Document doGetDocumentWithInputStream(Map description, InputStream inputStream) throws IOException {
		//The text is analyzed and indexed but not stored
		Document document = LucenePDFDocument.getDocument(inputStream);
		if( description.get(AbstractInputStreamDocumentHandler.FILENAME)!=null ) {
			document.add(new Field("type", "file", Field.Store.YES, Field.Index.UN_TOKENIZED));
			document.add(new Field("filename", (String)description.get(AbstractInputStreamDocumentHandler.FILENAME), Field.Store.YES, Field.Index.UN_TOKENIZED));
		}
		return document;
	}

}
