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

package org.springmodules.samples.lucene.index.console;

import java.io.File;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springmodules.lucene.index.factory.IndexFactory;
import org.springmodules.lucene.index.object.directory.DefaultDirectoryIndexer;
import org.springmodules.lucene.index.object.directory.FileDocumentIndexingListener;
import org.springmodules.lucene.index.support.file.DocumentHandlerManager;
import org.springmodules.samples.lucene.util.ExecutionTimeUtils;

/**
 * Main class to index all datas (directories and databases)
 * 
 * @author Thierry Templier
 */
public class SimpleDirectoryIndexingImpl implements DirectoryIndexing,InitializingBean {

	private IndexFactory indexFactory;
	private DocumentHandlerManager documentHandlerManager;
	private DefaultDirectoryIndexer indexer;

	public SimpleDirectoryIndexingImpl() {
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		if( indexFactory==null ) {
			throw new IllegalArgumentException("indexFactory is required");
		}
		this.indexer=new DefaultDirectoryIndexer(indexFactory,documentHandlerManager);
	}

	public void indexDirectory(String directory) {
		indexer.index(directory,true);
	}

	public void prepareListeners() {
		FileDocumentIndexingListener listener=new FileDocumentIndexingListener() {
			private long indexingBeginningTime=0;
			private long indexingDocumentBeginningTime=0;

			public void beforeIndexingDirectory(File file) {
				System.out.println("Indexing the directory : "+file.getPath()+" ...");
			}

			public void afterIndexingDirectory(File file) {
				System.out.println(" -> Directory indexed.");
			}

			public void beforeIndexingFile(File file) {
				ExecutionTimeUtils.executionBeginning();
				System.out.println("Indexing the file : "+file.getPath()+" ...");
			}

			public void afterIndexingFile(File file) {
				String duration=ExecutionTimeUtils.showExecutionTime();
				System.out.println(" -> File indexed ("+duration+").");
			}

			public void onErrorIndexingFile(File file, Exception ex) {
				System.out.println(" -> Error during the indexing : "+ex.getMessage());
			}

			public void onNotAvailableHandler(File file) {
				System.out.println("No handler registred for the file : "+file.getPath()+" ...");
			}

		};
		indexer.addListener(listener);
	}

	public static void main(String[] args) {
		ClassPathXmlApplicationContext ctx=new ClassPathXmlApplicationContext("/applicationContext.xml");
		DirectoryIndexing indexing=(DirectoryIndexing)ctx.getBean("indexingDirectory");
		indexing.prepareListeners();
		String directory="c:/temp/lucene-documents";
		if( args.length==1 ) {
			directory=args[0];
		}
		indexing.indexDirectory(directory);
	}

	public IndexFactory getIndexFactory() {
		return indexFactory;
	}

	public void setIndexFactory(IndexFactory factory) {
		indexFactory = factory;
	}

	public DocumentHandlerManager getDocumentHandlerManager() {
		return documentHandlerManager;
	}

	public void setDocumentHandlerManager(DocumentHandlerManager manager) {
		documentHandlerManager = manager;
	}

}
