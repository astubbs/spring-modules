/* Copyright 2005 Brian McCallister
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springmodules.lucene;

import org.apache.lucene.search.Searcher;

/**
 * For use with the LuceneTemplate
 */
public interface SearcherCallback
{
    /**
     * An open Searcher will be passed in, and should be managed by the LuceneTemplate. In other words,
     * let the framework close it for you, please.
     *
     * @param searcher an open IndexReader
     * @return value to be returned by the LuceneTemplate#withSearcher
     * @throws Exception which will be wrapped in a RuntimeException and re-thrown
     */
    public Object withSearcher(Searcher searcher) throws Exception;
}
