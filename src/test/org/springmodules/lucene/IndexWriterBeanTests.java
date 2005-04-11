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

import junit.framework.TestCase;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.springmodules.lucene.IndexWriterBean;

public class IndexWriterBeanTests extends TestCase
{
    private IndexWriterBean bean;
    private Directory directory;
    private Analyzer analyzer;
    private IndexWriter writer;

    public void setUp() throws Exception
    {
        directory = new RAMDirectory();
        bean = new IndexWriterBean();
        analyzer = new SimpleAnalyzer();
        bean.setDirectory(directory);
        bean.setAnalyzer(analyzer);
        bean.afterPropertiesSet();
    }

    public void tearDown() throws Exception
    {
        if (writer != null) writer.close();
    }

    public void testGetWriter() throws Exception
    {
        writer = (IndexWriter) bean.getObject();
        assertNotNull(writer);
    }

    public void testDeclaredType() throws Exception
    {
        assertTrue(bean.getObjectType().isAssignableFrom(IndexWriter.class));
    }
}
