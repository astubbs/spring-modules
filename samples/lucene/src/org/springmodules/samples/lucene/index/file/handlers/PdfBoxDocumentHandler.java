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

package org.springmodules.samples.lucene.index.file.handlers;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.pdfbox.searchengine.lucene.LucenePDFDocument;
import org.springmodules.lucene.index.support.file.DocumentHandler;

/**
 * @author Thierry Templier
 */
public class PdfBoxDocumentHandler implements DocumentHandler {

	/**
	 * @see org.springmodules.lucene.index.object.file.FileDocumentHandler#getDocument(java.io.File, java.io.FileReader)
	 */
	public Document getDocument(Map description, InputStream inputStream) throws IOException {
		//The text is analyzed and indexed but not stored
		Document document=LucenePDFDocument.getDocument(inputStream);
		if( description.get(DocumentHandler.FILENAME)!=null ) {
			document.add(Field.Keyword("type", "file"));
			document.add(Field.Keyword("filename", (String)description.get(DocumentHandler.FILENAME)));
		}
		return document;
	}

}
