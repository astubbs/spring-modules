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

package org.springmodules.samples.lucene.dao.indexing;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.Term;
import org.springmodules.lucene.index.core.DocumentCreator;
import org.springmodules.lucene.index.core.InputStreamDocumentCreatorWithManager;
import org.springmodules.lucene.index.support.LuceneIndexSupport;
import org.springmodules.lucene.index.support.file.DocumentHandler;
import org.springmodules.samples.lucene.bean.indexing.IndexInformations;
import org.springmodules.samples.lucene.ui.web.indexing.FileDocumentHolder;

/**
 * @author Thierry Templier
 */
public class IndexAccessorDaoImpl extends LuceneIndexSupport implements IndexAccessorDao {

	public IndexInformations getIndexInformations() {
		boolean hasDeletions=getTemplate().hasDeletions();
		int numDocs=getTemplate().getNumDocs();
		IndexInformations infos=new IndexInformations();
		infos.setHasDeletions(hasDeletions);
		infos.setNumDocs(numDocs);
		return infos;
	}

	public void addDocument(final String id,final String title,
							final String text,final String category) {
		getTemplate().addDocument(new DocumentCreator() {
			public Document createDocument() throws IOException {
				Document document = new Document();
				//The text is analyzed and indexed but not stored
				document.add(Field.Text("contents", text));
				document.add(Field.Keyword("id", id));
				document.add(Field.Keyword("type", "text"));
				document.add(Field.Keyword("filename", title));
				document.add(Field.Keyword("category", category));
				return document;
			}
		});
	}

	public void addDocument(final FileDocumentHolder holder) {
		getTemplate().addDocument(new InputStreamDocumentCreatorWithManager(getDocumentHandlerManager()) {
			public InputStream createInputStream() throws IOException {
				return new ByteArrayInputStream(holder.getFile());
			}

			protected String getResourceName() {
				return holder.getFilename();
			}

			protected Map getResourceDescription() {
				Map description=new HashMap();
				description.put(DocumentHandler.FILENAME,holder.getFilename());
				return description;
			}

			protected void addFields(Document document) {
				document.add(Field.Keyword("id", holder.getId()));
				document.add(Field.Keyword("category", holder.getCategory()));
			}
		});
	}

	public void purgeIndex() {
		getTemplate().deleteDocuments(new Term("",""));
	}

}
