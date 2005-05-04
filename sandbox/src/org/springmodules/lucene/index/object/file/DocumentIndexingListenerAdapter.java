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

/**
 * @author Thierry Templier
 */
public class DocumentIndexingListenerAdapter implements DocumentIndexingListener {

	/**
	 * @see org.springmodules.lucene.index.object.file.DocumentIndexingListener#beforeIndexingDirectory(java.io.File)
	 */
	public void beforeIndexingDirectory(File file) {
	}

	/**
	 * @see org.springmodules.lucene.index.object.file.DocumentIndexingListener#afterIndexingDirectory(java.io.File)
	 */
	public void afterIndexingDirectory(File file) {
	}

	/**
	 * @see org.springmodules.lucene.index.object.file.DocumentIndexingListener#beforeIndexingFile(java.io.File)
	 */
	public void beforeIndexingFile(File file) {
	}

	/**
	 * @see org.springmodules.lucene.index.object.file.DocumentIndexingListener#afterIndexingFile(java.io.File)
	 */
	public void afterIndexingFile(File file) {
	}

	/**
	 * @see org.springmodules.lucene.index.object.file.DocumentIndexingListener#onErrorIndexingFile(java.io.File, java.lang.Exception)
	 */
	public void onErrorIndexingFile(File file, Exception ex) {
	}

	/**
	 * @see org.springmodules.lucene.index.object.file.DocumentIndexingListener#onNotAvailableHandler(java.io.File)
	 */
	public void onNotAvailableHandler(File file) {
	}

}
