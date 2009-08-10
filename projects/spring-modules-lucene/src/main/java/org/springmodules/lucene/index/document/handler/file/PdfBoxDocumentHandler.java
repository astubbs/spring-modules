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
import org.pdfbox.searchengine.lucene.LucenePDFDocument;
import org.springmodules.lucene.index.document.handler.DocumentHandler;
import org.springmodules.lucene.index.document.handler.DocumentHandlerManagerFactoryBean;

/**
 * This class is the DocumentHandler implementation based on the PdfBox
 * tool in order to index the text contained in PDF documents.
 * 
 * <p>It can be associated with the "pdf" file extension and can
 * be used to configure the ExtensionDocumentHandlerManager with
 * the DocumentHandlerManagerFactoryBean factory bean.
 *  
 * @author Thierry Templier
 * @see DocumentHandler
 * @see DocumentHandlerManagerFactoryBean
 * @see LucenePDFDocument
 */
public class PdfBoxDocumentHandler extends AbstractInputStreamDocumentHandler {

	protected Document doGetDocumentWithInputStream(Map description, InputStream inputStream) throws IOException{
		return LucenePDFDocument.getDocument(inputStream);
	}

}
