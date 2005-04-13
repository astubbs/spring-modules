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

import org.apache.lucene.index.IndexWriter;

/**
 * For use with the LuceneTemplate
 */
public interface WriterCallback
{
    /**
     * An open IndexWriter will be passed in, and should be managed by the LuceneTemplate. In other words,
     * let the framework close it for you, please.
     *
     * @param writer an open IndexWriter
     * @throws Exception which will be wrapped in a RuntimeException and re-thrown
     */
    void withWriter(IndexWriter writer) throws Exception;
}
