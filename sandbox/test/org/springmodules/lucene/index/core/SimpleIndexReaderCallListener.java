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
 * @author Thierry Templier
 */
public class SimpleIndexReaderCallListener implements IndexReaderCallListener {
	private int numberReadersCreated=0;
	private boolean indexReaderUndeletedAll=false;
	private int indexReaderDeletedId=-1;
	private boolean indexReaderHasDeletions=false;
	private boolean indexReaderMaxDoc=false;
	private boolean indexReaderNumDocs=false;
	private int indexReaderIsDeleted=-1;
	private int readerClosed=0;

	private void initReaderVariables() {
		indexReaderUndeletedAll=false;
		indexReaderDeletedId=-1;
		indexReaderHasDeletions=false;
		indexReaderMaxDoc=false;
		indexReaderNumDocs=false;
		indexReaderIsDeleted=-1;
	}

	public int getNumberReadersCreated() {
		return numberReadersCreated;
	}

	public void readerCreated() {
		//initReaderVariables();
		numberReadersCreated++;
	}

	public void indexReaderClosed() {
		readerClosed++;
	}

	public int getNumberReaderClosed() {
		return readerClosed;
	}

	public void indexReaderDeleted(int id) {
		this.indexReaderDeletedId=id;
	}

	public int getIndexReaderDeletedId() {
		return indexReaderDeletedId;
	}

	public void indexReaderUndeletedAll() {
		this.indexReaderUndeletedAll=true;
		
	}

	public boolean isIndexReaderUndeletedAll() {
		return indexReaderUndeletedAll;
	}

	public void indexReaderHasDeletions() {
		indexReaderHasDeletions=true;
	}

	public boolean isIndexReaderHasDeletions() {
		return indexReaderHasDeletions;
	}

	public void indexReaderMaxDoc() {
		indexReaderMaxDoc=true;
	}

	public boolean isIndexReaderMaxDoc() {
		return indexReaderMaxDoc;
	}

	public void indexReaderNumDocs() {
		indexReaderNumDocs=true;
	}

	public boolean isIndexReaderNumDocs() {
		return indexReaderNumDocs;
	}

	public void indexReaderIsDeleted(int id) {
		indexReaderIsDeleted=id;
	}

	public int getIndexReaderIsDeleted() {
		return indexReaderIsDeleted;
	}

}
