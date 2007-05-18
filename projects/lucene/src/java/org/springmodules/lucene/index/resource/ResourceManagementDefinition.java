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

/**
 * @author Thierry Templier
 */
public interface ResourceManagementDefinition {
	
	/**
	 * Specify if an instance of {@link org.springmodules.lucene.index.factory.LuceneIndexReader}
	 * must be gotten and bound to the local thread.
	 */
	boolean isIndexReaderOpen();
	
	/**
	 * Specify if the modify operations of the {@link org.springmodules.lucene.index.factory.LuceneIndexReader}
	 * like delete can be used. If false, the call of these operations will
	 * throw an exception.
	 * Is used only if the return of the {@link #isIndexReaderOpen()}} is true.
	 */
	boolean isWriteOperationsForIndexReaderAuthorized();
	
	/**
	 * Specify if an instance of {@link org.springmodules.lucene.index.factory.LuceneIndexWriter}
	 * must be gotten and bound to the local thread.
	 */
	boolean isIndexWriterOpen();
	
	/**
	 * Specify if the modify operations of the {@link org.springmodules.lucene.index.factory.LuceneIndexWriter}
	 * like addDocument, optimize can be used. If false, the call of these
	 * operations will throw an exception.
	 * Is used only if the return of the {@link #isIndexReaderOpen()}} is true.
	 */
	boolean isWriteOperationsForIndexWriterAuthorized();
}
