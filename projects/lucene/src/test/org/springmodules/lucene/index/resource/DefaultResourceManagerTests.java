package org.springmodules.lucene.index.resource;

import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.springmodules.lucene.index.LuceneIndexingException;
import org.springmodules.lucene.index.core.DefaultLuceneIndexTemplate;
import org.springmodules.lucene.index.factory.IndexFactory;
import org.springmodules.lucene.index.factory.LuceneIndexReader;
import org.springmodules.lucene.index.factory.LuceneIndexWriter;

public class DefaultResourceManagerTests extends AbstractDefaultResourceManagerTests {
	
	public void testTwoResourcesOpened() throws Exception {
		IndexFactory indexFactory = createIndexFactory();
		ResourceManager resourceManager = createResourceManager(indexFactory);
		
		DefaultResourceManagementDefinition definition = new DefaultResourceManagementDefinition();
		definition.setIndexReaderOpen(true);
		definition.setIndexWriterOpen(true);
		
		resourceManager.initializeResources(definition);
		ResourceHolder holder = (ResourceHolder)ResourceBindingManager.getResource(indexFactory);
		
		try {
			assertNotNull(holder.getIndexReader());
			assertNotNull(holder.getIndexWriter());
		} finally {
			resourceManager.releaseResources();
		}
	}

	public void testOnlyIndexReaderOpened() throws Exception {
		IndexFactory indexFactory = createIndexFactory();
		ResourceManager resourceManager = createResourceManager(indexFactory);
		
		DefaultResourceManagementDefinition definition = new DefaultResourceManagementDefinition();
		definition.setIndexReaderOpen(true);
		definition.setIndexWriterOpen(false);
		
		resourceManager.initializeResources(definition);
		ResourceHolder holder = (ResourceHolder)ResourceBindingManager.getResource(indexFactory);
		assertNotNull(holder.getIndexReader());
		try {
			holder.getIndexWriter();
			fail();
		} catch(LuceneIndexingException ex) {
		} finally {
			resourceManager.releaseResources();
		}
	}

	public void testOnlyIndexReaderOpenedAndProtected() throws Exception {
		IndexFactory indexFactory = createIndexFactory();
		ResourceManager resourceManager = createResourceManager(indexFactory);
		
		DefaultResourceManagementDefinition definition = new DefaultResourceManagementDefinition();
		definition.setIndexReaderOpen(true);
		definition.setWriteOperationsForIndexReaderAuthorized(false);
		definition.setIndexWriterOpen(false);
		
		resourceManager.initializeResources(definition);
		ResourceHolder holder = (ResourceHolder)ResourceBindingManager.getResource(indexFactory);
		assertNotNull(holder.getIndexReader());
		try {
			holder.getIndexWriter();
			fail();
		} catch(LuceneIndexingException ex) {}
		
		LuceneIndexReader indexReader = holder.getIndexReader();
		
		try {
			indexReader.deleteDocument(2);
			fail();
		} catch(LuceneIndexingException ex) {}

		try {
			indexReader.deleteDocuments(new Term("id", "1"));
			fail();
		} catch(LuceneIndexingException ex) {}

		try {
			indexReader.setNorm(2, null, 2);
			fail();
		} catch(LuceneIndexingException ex) {}

		try {
			indexReader.undeleteAll();
			fail();
		} catch(LuceneIndexingException ex) {}
		
		resourceManager.releaseResources();
	}

	public void testOnlyIndexWriterOpened() throws Exception {
		IndexFactory indexFactory = createIndexFactory();
		ResourceManager resourceManager = createResourceManager(indexFactory);
		
		DefaultResourceManagementDefinition definition = new DefaultResourceManagementDefinition();
		definition.setIndexReaderOpen(false);
		definition.setIndexWriterOpen(true);
		
		resourceManager.initializeResources(definition);
		ResourceHolder holder = (ResourceHolder)ResourceBindingManager.getResource(indexFactory);
		try {
			holder.getIndexReader();
			fail();
		} catch(LuceneIndexingException ex) {
		} finally {
			resourceManager.releaseResources();
		}
		
		try {
			assertNotNull(holder.getIndexWriter());
		} finally {
			resourceManager.releaseResources();
		}
	}

	public void testOnlyIndexWriterOpenedAndProtected() throws Exception {
		IndexFactory indexFactory = createIndexFactory();
		ResourceManager resourceManager = createResourceManager(indexFactory);
		
		DefaultResourceManagementDefinition definition = new DefaultResourceManagementDefinition();
		definition.setIndexReaderOpen(false);
		definition.setIndexWriterOpen(true);
		definition.setWriteOperationsForIndexWriterAuthorized(false);
		
		resourceManager.initializeResources(definition);
		ResourceHolder holder = (ResourceHolder)ResourceBindingManager.getResource(indexFactory);
		try {
			holder.getIndexReader();
			fail();
		} catch(LuceneIndexingException ex) {}
		assertNotNull(holder.getIndexWriter());
		
		LuceneIndexWriter indexWriter = holder.getIndexWriter();
		
		try {
			Document document = new Document();
			document.add(new Field("id", "1", Field.Store.YES, Field.Index.UN_TOKENIZED));
			indexWriter.addDocument(document);
			fail();
		} catch(LuceneIndexingException ex) {}
		
		try {
			indexWriter.addIndexes(new Directory[] { new RAMDirectory() });
			fail();
		} catch(LuceneIndexingException ex) {}

		try {
			indexWriter.optimize();
			fail();
		} catch(LuceneIndexingException ex) {}

		try {
			indexWriter.setCommitLockTimeout(2);
			fail();
		} catch(LuceneIndexingException ex) {}
		
		resourceManager.releaseResources();
	}

