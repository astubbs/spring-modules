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

import org.apache.lucene.search.Searcher;

/**
 * Searcher holder, wrapping a Searcher.
 *
 * <p>LuceneSearcherResourceManager binds instances of this class
 * to the thread, for a given SercherFactory.
 *
 * <p>Note: This is an SPI class, not intended to be used by applications.
 *
 * @author Thierry Templier
 */
public class SearcherHolder {

	private LuceneSearcher searcher;

	public SearcherHolder(LuceneSearcher searcher) {
		this.searcher = searcher;
	}

	public LuceneSearcher getSearcher() {
		return this.searcher;
	}

	public void setSearcher(LuceneSearcher searcher) {
		this.searcher = searcher;
	}

}
