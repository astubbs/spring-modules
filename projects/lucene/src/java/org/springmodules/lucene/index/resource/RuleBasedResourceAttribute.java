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
public class RuleBasedResourceAttribute implements ResourceAttribute {
	public static final String INDEX_READER_OPEN = "INDEX_READER_OPEN";
	public static final String INDEX_WRITER_OPEN = "INDEX_WRITER_OPEN";
	public static final String WRITE_INDEX_READER_AUTHORIZED = "WRITE_INDEX_READER_AUTHORIZED";
	public static final String WRITE_INDEX_WRITER_AUTHORIZED = "WRITE_INDEX_WRITER_AUTHORIZED";
	
	private boolean indexReaderOpen = false;
	private boolean indexWriterOpen = false;
	private boolean writeOperationsForIndexReaderAuthorized = false;
	private boolean writeOperationsForIndexWriterAuthorized = false;

	/**
	 * @see ResourceAttribute#isIndexReaderOpen()
	 */
	public boolean isIndexReaderOpen() {
		return indexReaderOpen;
	}

	public void setIndexReaderOpen(boolean indexReaderOpen) {
		this.indexReaderOpen = indexReaderOpen;
	}
	
	/**
	 * @see ResourceAttribute#isIndexWriterOpen()
	 */
	public boolean isIndexWriterOpen() {
		return indexWriterOpen;
	}
	
	public void setIndexWriterOpen(boolean indexWriterOpen) {
		this.indexWriterOpen = indexWriterOpen;
	}
	
	/**
	 * @see ResourceAttribute#isWriteOperationsForIndexReaderAuthorized()
	 */
	public boolean isWriteOperationsForIndexReaderAuthorized() {
		return writeOperationsForIndexReaderAuthorized;
	}
	
	public void setWriteOperationsForIndexReaderAuthorized(
			boolean writeOperationsForIndexReaderAuthorized) {
		this.writeOperationsForIndexReaderAuthorized = writeOperationsForIndexReaderAuthorized;
	}
	
	/**
	 * @see ResourceAttribute#isWriteOperationsForIndexWriterAuthorized()
	 */
	public boolean isWriteOperationsForIndexWriterAuthorized() {
		return writeOperationsForIndexWriterAuthorized;
	}
	
	public void setWriteOperationsForIndexWriterAuthorized(
			boolean writeOperationsForIndexWriterAuthorized) {
		this.writeOperationsForIndexWriterAuthorized = writeOperationsForIndexWriterAuthorized;
	}

}
