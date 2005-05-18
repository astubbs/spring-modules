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
import org.springmodules.lucene.index.LuceneIndexingException;
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
 * factory to create IndexWriter for the configured Directory. For the
 * execution and the indexation of the corresponding datas, the indexer
 * uses the same IndexWriter. It calls the IndexWriterFactoryUtils
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
 * @see org.springmodules.lucene.index.support.file.ExtensionDocumentHandlerManager
 * @see org.springmodules.lucene.index.support.file.ExtensionDocumentHandlerManagerFactoryBean
 * @see org.springmodules.lucene.index.support.file.DocumentMatching
 * @see org.springmodules.lucene.index.support.file.DocumentHandler
 * @see org.springmodules.lucene.index.object.directory.DocumentIndexingListener
 * @see org.springmodules.lucene.index.factory.IndexWriterFactoryUtils#getIndexWriter(IndexFactory)
 * @see org.springmodules.lucene.index.factory.IndexWriterFactoryUtils#releaseIndexWriter(IndexFactory, IndexWriter)
 */
public class DirectoryIndexer extends AbstractIndexer {
	private DocumentHandlerManager documentHandlerManager;
	private List listeners;

	/**
	 * Construct a new DirectoryIndexer, given an IndexFactory to obtain IndexWriter.
	 * 
	 * @param indexFactory IndexFactory to obtain IndexWriter
	 */
	public DirectoryIndexer(IndexFactory indexFactory) {
		this(indexFactory,null);
	}

	/**
	 * Construct a new DirectoryIndexer, given an IndexFactory to obtain IndexWriter
	 * and a DocumentHandlerManager which has been configured with Spring.
	 * 
	 * @param indexFactory IndexFactory to obtain IndexWriter
	 * @param documentHandlerManager DocumentHandlerManager which will be used by the indexer
	 */
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

	/**
	 * This method is used to register a document handler. It is associated to
	 * a DocumentMatching instance to determine when this handler must be used.
	 * As the indexer works on directories and files, it is used to determine
	 * which document handler will be used to index a file.
	 * 
	 * @param matching the DocumentMatching associated with the handler
	 * @param handler the handler to register
	 */
	public void registerDocumentHandler(DocumentMatching matching,DocumentHandler handler) {
		documentHandlerManager.registerDocumentHandler(matching,handler);
	}

	/**
	 * This method is used to unregister the document handler corresponding the
	 * mapping.
	 * 
	 * @param matching the DocumentMatching associated with the handler
	 */ 
	public void unregisterDocumentHandler(DocumentMatching matching) {
		documentHandlerManager.unregisterDocumentHandler(matching);
	}

	/**
	 * This method is used to get the DocumentHandler implementation
	 * corresponding to the fileName passed as parameter.
	 * 
	 * @param fileName the name of the file to index
	 * @return the handler to index the file
	 */
	public DocumentHandler getDocumentHandler(String fileName) {
		return documentHandlerManager.getDocumentHandler(fileName);
	}

	/**
	 * This method is used to add a listener to be notified during the
	 * indexing execution.
	 * 
	 * @param listener the listener to add
	 */
	public void addListener(DocumentIndexingListener listener) {
		if( listener!=null ) {
			listeners.add(listener);
		}
	}

	/**
	 * This method is used to remove a specifed listener.
	 * 
	 * @param listener the listener to remove
	 */
	public void removeListener(DocumentIndexingListener listener) {
		if( listener!=null ) {
			listeners.remove(listener);
		}
	}

	/**
	 * This method is used to get the list of listeners to notify
	 * during the indexing execution.
	 * 
	 * @return the list of listeners to notify
	 */
	public List getListeners() {
		return listeners;
	}

	/**
	 * This method is used to fire the "on before directory" event to
	 * every listeners.
	 * 
	 * <p>This event will be fired before the indexing of a directory.
	 * 
	 * @param file the directory which will be indexed
	 */
	protected void fireListenersOnBeforeDirectory(File file) {
		for(Iterator i=listeners.iterator();i.hasNext();) {
			DocumentIndexingListener listener=(DocumentIndexingListener)i.next();
			listener.beforeIndexingDirectory(file);
		}
	}

	/**
	 * This method is used to fire the "on after directory" event to
	 * every listeners.
	 * 
	 * <p>This event will be fired after the indexing of a directory, even
	 * if there have been errors during the indexing of its files.
	 * 
	 * @param file the directory which has been indexed
	 */
	protected void fireListenersOnAfterDirectory(File file) {
		for(Iterator i=listeners.iterator();i.hasNext();) {
			DocumentIndexingListener listener=(DocumentIndexingListener)i.next();
			listener.afterIndexingDirectory(file);
		}
	}

	/**
	 * This method is used to fire the "on before file" event to
	 * every listeners.
	 * 
	 * <p>This event will be fired before the indexing of a file, even
	 * if there are errors during its indexing.
	 * 
	 * @param file the file which will be indexed
	 */
	protected void fireListenersOnBeforeFile(File file) {
		for(Iterator i=listeners.iterator();i.hasNext();) {
			DocumentIndexingListener listener=(DocumentIndexingListener)i.next();
			listener.beforeIndexingFile(file);
		}
	}

