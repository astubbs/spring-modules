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

package org.springmodules.lucene.index.object.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.springmodules.lucene.index.LuceneWriteIndexException;
import org.springmodules.lucene.index.factory.IndexFactory;
import org.springmodules.lucene.index.factory.IndexWriterFactoryUtils;
import org.springmodules.lucene.index.object.AbstractIndexer;
import org.springmodules.lucene.index.object.file.handlers.TextDocumentHandler;
import org.springmodules.lucene.util.FileUtils;

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
			this.documentHandlerManager=new SimpleDocumentHandlerManager();
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

	private DocumentHandler getDocumentHandler(String fileName) {
		return documentHandlerManager.getDocumentHandler(fileName);
	}

	private Document doCallHandler(File file,FileInputStream inputStream,DocumentHandler handler) throws IOException {
		Map description=new HashMap();
		description.put(DocumentHandler.FILENAME,file.getAbsolutePath());
		return handler.getDocument(description,inputStream);
	}

	private void indexFile(IndexWriter writer,File file) throws IOException {
		DocumentHandler handler = getDocumentHandler(file.getPath());
		if( handler!=null ) {
			fireListenersOnBeforeFile(file);
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
			throw new LuceneWriteIndexException("Error during indexing the directory : "+dirToParse,ex);
		} finally {
			IndexWriterFactoryUtils.closeIndexWriterIfNecessary(getIndexFactory(),writer);
		}
	}

}
