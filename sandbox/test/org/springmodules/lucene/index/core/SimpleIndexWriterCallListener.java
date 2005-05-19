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
public class SimpleIndexWriterCallListener implements IndexWriterCallListener {
	private int numberWritersCreated=0;
	private boolean indexWriterOptimize=false;
	private int indexWriterAddDocuments=0;
	private int writerClosed=0;

	private void initWriterVariables() {
		numberWritersCreated=0;
		indexWriterOptimize=false;
		indexWriterAddDocuments=0;
		writerClosed=0;
	}

	public int getNumberWritersCreated() {
		return numberWritersCreated;
	}

	public void writerCreated() {
		//initWriterVariables();
		numberWritersCreated++;
	}

	public void indexWriterClosed() {
		writerClosed++;
	}

	public int getNumberWritersClosed() {
		return writerClosed;
	}

	public void indexWriterOptimize() {
		indexWriterOptimize=true;
	}

	public boolean isIndexWriterOptimize() {
		return indexWriterOptimize;
	}

	public void indexWriterAddDocument() {
		indexWriterAddDocuments++;
	}


	public int getIndexWriterAddDocuments() {
		return indexWriterAddDocuments;
	}

}
