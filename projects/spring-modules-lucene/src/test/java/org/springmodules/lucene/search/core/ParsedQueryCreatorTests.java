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

package org.springmodules.lucene.search.core;

import junit.framework.TestCase;

import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.search.Query;

public class ParsedQueryCreatorTests extends TestCase {

	public void testParsedQueryCreatorOneToken() throws Exception {
		ParsedQueryCreator creator = new ParsedQueryCreator() {
			public QueryParams configureQuery() {
				QueryParams params = new QueryParams(
						new String[] { "token1" }, "a text");
				return params;
			}
		};
		
		Query query = creator.createQuery(new SimpleAnalyzer());
		assertNotNull(query);
	}

	public void testParsedQueryCreatorSeveralTokens() throws Exception {
		ParsedQueryCreator creator = new ParsedQueryCreator() {
			public QueryParams configureQuery() {
				QueryParams params = new QueryParams(
						new String[] { "token1", "token2", "token3" }, "a text");
				return params;
			}
		};
		
		Query query = creator.createQuery(new SimpleAnalyzer());
		assertNotNull(query);
	}
}