	public void testTemplateExecutionWithIndexReader() throws Exception {
		IndexFactory indexFactory = createIndexFactory();
		ResourceManager resourceManager = createResourceManager(indexFactory);
		
		DefaultLuceneIndexTemplate template = new DefaultLuceneIndexTemplate();
		template.setIndexFactory(indexFactory);
		template.setAnalyzer(new SimpleAnalyzer());
		template.afterPropertiesSet();

		DefaultResourceManagementDefinition definition = new DefaultResourceManagementDefinition();
		definition.setIndexReaderOpen(true);
		definition.setWriteOperationsForIndexReaderAuthorized(true);
		try {
			resourceManager.initializeResources(definition);
			
			template.deleteDocuments(new Term("id", "1"));
			template.hasDeletions();
			template.isDeleted(1);
			template.isDeleted(2);
			template.undeleteDocuments();
		} finally {
			resourceManager.releaseResources();
		}
	}

	public void testTemplateExecutionWithIndexWriter() throws Exception {
		IndexFactory indexFactory = createIndexFactory();
		ResourceManager resourceManager = createResourceManager(indexFactory);
		
		DefaultLuceneIndexTemplate template = new DefaultLuceneIndexTemplate();
		template.setIndexFactory(indexFactory);
		template.setAnalyzer(new SimpleAnalyzer());
		template.afterPropertiesSet();

		DefaultResourceManagementDefinition definition = new DefaultResourceManagementDefinition();
		definition.setIndexWriterOpen(true);
		definition.setWriteOperationsForIndexWriterAuthorized(true);
		try {
			resourceManager.initializeResources(definition);
			
			Document document = new Document();
			document.add(new Field("id", "1", Field.Store.YES, Field.Index.UN_TOKENIZED));
			template.addDocument(document);
			template.addDocument(document);
			template.addDocument(document);
			template.optimize();
		} finally {
			resourceManager.releaseResources();
		}
	}

	public void testTemplateExecutionWithIndexWriterReaderWithError() throws Exception {
		IndexFactory indexFactory = createIndexFactory();
		ResourceManager resourceManager = createResourceManager(indexFactory);
		
		DefaultLuceneIndexTemplate template = new DefaultLuceneIndexTemplate();
		template.setIndexFactory(indexFactory);
		template.setAnalyzer(new SimpleAnalyzer());
		template.afterPropertiesSet();

		DefaultResourceManagementDefinition definition = new DefaultResourceManagementDefinition();
		definition.setIndexWriterOpen(true);
		definition.setWriteOperationsForIndexWriterAuthorized(true);
		//definition.setIndexReaderOpen(true);
		definition.setWriteOperationsForIndexReaderAuthorized(false);
		try {
			resourceManager.initializeResources(definition);
			
			Document document = new Document();
			document.add(new Field("id", "3", Field.Store.YES, Field.Index.UN_TOKENIZED));
			template.addDocument(document);
			template.addDocument(document);
			template.addDocument(document);
			try {
				template.deleteDocuments(new Term("id", "1"));
				fail();
			} catch(LuceneIndexingException ex) {}
			template.optimize();
		} finally {
			resourceManager.releaseResources();
		}
	}

	public void testTemplateExecutionWithIndexWriterReader() throws Exception {
		IndexFactory indexFactory = createIndexFactory();
		ResourceManager resourceManager = createResourceManager(indexFactory);
		
		DefaultLuceneIndexTemplate template = new DefaultLuceneIndexTemplate();
		template.setIndexFactory(indexFactory);
		template.setAnalyzer(new SimpleAnalyzer());
		template.afterPropertiesSet();

		DefaultResourceManagementDefinition definition = new DefaultResourceManagementDefinition();
		definition.setIndexWriterOpen(true);
		definition.setWriteOperationsForIndexWriterAuthorized(true);
		definition.setIndexReaderOpen(true);
		definition.setWriteOperationsForIndexReaderAuthorized(false);
		try {
			resourceManager.initializeResources(definition);
			
			Document document = new Document();
			document.add(new Field("id", "3", Field.Store.YES, Field.Index.UN_TOKENIZED));
			template.addDocument(document);
			template.addDocument(document);
			template.addDocument(document);
			try {
				template.deleteDocuments(new Term("id", "1"));
			} catch(LuceneIndexingException ex) {}
			template.optimize();
		} finally {
			resourceManager.releaseResources();
		}
	}
	
}
