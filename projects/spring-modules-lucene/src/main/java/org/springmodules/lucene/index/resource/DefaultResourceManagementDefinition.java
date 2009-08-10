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
 * Default implementation of the ResourceManagementDefinition interface
 * which specifies the strategy of usage of the resources.
 * 
 * By default, all the values of the properties are false.
 * 
 * @author Thierry Templier
 */
public class DefaultResourceManagementDefinition implements ResourceManagementDefinition {
	private boolean indexReaderOpen = false;
	private boolean writeOperationsForIndexWriterAuthorized = false;
	private boolean indexWriterOpen = false;
	private boolean writeOperationsForIndexReaderAuthorized = false;

	public boolean isIndexReaderOpen() {
		return indexReaderOpen;
	}

	public void setIndexReaderOpen(boolean indexReaderOpen) {
		this.indexReaderOpen = indexReaderOpen;
	}

	public boolean isIndexWriterOpen() {
		return indexWriterOpen;
	}

	public void setIndexWriterOpen(boolean indexWriterOpen) {
		this.indexWriterOpen = indexWriterOpen;
	}

	public boolean isWriteOperationsForIndexReaderAuthorized() {
		return writeOperationsForIndexReaderAuthorized;
	}

	public void setWriteOperationsForIndexReaderAuthorized(
			boolean writeOperationsForIndexReaderAuthorized) {
		this.writeOperationsForIndexReaderAuthorized = writeOperationsForIndexReaderAuthorized;
	}

	public boolean isWriteOperationsForIndexWriterAuthorized() {
		return writeOperationsForIndexWriterAuthorized;
	}

	public void setWriteOperationsForIndexWriterAuthorized(
			boolean writeOperationsForIndexWriterAuthorized) {
		this.writeOperationsForIndexWriterAuthorized = writeOperationsForIndexWriterAuthorized;
	}

}
