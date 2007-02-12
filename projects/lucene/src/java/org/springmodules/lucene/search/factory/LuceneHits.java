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

package org.springmodules.lucene.search.factory;

import java.io.IOException;
import java.util.Iterator;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.Hits;

/**
 * Interface representing the contract of the Lucene Hits class. It
 * allows unit tests with this resource.
 *  
 * All the method of the Hits class are present in this interface
 * and, so allow to make all the operations of this class. 
 *  
 * @author Thierry Templier
 * @see Hits
 */
public interface LuceneHits {
    int length();
    Document doc(int n) throws IOException;
	float score(int n) throws IOException;
	int id(int n) throws IOException;
	Iterator iterator();
}
