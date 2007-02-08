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


/**
 * <p>This is the index factory abstraction to get reader and writer
 * instances to work on a Lucene index. These instances can be used
 * to get informations about it or manipulate it with the LuceneIndexTemplate
 * class or with the different indexer classes.
 * 
 * @author Thierry Templier
 */
public interface SmartIndexFactory extends IndexFactory {

	/**
	 * This method determines if the IndexReader should be closed or
	 * not. This check is made by both the Spring Modules's
	 * IndexReaderFactoryUtils and LuceneIndexTemplate classes
	 * @param indexReader the IndexReader to check
	 * @return whether the given index reader should be closed
	 */
	boolean shouldCloseIndexReader(LuceneIndexReader indexReader);
}