	/**
	 * This method is used to fire the "on after file" event to
	 * every listeners.
	 * 
	 * <p>This event will be fired after the indexing of a file. It will
	 * not happen if there is an indexing error.
	 * 
	 * @param file the file which have been indexed
	 */
	protected void fireListenersOnAfterFile(File file) {
		for(Iterator i=listeners.iterator();i.hasNext();) {
			DocumentIndexingListener listener=(DocumentIndexingListener)i.next();
			listener.afterIndexingFile(file);
		}
	}

	/**
	 * This method is used to fire the "on error file" event to
	 * every listeners.
	 * 
	 * <p>This event will be fired if there is an indexing error.
	 * 
	 * @param file the file on which the error occurs
	 */
	protected void fireListenersOnErrorFile(File file,Exception ex) {
		for(Iterator i=listeners.iterator();i.hasNext();) {
			DocumentIndexingListener listener=(DocumentIndexingListener)i.next();
			listener.onErrorIndexingFile(file,ex);
		}
	}

	/**
	 * This method is used to fire the "on no handler available" event to
	 * every listeners.
	 * 
	 * <p>This event will be fired if there is no matching handler to index
	 * the file.
	 * 
	 * @param file the file to index
	 */
	protected void fireListenersOnNoHandlerAvailable(File file) {
		for(Iterator i=listeners.iterator();i.hasNext();) {
			DocumentIndexingListener listener=(DocumentIndexingListener)i.next();
			listener.onNotAvailableHandler(file);
		}
	}

	/**
	 * This method parses the directory, its index every files and
	 * calls itself recursively for all its sub directories in order
	 * to index them.
	 * 
	 * <p>This method fires too the different events corresponding to the
	 * indexing. At the beginning, the onBeforeDirectory method is called
	 * on every listeners. At the end, the onAfterDirectory method is called
	 * on every listeners.
	 * 
	 * @param writer the IndexWriter used to index files
	 * @param dirToParse the based directory to index
	 * @throws IOException if thrown by a Lucene method, to be auto-converted
	 * to a LuceneManipulateIndexException
	 * @see DocumentIndexingListener
	 */
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

	/**
	 * This method defines the description of the file to pass as parameter
	 * to the handler used and invokes it to get the indexed document for
	 * the file.
	 *  
	 * @param file the file to index
	 * @param inputStream the corresponding input stream
	 * @param handler the handler to use to index the file
	 * @return the indexed document to add to the index
	 * @throws IOException if thrown by a Lucene method, to be auto-converted
	 * to a LuceneManipulateIndexException
	 */
	private Document doCallHandler(File file,FileInputStream inputStream,DocumentHandler handler) throws IOException {
		Map description=new HashMap();
		description.put(DocumentHandler.FILENAME,file.getAbsolutePath());
		return handler.getDocument(description,inputStream);
	}

	/**
	 * This method indexes a file if there is a registred document handler
	 * which matches. It correctly opens and closes the file even if there
	 * are errors during the indexing.
	 * 
	 * <p>This method fires too the different events corresponding to the
	 * indexing. At the beginning, the beforeIndexingFile method is called
	 * on every listeners. If there is no matching document handler, the
	 * onNotAvailableHandler method is called on every listeners. If the
	 * indexing of the file is correctly done, the afterIndexingFile method
	 * is called and the onErrorIndexingFile otherwise.
	 * 
	 * @param writer the IndexWriter used to index files
	 * @param file the file to index
	 * @throws IOException if thrown by a Lucene method, to be auto-converted
	 * to a LuceneManipulateIndexException
	 * @see #doCallHandler(File, FileInputStream, DocumentHandler)
	 * @see DocumentIndexingListener
	 */
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

	/**
	 * This method is the entry point to index a directory recursively. It uses
	 * the registred document handlers to index every files.
	 * 
	 * <p>In this case, the index will not be optimized.
	 *  
	 * @param dirToParse the directory to index recursively
	 * @see #index(String, boolean)
	 */
	public void index(String dirToParse) {
		index(dirToParse,false);
	}

	/**
	 * This method checks if the directory to parse exists.
	 * 
	 * @param dirToParse the directory to check
	 * @return true if it exists, otherwise false
	 */
	private boolean checkBaseDirectory(String dirToParse) {
		File dir=new File(dirToParse);
		return dir.exists();
	}

	/**
	 * This method is the entry point to index a directory recursively. It uses
	 * the registred document handlers to index every files.
	 * 
	 * <p>In this case, the index will be optimized after each request if the
	 * value of the optimizeIndex parameter is true.
	 * 
	 * <p>If there is an error during executing a file, the other files will be
	 * executed. However the error will notify to specified listeners.
	 *  
	 * <p>This method gets an IndexWriter instance from the IndexWriterFactoryUtils
	 * class and release it at the end if necessary. 
	 * 
	 * @param dataSource the datasource to use
	 * @param optimizeIndex if the index must be optmized after
	 * the request indexing
	 * @see #indexDirectory(IndexWriter, File)
	 * @see #indexFile(IndexWriter, File)
	 * @see IndexWriterFactoryUtils#getIndexWriter(IndexFactory)
	 * @see IndexWriterFactoryUtils#releaseIndexWriter(IndexFactory, IndexWriter)
	 */
	public void index(String dirToParse,boolean optimizeIndex) {
		if( !checkBaseDirectory(dirToParse) ) {
			throw new LuceneIndexingException("The base directory doesn't exist!");
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
