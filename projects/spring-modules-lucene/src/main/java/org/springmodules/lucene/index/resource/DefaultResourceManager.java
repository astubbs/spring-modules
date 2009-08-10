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

package org.springmodules.lucene.index.resource;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.springmodules.lucene.index.LuceneIndexingException;
import org.springmodules.lucene.index.factory.IndexReaderFactoryUtils;
import org.springmodules.lucene.index.factory.IndexWriterFactoryUtils;
import org.springmodules.lucene.index.factory.LuceneIndexReader;
import org.springmodules.lucene.index.factory.LuceneIndexWriter;

/**
 * Default implementation of ResourceManager interface.
 * 
 * @author Thierry Templier
 */
public class DefaultResourceManager extends AbstractResourceManager {

	/**
	 * Initialize LuceneIndexReader instance if necessary based on the
	 * specified definition.
	 * 
	 * @param definition a definition of the resources to initialize
	 * @return an instance of LuceneIndexWriter
	 */
	private LuceneIndexReader doInitializeIndexReader(ResourceManagementDefinition definition) {
		LuceneIndexReader indexReader = null;
		System.out.println("[doInitializeIndexReader] isIndexReaderOpen = "+definition.isIndexReaderOpen());
		System.out.println("[doInitializeIndexReader] isWriteOperationsForIndexReaderAuthorized = "+definition.isWriteOperationsForIndexReaderAuthorized());
		if( definition.isIndexReaderOpen() ) {
			if( definition.isWriteOperationsForIndexReaderAuthorized() ) {
				indexReader = getIndexFactory().getIndexReader();
			} else {
				indexReader = (LuceneIndexReader) Proxy.newProxyInstance(
						LuceneIndexReader.class.getClassLoader(),
						new Class[] {LuceneIndexReader.class},
						new ReadOnlyLuceneIndexReaderInvocationHandler(getIndexFactory().getIndexReader()));
			}
		}
		return indexReader;
	}

	/**
	 * Initialize LuceneIndexWriter instance if necessary based on the
	 * specified definition.
	 * 
	 * @param definition a definition of the resources to initialize
	 * @return an instance of LuceneIndexWriter
	 */
	private LuceneIndexWriter doInitializeIndexWriter(ResourceManagementDefinition definition) {
		LuceneIndexWriter indexWriter = null;
		System.out.println("[doInitializeIndexReader] isIndexWriterOpen = "+definition.isIndexWriterOpen());
		System.out.println("[doInitializeIndexReader] isWriteOperationsForIndexWriterAuthorized = "+definition.isWriteOperationsForIndexWriterAuthorized());
		if( definition.isIndexWriterOpen() ) {
			if( definition.isWriteOperationsForIndexWriterAuthorized() ) {
				indexWriter = getIndexFactory().getIndexWriter();
			} else {
				indexWriter = (LuceneIndexWriter) Proxy.newProxyInstance(
					LuceneIndexWriter.class.getClassLoader(),
					new Class[] {LuceneIndexWriter.class},
					new ReadOnlyLuceneIndexWriterInvocationHandler(getIndexFactory().getIndexWriter()));
			}
		}
		return indexWriter;
	}

	/**
	 * Initialize LuceneIndexReader and/or LuceneIndexWriter according to
	 * the definition of resource management.
	 * 
	 * @see AbstractResourceManager#doInitializeResources(ResourceManagementDefinition)
	 */
	protected ResourceHolder doInitializeResources(ResourceManagementDefinition definition) {
		LuceneIndexReader indexReader = doInitializeIndexReader(definition);
		LuceneIndexWriter indexWriter = doInitializeIndexWriter(definition);

		return new ResourceHolder(indexReader, indexWriter);
	}

	/**
	 * Release all the resources initialized.
	 * 
	 * @see AbstractResourceManager#doReleaseResources(ResourceHolder)
	 */
	protected void doReleaseResources(ResourceHolder holder) {
		LuceneIndexReader indexReader = holder.getIndexReader(false);
		IndexReaderFactoryUtils.closeIndexReader(indexReader);
		
		LuceneIndexWriter indexWriter = holder.getIndexWriter(false);
		IndexWriterFactoryUtils.closeIndexWriter(indexWriter);
	}

	/**
	 * Invocation handler that delegates close calls on JDBC Connections
	 * to DataSourceUtils for being aware of thread-bound transactions.
	 */
	private class ReadOnlyLuceneIndexReaderInvocationHandler implements InvocationHandler {
		private final String INDEX_READER_FORBIDDEN_METHODS = "deleteDocument,deleteDocuments,setNorm,undeleteAll";
		private final LuceneIndexReader indexReader;

		public ReadOnlyLuceneIndexReaderInvocationHandler(LuceneIndexReader indexReader) {
			this.indexReader = indexReader;
		}

		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			if( INDEX_READER_FORBIDDEN_METHODS.indexOf(method.getName())!=-1 ) {
				throw new LuceneIndexingException("Not authorized operation in this context.");
			} else {
				return method.invoke(indexReader, args);
			}
		}
	}

	/**
	 * Invocation handler that delegates close calls on JDBC Connections
	 * to DataSourceUtils for being aware of thread-bound transactions.
	 */
	private class ReadOnlyLuceneIndexWriterInvocationHandler implements InvocationHandler {
		private final String INDEX_WRITER_FORBIDDEN_METHODS = "addDocument,addIndexes,optimize," +
				"setCommitLockTimeout,setInfoStream,setMaxBufferedDocs,setMaxFieldLength,setMaxMergeDocs," +
				"setMergeFactor,setSimilarity,setTermIndexInterval,setUseCompoundFile,setWriteLockTimeout";
		private final LuceneIndexWriter indexWriter;

		public ReadOnlyLuceneIndexWriterInvocationHandler(LuceneIndexWriter indexWriter) {
			this.indexWriter = indexWriter;
		}

		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			if( INDEX_WRITER_FORBIDDEN_METHODS.indexOf(method.getName())!=-1 ) {
				throw new LuceneIndexingException("Not authorized operation in this context.");
			} else {
				return method.invoke(indexWriter, args);
			}
		}
	}
}
