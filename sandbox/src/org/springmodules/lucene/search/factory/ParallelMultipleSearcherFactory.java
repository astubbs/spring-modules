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
import java.util.Iterator;
import java.util.List;

import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ParallelMultiSearcher;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.store.Directory;

/**
 * Searcher factory on several indexes for parallel searchs.
 * 
 * @author Thierry Templier
 */
public class ParallelMultipleSearcherFactory implements SearcherFactory {
	private List directories;

	public ParallelMultipleSearcherFactory() {
	}

	public ParallelMultipleSearcherFactory(List directories) {
		this.directories=directories;
	}

	/**
	 * @see org.springmodules.lucene.search.SearcherFactory#getSearcher()
	 */
	public Searcher getSearcher() throws IOException {
		Searcher[] searchers=new Searcher[directories.size()];
		int cpt=0;
		for(Iterator iterator=directories.iterator();iterator.hasNext();) {
			searchers[cpt]=new IndexSearcher((Directory)iterator.next());
			cpt++; 
		}
		return new ParallelMultiSearcher(searchers);
	}


	/**
	 * @return
	 */
	public List getDirectories() {
		return directories;
	}

	/**
	 * @param directory
	 */
	public void setDirectories(List directories) {
		this.directories = directories;
	}

}
