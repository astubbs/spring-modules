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

package org.springmodules.samples.lucene.dao.indexing.handlers;

import java.io.IOException;
import java.io.InputStream;

import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.rtf.RTFEditorKit;

import org.springmodules.lucene.index.support.handler.handler.DocumentHandler;

/**
 * @author Thierry Templier
 */
public class DefaultRtfDocumentHandler extends AbstractDocumentHandler implements DocumentHandler {

	protected String extractText(InputStream inputStream) throws IOException {
		DefaultStyledDocument styledDoc = new DefaultStyledDocument();
		try {
			new RTFEditorKit().read(inputStream, styledDoc, 0);
			return styledDoc.getText(0, styledDoc.getLength());
		} catch(BadLocationException ex) {
			ex.printStackTrace();
		}
		return null;
	}

}
