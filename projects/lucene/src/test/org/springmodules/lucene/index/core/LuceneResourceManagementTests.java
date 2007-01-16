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

package org.springmodules.lucene.index.core;

import java.io.IOException;

import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.springmodules.lucene.AbstractLuceneTestCase;
import org.springmodules.lucene.index.factory.SimpleIndexFactory;
import org.springmodules.resource.ResourceManager;
import org.springmodules.resource.support.ResourceBindingManager;
import org.springmodules.resource.support.ResourceCallback;
import org.springmodules.resource.support.ResourceTemplate;

/**
 * @author Thierry Templier
 */
public class LuceneResourceManagementTests extends AbstractLuceneTestCase {


	public void testLuceneReaderResourceManagement() {
		//Initialization of the index
		final SimpleAnalyzer analyzer=new SimpleAnalyzer();
		SimpleIndexFactory targetIndexFactory=new SimpleIndexFactory(directory,analyzer);
		final MockSimpleIndexFactory indexFactory=new MockSimpleIndexFactory(targetIndexFactory);

		ResourceManager rm=new LuceneIndexResourceManager(indexFactory);
		ResourceTemplate rt = new ResourceTemplate(rm);

		rt.execute(new ResourceCallback() {
			public Object doWithResource() {
				assertTrue("Has thread indexFactory", ResourceBindingManager.hasResource(indexFactory));

				//Lucene template
				LuceneIndexTemplate template=new DefaultLuceneIndexTemplate(indexFactory,analyzer);
				template.deleteDocument(0);
				template.hasDeletions();
				template.undeleteDocuments();

				return null;
			}
		});

		//Check if a reader has been opened
		assertEquals(indexFactory.getReaderListener().getNumberReadersCreated(),1);
		//Check if the document is marked for deletion
		assertEquals(indexFactory.getReaderListener().getIndexReaderDeletedId(),0);
		//Check if the hasDeletions has been called
		assertTrue(indexFactory.getReaderListener().isIndexReaderHasDeletions());
		//Check if the document is marked for deletion
		assertTrue(indexFactory.getReaderListener().isIndexReaderUndeletedAll());
		//Check if the reader of the template is closed
		assertEquals(indexFactory.getReaderListener().getNumberReadersClosed(),1);

	}

	public void testNoLuceneReaderResourceManagement() {
		//Initialization of the index
		SimpleAnalyzer analyzer=new SimpleAnalyzer();
		SimpleIndexFactory targetIndexFactory=new SimpleIndexFactory(directory,analyzer);
		MockSimpleIndexFactory indexFactory=new MockSimpleIndexFactory(targetIndexFactory);

		assertFalse("Has no thread indexFactory", ResourceBindingManager.hasResource(indexFactory));

		//Lucene template
		LuceneIndexTemplate template=new DefaultLuceneIndexTemplate(indexFactory,analyzer);
		assertFalse("Has no thread indexFactory", ResourceBindingManager.hasResource(indexFactory));
		template.deleteDocument(0);
		assertFalse("Has no thread indexFactory", ResourceBindingManager.hasResource(indexFactory));
		template.hasDeletions();
		assertFalse("Has no thread indexFactory", ResourceBindingManager.hasResource(indexFactory));
		template.undeleteDocuments();
		assertFalse("Has no thread indexFactory", ResourceBindingManager.hasResource(indexFactory));

		//Check if a reader has been opened
		assertEquals(indexFactory.getReaderListener().getNumberReadersCreated(),3);
		//Check if the document is marked for deletion
		assertEquals(indexFactory.getReaderListener().getIndexReaderDeletedId(),0);
		//Check if the hasDeletions has been called
		assertTrue(indexFactory.getReaderListener().isIndexReaderHasDeletions());
		//Check if the document is marked for deletion
		assertTrue(indexFactory.getReaderListener().isIndexReaderUndeletedAll());
		//Check if the reader of the template is closed
		assertEquals(indexFactory.getReaderListener().getNumberReadersClosed(),3);

	}

