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

package org.springmodules.samples.lucene.index.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.springmodules.lucene.index.core.DocumentCreator;
import org.springmodules.lucene.index.support.LuceneIndexSupport;
import org.springmodules.lucene.util.FileUtils;
import org.springmodules.samples.lucene.index.domain.IndexInformations;

/**
 * @author Thierry Templier
 */
public class IndexAccessorImpl extends LuceneIndexSupport implements IndexAccessor {

	public IndexInformations getIndexInformations() {
		boolean hasDeletions=getTemplate().hasDeletions();
		int numDocs=getTemplate().getNumDocs();
		IndexInformations infos=new IndexInformations();
		infos.setHasDeletions(hasDeletions);
		infos.setNumDocs(numDocs);
		return infos;
	}

	public void addDocument(final String title,final String text) {
		getTemplate().addDocument(new DocumentCreator() {
			public Document createDocument() throws IOException {
				Document document = new Document();
				//The text is analyzed and indexed but not stored
				document.add(Field.UnStored("contents", text));
				document.add(Field.Keyword("type", "text"));
				document.add(Field.Keyword("filename", title));
				return document;
			}
		});
	}

	public void addDocument(final File file) {
		getTemplate().addDocument(new DocumentCreator() {
			public Document createDocument() throws IOException {
				FileInputStream inputStream=null;
				try {
					inputStream=new FileInputStream(file);
					Document document = new Document();
					//The text is analyzed and indexed but not stored
					document.add(Field.Text("contents", new InputStreamReader(inputStream)));
					document.add(Field.Keyword("type", "file"));
					document.add(Field.Keyword("filename", file.getCanonicalPath()));
					return document;
				} finally {
					FileUtils.closeInputStream(inputStream);
				}
			}
		});
	}
}
