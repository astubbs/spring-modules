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

package org.springmodules.lucene.index.object.directory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.springmodules.lucene.index.LuceneIndexAccessException;
import org.springmodules.lucene.index.factory.IndexFactory;
import org.springmodules.lucene.index.factory.IndexWriterFactoryUtils;
import org.springmodules.lucene.index.object.AbstractIndexer;
import org.springmodules.lucene.index.support.file.DocumentHandler;
import org.springmodules.lucene.index.support.file.DocumentHandlerManager;
import org.springmodules.lucene.index.support.file.DocumentMatching;
import org.springmodules.lucene.index.support.file.ExtensionDocumentHandlerManager;
import org.springmodules.lucene.util.FileUtils;

/**
 * <b>This is the central class in the lucene directory indexing package.</b>
 * It simplifies the use of lucene to index a directory specifiying the base
 * directory and the way to index every files contained in this directory and
 * its sub directories.
 * It helps to avoid common errors and to manage these resource in a flexible
 * manner.
 * It executes core Lucene workflow, leaving application code to focus on
 * the way to create Lucene documents from a row for a request.
 * 
 * <p>This class is based on a DocumentHandlerManager instance that can be
 * injected with IoC. So several document handlers for different file types
 * can be registred declaratively or using the registration methods of this
 * class. By default, only the text handler is registred for files
 * with the "txt" extension.
 *
 * <p>This class is based on the IndexFactory abstraction which is a
 * factory to create IndexWriter for the configured
 * Directory. For the execution and the indexation of the corresponding
 * datas, the indexer uses the same IndexWriter. It calls the IndexWriterFactoryUtils
 * class to eventually release it. So the indexer doesn't need to always
 * hold resources during the indexation of every requests and
 * this avoids some locking problems on the index. You can too apply
 * different strategies for managing index resources.
 *
 * <p>Can be used within a service implementation via direct instantiation
 * with a IndexFactory reference, or get prepared in an application context
 * and given to services as bean reference. Note: The IndexFactory should
 * always be configured as a bean in the application context, in the first case
 * given to the service directly, in the second case to the prepared template.
 * 
 * @author Thierry Templier
 * @see org.springmodules.lucene.index.object.AbstractIndexer
 * @see org.springmodules.lucene.index.factory.IndexFactory
 * @see org.springmodules.lucene.index.support.file.DocumentHandlerManager
 * @see org.springmodules.lucene.index.support.file.DocumentMatching
 * @see org.springmodules.lucene.index.support.file.DocumentHandler
 * @see org.springmodules.lucene.index.object.directory.DocumentIndexingListener
 * @see org.springmodules.lucene.index.factory.IndexWriterFactoryUtils#getIndexWriter(IndexFactory)
 * @see org.springmodules.lucene.index.factory.IndexWriterFactoryUtils#releaseIndexWriter(IndexFactory, IndexWriter)
/**
 * @author Thierry Templier
 */
public class DirectoryIndexer extends AbstractIndexer {
	private DocumentHandlerManager documentHandlerManager;
	private List listeners;

	public DirectoryIndexer(IndexFactory indexFactory) {
		this(indexFactory,null);
	}

	public DirectoryIndexer(IndexFactory indexFactory,DocumentHandlerManager documentHandlerManager) {
		setIndexFactory(indexFactory);
		if( documentHandlerManager==null ) {
			this.documentHandlerManager=new ExtensionDocumentHandlerManager();
			this.documentHandlerManager.registerDefautHandlers();
		} else {
			this.documentHandlerManager=documentHandlerManager;
		}
		listeners=new ArrayList();
	}

	public void registerDocumentHandler(DocumentMatching matching,DocumentHandler handler) {
		documentHandlerManager.registerDocumentHandler(matching,handler);
	}

	public void unregisterDocumentHandler(DocumentMatching matching) {
		documentHandlerManager.unregisterDocumentHandler(matching);
	}

	public void addListener(DocumentIndexingListener listener) {
		if( listener!=null ) {
			listeners.add(listener);
		}
	}

	public void removeListener(DocumentIndexingListener listener) {
		if( listener!=null ) {
			listeners.remove(listener);
		}
	}

	public List getListeners() {
		return listeners;
	}

	protected void fireListenersOnBeforeDirectory(File file) {
		for(Iterator i=listeners.iterator();i.hasNext();) {
			DocumentIndexingListener listener=(DocumentIndexingListener)i.next();
			listener.beforeIndexingDirectory(file);
		}
	}

