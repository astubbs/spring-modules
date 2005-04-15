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

package org.springmodules.lucene.search.object;

import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.springmodules.lucene.search.core.LuceneSearchTemplate;
import org.springmodules.lucene.search.factory.SearcherFactory;

/**
 * @author Thierry Templier
 */
public abstract class LuceneSearchQuery {
	private LuceneSearchTemplate template = new LuceneSearchTemplate();

	/**
	 * @return
	 */
	public LuceneSearchTemplate getTemplate() {
		return template;
	}

	/**
	 * @param analyzer
	 */
	public void setAnalyzer(Analyzer analyzer) {
		if( template==null ) {
			throw new IllegalArgumentException("template must not be null");
		}
		this.template.setAnalyzer(analyzer);
	}

	/**
	 * @param factory
	 */
	public void setSearcherFactory(SearcherFactory factory) {
		if( template==null ) {
			throw new IllegalArgumentException("template must not be null");
		}
		this.template.setSearcherFactory(factory);
	}

	public abstract List search(String textToSearch);
}
