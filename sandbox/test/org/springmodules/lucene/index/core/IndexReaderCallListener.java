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

/**
 * Event interface to detect the index reader closes.
 * 
 * @author Thierry Templier
 */
public interface IndexReaderCallListener {
	public int getNumberReadersCreated();
	public void readerCreated();

	public void indexReaderClosed();
	public int getNumberReaderClosed();

	public void indexReaderDeleted(int id);
	public int getIndexReaderDeletedId();

	public void indexReaderIsDeleted(int id);
	public int getIndexReaderIsDeleted();

	public void indexReaderUndeletedAll();
	public boolean isIndexReaderUndeletedAll();

	public void indexReaderHasDeletions();
	public boolean isIndexReaderHasDeletions();

	public void indexReaderMaxDoc();
	public boolean isIndexReaderMaxDoc();

	public void indexReaderNumDocs();
	public boolean isIndexReaderNumDocs();
}
