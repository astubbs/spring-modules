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

package org.springmodules.lucene.search.factory;

import java.io.IOException;

import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.store.Directory;

/**
 * Searcher factory on a single index.
 * 
 * @author Thierry Templier
 */
public class SimpleSearcherFactory implements SearcherFactory {
	private Directory directory;

	public SimpleSearcherFactory(Directory directory) {
		this.directory=directory;
	}

	/**
	 * @see org.springmodules.lucene.search.SearcherFactory#getSearcher()
	 */
	public Searcher getSearcher() throws IOException {
		return new IndexSearcher(directory);
	}


	/**
	 * @return
	 */
	public Directory getDirectory() {
		return directory;
	}

	/**
	 * @param directory
	 */
	public void setDirectory(Directory directory) {
		this.directory = directory;
	}

}
