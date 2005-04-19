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

import java.io.IOException;

import org.apache.lucene.document.Document;

/**
 * Interface to implement in order to specify the way to
 * create a Lucene document. Some index template methods
 * use it to add documents in an index.
 * 
 * @author Thierry Templier
 */
public interface DocumentCreator {
	/**
	 * @return
	 */
	Document createDocument() throws IOException;
}
