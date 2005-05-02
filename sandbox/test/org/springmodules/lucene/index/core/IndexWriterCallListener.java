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
 * Event interface to detect the index writer closes.
 * 
 * @author Thierry Templier
 */
public interface IndexWriterCallListener {
	public int getNumberWritersCreated();
	public void writerCreated();

	public boolean isWriterClosed();
	public void indexWriterClosed();

	public boolean isIndexWriterOptimize();
	public void indexWriterOptimize();

	public void indexWriterAddDocument();
	public int getIndexWriterAddDocuments();
}
