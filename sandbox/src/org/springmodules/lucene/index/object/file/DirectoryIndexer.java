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
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.springmodules.lucene.index.LuceneWriteIndexException;
import org.springmodules.lucene.index.factory.IndexFactory;
import org.springmodules.lucene.index.factory.IndexWriterFactoryUtils;
import org.springmodules.lucene.index.object.AbstractIndexer;
import org.springmodules.lucene.index.object.file.handlers.TextDocumentHandler;

/**
 * @author Thierry Templier
 */
public class DirectoryIndexer extends AbstractIndexer {
	private Map fileDocumentHandlers;

	public DirectoryIndexer(IndexFactory indexFactory) {
		setIndexFactory(indexFactory);
		fileDocumentHandlers=new HashMap();
		registerDefautHandlers();
	}

	protected void registerDefautHandlers() {
		//Register a default handler for text file (.txt)
		registerDocumentHandler(new DocumentExtensionMatching() {
			public boolean matchExtension(String extension) {
				if( extension.equals("txt") ) {
					return true;
				} else {
					return false;
				}
			}
		},new TextDocumentHandler());
	}

	public void registerDocumentHandler(DocumentMatching matching,FileDocumentHandler handler) {
		if( matching!=null && handler!=null ) {
			fileDocumentHandlers.put(matching,handler);
		}
	}

	public void unregisterDocumentHandler(DocumentMatching matching) {
		if( matching!=null ) {
			fileDocumentHandlers.remove(matching);
		}
	}

	private void indexDirectory(IndexWriter writer,File dirToParse) throws IOException {
		File[] files = dirToParse.listFiles();
		for(int cpt=0;cpt<files.length;cpt++) {
			File currentFile = files[cpt];
			if (currentFile.isDirectory()) {
				indexDirectory(writer, currentFile);
			} else {
				indexFile(writer, currentFile);
			}
		}
	}

	private FileDocumentHandler getDocumentHandler(String fileName) {
		Set keys=fileDocumentHandlers.keySet();
		for(Iterator i=keys.iterator();i.hasNext();) {
			DocumentMatching matching=(DocumentMatching)i.next();
			if( matching.match(fileName) ) {
				return (FileDocumentHandler)fileDocumentHandlers.get(matching);
			}
		}
		return null;
	}

	private Document doCallHandler(File file,FileReader reader,FileDocumentHandler handler) throws IOException {
		return handler.getDocument(file,reader);
	}

	private void indexFile(IndexWriter writer,File file) throws IOException {
		FileDocumentHandler handler = getDocumentHandler(file.getPath());
		if( handler!=null ) {
			FileReader reader=null;
			try {
				reader=new FileReader(file);
				Document document=doCallHandler(file,reader,handler);
				if( document!=null ) {
					System.out.println("fichier "+file.getName()+" indexe...");
					writer.addDocument(document);
				}
			} catch(IOException ex) {
				ex.printStackTrace();
			} finally {
				try {
					if( reader!=null ) {
						reader.close();
					}
				} catch(Exception ex) {
				}
			}
		}
	}

	public void index(String dirToParse) {
		index(dirToParse,false);
	}

	public void index(String dirToParse,boolean optimizeIndex) {
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
			throw new LuceneWriteIndexException("Error during indexing the directory : "+dirToParse,ex);
		} finally {
			IndexWriterFactoryUtils.closeIndexWriterIfNecessary(getIndexFactory(),writer);
		}
	}
}
