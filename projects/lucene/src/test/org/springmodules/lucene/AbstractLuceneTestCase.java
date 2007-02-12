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

package org.springmodules.lucene;

import junit.framework.TestCase;

import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.RAMDirectory;

/**
 * @author Thierry Templier
 */
public abstract class AbstractLuceneTestCase extends TestCase {

	protected RAMDirectory directory;

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		//Initialization of the index
		this.directory = new RAMDirectory();
		IndexWriter writer = new IndexWriter(directory,new SimpleAnalyzer(),true);

		//Adding a document
		Document document1 = new Document();
		document1.add(new Field("id", "1", Field.Store.YES, Field.Index.UN_TOKENIZED));
		document1.add(new Field("field", "a sample", Field.Store.YES, Field.Index.TOKENIZED));
		document1.add(new Field("filter", "a sample filter", Field.Store.YES, Field.Index.TOKENIZED));
		document1.add(new Field("sort", "2", Field.Store.YES, Field.Index.UN_TOKENIZED));
		writer.addDocument(document1);

		//Adding a document
		Document document2 = new Document();
		document2.add(new Field("id", "2", Field.Store.YES, Field.Index.UN_TOKENIZED));
		document2.add(new Field("field", "a Lucene support sample", Field.Store.YES, Field.Index.TOKENIZED));
		document2.add(new Field("filter", "another sample filter", Field.Store.YES, Field.Index.TOKENIZED));
		document2.add(new Field("sort", "3", Field.Store.YES, Field.Index.UN_TOKENIZED));
		writer.addDocument(document2);

		//Adding a document
		Document document3 = new Document();
		document3.add(new Field("id", "3", Field.Store.YES, Field.Index.UN_TOKENIZED));
		document3.add(new Field("field", "a different sample", Field.Store.YES, Field.Index.TOKENIZED));
		document3.add(new Field("filter", "another sample filter", Field.Store.YES, Field.Index.TOKENIZED));
		document3.add(new Field("sort", "1", Field.Store.YES, Field.Index.UN_TOKENIZED));
		writer.addDocument(document3);

		writer.optimize();
		writer.close();
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		this.directory=null;
	}
}