	public void testLuceneWriterResourceManagement() {
		//Initialization of the index
		final SimpleAnalyzer analyzer=new SimpleAnalyzer();
		SimpleIndexFactory targetIndexFactory=new SimpleIndexFactory(directory,analyzer);
		final MockSimpleIndexFactory indexFactory=new MockSimpleIndexFactory(targetIndexFactory);

		ResourceManager rm=new LuceneIndexResourceManager(indexFactory);
		ResourceTemplate rt = new ResourceTemplate(rm);

		rt.execute(new ResourceCallback() {
			public Object doWithResource() {
				assertTrue("Has thread indexFactory", ResourceBindingManager.hasResource(indexFactory));

				//Lucene template
				LuceneIndexTemplate template=new DefaultLuceneIndexTemplate(indexFactory,analyzer);
				template.addDocument(new DocumentCreator() {
					public Document createDocument() throws IOException {
						Document document=new Document();
						document.add(new Field("field", "a Lucene support sample", Field.Store.YES, Field.Index.TOKENIZED));
						document.add(new Field("filter", "another sample filter", Field.Store.YES, Field.Index.TOKENIZED));
						document.add(new Field("sort", "13", Field.Store.YES, Field.Index.UN_TOKENIZED));
						return document;
					}
				});
				template.addDocument(new DocumentCreator() {
					public Document createDocument() throws IOException {
						Document document=new Document();
						document.add(new Field("field", "a Lucene support sample", Field.Store.YES, Field.Index.TOKENIZED));
						document.add(new Field("filter", "another sample filter", Field.Store.YES, Field.Index.TOKENIZED));
						document.add(new Field("sort", "13", Field.Store.YES, Field.Index.UN_TOKENIZED));
						return document;
					}
				});

				return null;
			}
		});

		//Check if a reader has been opened
		assertEquals(indexFactory.getWriterListener().getNumberWritersCreated(),1);
		//Check if the number of added documents is correct
		assertEquals(indexFactory.getWriterListener().getIndexWriterAddDocuments(),2);
		//Check if the reader of the template is closed
		assertEquals(indexFactory.getWriterListener().getNumberWritersClosed(),1);

	}

	public void testNoLuceneWriterResourceManagement() {
		//Initialization of the index
		final SimpleAnalyzer analyzer=new SimpleAnalyzer();
		SimpleIndexFactory targetIndexFactory=new SimpleIndexFactory(directory,analyzer);
		final MockSimpleIndexFactory indexFactory=new MockSimpleIndexFactory(targetIndexFactory);

		//Lucene template
		LuceneIndexTemplate template=new DefaultLuceneIndexTemplate(indexFactory,analyzer);
		template.addDocument(new DocumentCreator() {
			public Document createDocument() throws IOException {
				Document document=new Document();
				document.add(new Field("field", "a Lucene support sample", Field.Store.YES, Field.Index.TOKENIZED));
				document.add(new Field("filter", "another sample filter", Field.Store.YES, Field.Index.TOKENIZED));
				document.add(new Field("sort", "13", Field.Store.YES, Field.Index.UN_TOKENIZED));
				return document;
			}
		});
		template.addDocument(new DocumentCreator() {
			public Document createDocument() throws IOException {
				Document document=new Document();
				document.add(new Field("field", "a Lucene support sample", Field.Store.YES, Field.Index.TOKENIZED));
				document.add(new Field("filter", "another sample filter", Field.Store.YES, Field.Index.TOKENIZED));
				document.add(new Field("sort", "13", Field.Store.YES, Field.Index.UN_TOKENIZED));
				return document;
			}
		});

		//Check if a reader has been opened
		assertEquals(indexFactory.getWriterListener().getNumberWritersCreated(),2);
		//Check if the number of added documents is correct
		assertEquals(indexFactory.getWriterListener().getIndexWriterAddDocuments(),2);
		//Check if the reader of the template is closed
		assertEquals(indexFactory.getWriterListener().getNumberWritersClosed(),2);

	}

}
