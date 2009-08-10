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

package org.springmodules.lucene.index.factory;

import org.springmodules.lucene.index.factory.DelegatingIndexFactory;
import org.springmodules.lucene.index.factory.LuceneIndexReader;
import org.springmodules.lucene.index.factory.LuceneIndexWriter;

/**
 * @author Thierry Templier
 */
public class MockIndexFactory extends DelegatingIndexFactory {
	private int callNumberReader = 0;
	private int callNumberWriter = 0;
	
	public LuceneIndexReader getIndexReader() {
		System.out.println("############## getIndexReader");
		callNumberReader++;
		return super.getIndexReader();
	}

	public LuceneIndexWriter getIndexWriter() {
		System.out.println("############## getIndexWriter");
		callNumberWriter++;
		return super.getIndexWriter();
	}

	public int getCallNumberReader() {
		return callNumberReader;
	}

	public int getCallNumberWriter() {
		return callNumberWriter;
	}

}