	protected void fireListenersOnAfterDirectory(File file) {
		for(Iterator i=listeners.iterator();i.hasNext();) {
			DocumentIndexingListener listener=(DocumentIndexingListener)i.next();
			listener.afterIndexingDirectory(file);
		}
	}

	protected void fireListenersOnBeforeFile(File file) {
		for(Iterator i=listeners.iterator();i.hasNext();) {
			DocumentIndexingListener listener=(DocumentIndexingListener)i.next();
			listener.beforeIndexingFile(file);
		}
	}

	protected void fireListenersOnAfterFile(File file) {
		for(Iterator i=listeners.iterator();i.hasNext();) {
			DocumentIndexingListener listener=(DocumentIndexingListener)i.next();
			listener.afterIndexingFile(file);
		}
	}

	protected void fireListenersOnErrorFile(File file,Exception ex) {
		for(Iterator i=listeners.iterator();i.hasNext();) {
			DocumentIndexingListener listener=(DocumentIndexingListener)i.next();
			listener.onErrorIndexingFile(file,ex);
		}
	}

	protected void fireListenersOnNoHandlerAvailable(File file) {
		for(Iterator i=listeners.iterator();i.hasNext();) {
			DocumentIndexingListener listener=(DocumentIndexingListener)i.next();
			listener.onNotAvailableHandler(file);
		}
	}

	private void indexDirectory(IndexWriter writer,File dirToParse) throws IOException {
		fireListenersOnBeforeDirectory(dirToParse);
		File[] files = dirToParse.listFiles();
		for(int cpt=0;cpt<files.length;cpt++) {
			File currentFile = files[cpt];
			if (currentFile.isDirectory()) {
				indexDirectory(writer, currentFile);
			} else {
				indexFile(writer, currentFile);
			}
		}
		fireListenersOnAfterDirectory(dirToParse);
	}

	public DocumentHandler getDocumentHandler(String fileName) {
		return documentHandlerManager.getDocumentHandler(fileName);
	}

	private Document doCallHandler(File file,FileInputStream inputStream,DocumentHandler handler) throws IOException {
		Map description=new HashMap();
		description.put(DocumentHandler.FILENAME,file.getAbsolutePath());
		return handler.getDocument(description,inputStream);
	}

	private void indexFile(IndexWriter writer,File file) throws IOException {
		fireListenersOnBeforeFile(file);
		DocumentHandler handler = getDocumentHandler(file.getPath());
		if( handler!=null ) {
			FileInputStream inputStream=null;
			try {
				inputStream=new FileInputStream(file);
				Document document=doCallHandler(file,inputStream,handler);
				if( document!=null ) {
					writer.addDocument(document);
				}
				fireListenersOnAfterFile(file);
			} catch(IOException ex) {
				fireListenersOnErrorFile(file,ex);
			} catch(Exception ex) {
				logger.error("Error during indexing the file "+file.getName(),ex);
				fireListenersOnErrorFile(file,ex);
			} finally {
				FileUtils.closeInputStream(inputStream);
			}
		} else {
			fireListenersOnNoHandlerAvailable(file);
		}
	}

	public void index(String dirToParse) {
		index(dirToParse,false);
	}

	private boolean checkBaseDirectory(String dirToParse) {
		File dir=new File(dirToParse);
		if( !dir.exists() ) {
			return false;
		} else {
			return true;
		}
	}

	public void index(String dirToParse,boolean optimizeIndex) {
		if( !checkBaseDirectory(dirToParse) ) {
			throw new RuntimeException("The base directory doesn't exist!");
		}

		IndexWriter writer = IndexWriterFactoryUtils.getIndexWriter(getIndexFactory());
		try {
			File file=new File(dirToParse);
			//Indexing the directory
			if( file.isDirectory() ) {
				indexDirectory(writer,new File(dirToParse));
			} else {
				indexFile(writer,file);
			}
			//Optimize the index
			if( optimizeIndex ) {
				writer.optimize();
			}
		} catch(IOException ex) {
			logger.error("Error during indexing the directory : "+dirToParse,ex);
			throw new LuceneIndexAccessException("Error during indexing the directory : "+dirToParse,ex);
		} finally {
			IndexWriterFactoryUtils.releaseIndexWriter(getIndexFactory(),writer);
		}
	}

}
