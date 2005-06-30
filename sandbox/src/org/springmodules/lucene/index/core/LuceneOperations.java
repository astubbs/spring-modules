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

package org.springmodules.lucene.index.core;

import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;

/**
 * @author ttemplier
 *
 * Pour changer le modèle de ce commentaire de type généré, allez à :
 * Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public interface LuceneOperations {

	/**
	 * Delete the document corresponding to its internal document
	 * identifier.
	 * Note: Lucene deletes really this document at the IndexReader
	 * close. By default (if you don't share resources across several
	 * calls) the document is really delete from the index before the
	 * method returns. 
	 * @param internalDocumentId the internal document identifier
	 */
	void deleteDocument(int internalDocumentId);

	/**
	 * Delete one or more documents corresponding to a specified value
	 * for a field.
	 * Note: Lucene deletes really these documents at the IndexReader
	 * close. By default (if you don't share resources across several
	 * calls) the document is really delete from the index before the
	 * method returns. 
	 * @param term the term to specify a value for a field
	 */
	void deleteDocuments(Term term);

	/**
	 * Undelete every documents that have been marked as deleted. As
	 * Lucene deletes really these documents at the IndexReader
	 * close, this method is useful only if you don't share resources
	 * across several calls. In the contrary, the call of this method
	 * has no effect on the index.
	 */
	void undeleteDocuments();

	/**
	 * Check if a document corresponding to an internal document identifier
	 * has been marked as deleted. As Lucene deletes really these documents
	 * at the IndexReader close, this method is useful only if you don't
	 * share resources across several calls. In the contrary, the call of
	 * this method will always return false.
	 * @param internalDocumentId the internal document identifier
	 */
	boolean isDeleted(int internalDocumentId);

	/**
	 * Check if an index has documents marked as deleted. As Lucene deletes
	 * really these documents at the IndexReader close, this method is useful
	 * only if you don't share resources across several calls. In the contrary,
	 * the call of this method will always return false.
	 */
	boolean hasDeletions();

	/**
	 * Get the next document number which will be used to internally
	 * identify a document. Be aware that, if you share resources
	 * across several calls, this number is modified at every document
	 * add or document marked as deleted. 
	 * @return the next document number in the index
	 */
	int getMaxDoc();

	/**
	 * Get the number of documents in the index. Be aware that, if you
	 * share resources across several calls, this number represents the
	 * number of documents in the index plus the number of documents which
	 * will be added at the IndexWriter close minus the number of documents
	 * which are marked as deleted.
	 * @return the number of documents in the index
	 */
	int getNumDocs();

	/**
	 * Add a document created outside the template to the index. In this case,
	 * the application needs to manage exceptions.
	 * Note: Lucene adds really this document at the IndexWriter
	 * close. By default (if you don't share resources across several
	 * calls) the document is really added to the index before the
	 * method returns. 
	 * @param document the document to add
	 */
	void addDocument(Document document);

	/**
	 * Add a document created outside the template to the index, basing
	 * the analyzer parameter. In this case, the application needs to
	 * manage exceptions.
	 * Note: Lucene adds really this document at the IndexWriter
	 * close. By default (if you don't share resources across several
	 * calls) the document is really added to the index before the
	 * method returns. 
	 * @param document the document to add
	 * @param analyzer the Lucene analyzer to use to index
	 */
	void addDocument(Document document, Analyzer analyzer);

	/**
	 * Add a document thanks to a callback method defined in the DocumentCreator
	 * interface. In this case, the exceptions during the document creation are
	 * managed by the template.
	 * Note: Lucene adds really this document at the IndexWriter
	 * close. By default (if you don't share resources across several
	 * calls) the document is really added to the index before the
	 * method returns.
	 * @param creator the implementation of DocumentCreator that creates the document to add
	 * @see DocumentCreator
	 */
	void addDocument(DocumentCreator creator);

	/**
	 * Add a document thanks to a callback method defined in the DocumentCreator
	 * interface, basing the analyzer parameter. In this case, the exceptions
	 * during the document creation are managed by the template.
	 * Note: Lucene adds really this document at the IndexWriter
	 * close. By default (if you don't share resources across several
	 * calls) the document is really added to the index before the
	 * method returns. 
	 * @param creator the implementation of DocumentCreator that creates the document to add
	 * @param analyzer the Lucene analyzer to use to index
	 * @see DocumentCreator
	 */
	void addDocument(DocumentCreator documentCreator,Analyzer analyzer);

	/**
	 * Add a document thanks to a callback method defined in the
	 * InputStreamDocumentCreator interface, basing the analyzer parameter.
	 * In this case, the exceptions during the document creation and the
	 * InputStream are managed by the template. As a matter of the InputStream
	 * must be still opened when the document is added to the index.
	 * Note: Lucene adds really this document at the IndexWriter
	 * close. By default (if you don't share resources across several
	 * calls) the document is really added to the index before the
	 * method returns. 
	 * @param creator the implementation of DocumentCreator that creates the document to add
	 * @see InputStreamDocumentCreator
	 */
	void addDocument(InputStreamDocumentCreator creator);

	/**
	 * Add a document thanks to a callback method defined in the
	 * InputStreamDocumentCreator interface, basing the analyzer parameter.
	 * In this case, the exceptions during the document creation and the
	 * InputStream are managed by the template. As a matter of the InputStream
	 * must be still opened when the document is added to the index.
	 * Note: Lucene adds really this document at the IndexWriter
	 * close. By default (if you don't share resources across several
	 * calls) the document is really added to the index before the
	 * method returns. 
	 * @param creator the implementation of DocumentCreator that creates the document to add
	 * @param analyzer the Lucene analyzer to use to index
	 * @see InputStreamDocumentCreator
	 */
	void addDocument(InputStreamDocumentCreator documentCreator,Analyzer analyzer);

	/**
	 * Add a list of documents created outside the template to the index. In this case,
	 * the application needs to manage exceptions.
	 * Note: Lucene adds really these documents at the IndexWriter
	 * close. By default (if you don't share resources across several
	 * calls) the documents are really added to the index before the
	 * method returns. 
	 * @param documents the list of documents to add
	 */
	void addDocuments(List documents);

	/**
	 * Add a list of documents created outside the template to the index, basing
	 * the analyzer parameter. In this case, the application needs to
	 * manage exceptions.
	 * Note: Lucene adds really these documents at the IndexWriter
	 * close. By default (if you don't share resources across several
	 * calls) the documents are really added to the index before the
	 * method returns. 
	 * @param documents the list of documents to add
	 * @param analyzer the Lucene analyzer to use to index
	 */
	void addDocuments(List documents, Analyzer analyzer);

	/**
	 * Add a list of documents thanks to a callback method defined in the
	 * DocumentsCreator interface. In this case, the exceptions during the
	 * document creations are managed by the template.
	 * Note: Lucene adds really these documents at the IndexWriter
	 * close. By default (if you don't share resources across several
	 * calls) the documents are really added to the index before the
	 * method returns. 
	 * @param creator the implementation of DocumentCreator that creates the document to add
	 */
	void addDocuments(DocumentsCreator creator);

	/**
	 * Add a list of documents thanks to a callback method defined in the
	 * DocumentsCreator interface, basing the analyzer parameter. In this
	 * case, the exceptions during the document creation are managed by the template.
	 * Note: Lucene adds really these documents at the IndexWriter
	 * close. By default (if you don't share resources across several
	 * calls) the documents are really added to the index before the
	 * method returns. 
	 * @param creator the implementation of DocumentCreator that creates the document to add
	 * @param analyzer the Lucene analyzer to use to index
	 */
	void addDocuments(DocumentsCreator creator,Analyzer analyzer);

	/**
	 * Update a document thanks to a callback method defined in the
	 * DocumentModifier interface using the callback method defined
	 * in the DocumentIdentifier interface to identify it.
	 * In fact, with Lucene, you can't update a document of the index.
	 * So you need to remove the document and insert a new one with
	 * the modified fields.
	 * This method does the remove and the add operations for you.
	 * @param documentModifier the implementation of DocumentModifier to get the modified document
	 * @param identifierthe implementation of DocumentIdentifier to identify the document to modify
	 */
	void updateDocument(DocumentModifier documentModifier,DocumentIdentifier identifier);

	/**
	 * Update a document thanks to a callback method defined in the
	 * DocumentModifier interface using the callback method defined
	 * in the DocumentIdentifier interface to identify it.
	 * In fact, with Lucene, you can't update a document of the index.
	 * So you need to remove the document and insert a new one with
	 * the modified fields.
	 * This method does the remove and the add operations for you with
	 * a specified analyzer.
	 * @param documentModifier the implementation of DocumentModifier to get the modified document
	 * @param identifierthe implementation of DocumentIdentifier to identify the document to modify
	 * @param analyzer the Lucene analyzer to use to index
	 */
	void updateDocument(DocumentModifier documentUpdater,DocumentIdentifier identifier,Analyzer analyzer);

	/**
	 * Update documents thanks to a callback method defined in the
	 * DocumentsModifier interface using the callback method defined
	 * in the DocumentsIdentifier interface to identify them.
	 * In fact, with Lucene, you can't update documents of the index.
	 * So you need to remove the documents and insert new ones with
	 * the modified fields.
	 * This method does the remove and the add operations for you with
	 * a specified analyzer.
	 * @param documentModifier the implementation of DocumentsModifier to get the modified documents
	 * @param identifierthe implementation of DocumentIdentifier to identify the documents to modify
	 */
	void updateDocuments(DocumentsModifier documentsModifier,DocumentsIdentifier identifier);

	/**
	 * Update documents thanks to a callback method defined in the
	 * DocumentsModifier interface using the callback method defined
	 * in the DocumentsIdentifier interface to identify them.
	 * In fact, with Lucene, you can't update documents of the index.
	 * So you need to remove the documents and insert new ones with
	 * the modified fields.
	 * This method does the remove and the add operations for you with
	 * a specified analyzer.
	 * @param documentModifier the implementation of DocumentsModifier to get the modified documents
	 * @param identifierthe implementation of DocumentIdentifier to identify the documents to modify
	 * @param analyzer the Lucene analyzer to use to index
	 */
	void updateDocuments(DocumentsModifier documentsModifier,DocumentsIdentifier identifier,Analyzer analyzer);

	/**
	 * Add an index created outside the template to the index. In this case,
	 * the application needs to manage exceptions.
	 * Note: Lucene adds really this index at the IndexWriter
	 * close. By default (if you don't share resources across several
	 * calls) the index are really added to the index before the
	 * method returns. 
	 * @param documents the list of documents to add
	 */
	void addIndex(Directory directory);

	/**
	 * Add a list of indexes created outside the template to the index.
	 * In this case, the application needs to manage exceptions.
	 * Note: Lucene adds really these indexes at the IndexWriter
	 * close. By default (if you don't share resources across several
	 * calls) the indexes are really added to the index before the
	 * method returns. 
	 * @param directories the list of indexes to add
	 */
	void addIndexes(Directory[] directories);

	/**
	 * Index optimize method. We recommend to use this method as the end
	 * of an indexing.
	 */
	void optimize();

	/**
	 * Execute the action specified by the given action object within a
	 * Lucene IndexReader.
	 * Note: if you share resources across several calls, the IndexReader
	 * provides to the callback is the shared instance. A new one is not
	 * created.
	 * @param callback the callback object that exposes the IndexReader
	 */
	Object read(ReaderCallback callback);

	/**
	 * Execute the action specified by the given action object within a
	 * Lucene IndexWrier.
	 * Note: if you share resources across several calls, the IndexWriter
	 * provides to the callback is the shared instance. A new one is not
	 * created.
	 * @param callback the callback object that exposes the IndexWriter
	 */
	Object write(WriterCallback callback);
}