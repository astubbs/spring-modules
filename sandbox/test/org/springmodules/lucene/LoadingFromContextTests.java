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
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class LoadingFromContextTests extends TestCase
{
    private BeanFactory beans;

    public LoadingFromContextTests()
    {
        super();
    }

    public void setUp() throws Exception
    {
        Resource xml = new ClassPathResource("lucene-context.xml", this.getClass());
        beans = new XmlBeanFactory(xml);

        // ensure an index exists
        IndexWriter w = (IndexWriter) beans.getBean("writer");
        w.close();
    }

    public void testFactory() throws Exception
    {
        assertNotNull(beans);
        IndexReader reader = (IndexReader) beans.getBean("reader");
        IndexReader another = (IndexReader) beans.getBean("reader");

        assertNotSame(reader, another);

        assertNotNull(reader);
        reader.close();
        another.close();
    }

    /**
     * Presumes a /tmp directory, so disabled by default
     */
    public void _testLoadFSDir() throws Exception
    {
        Directory d = (Directory) beans.getBean("fsDirectory");
        assertNotNull(d);
    }

    public void testDemonstration() throws Exception
    {
        LuceneTemplate lucene = (LuceneTemplate) beans.getBean("lucene");
        Document one = new Document();
        one.add(Field.Text("name", "Brian McCallister"));

        Document two = new Document();
        two.add(Field.Text("name", "Eric McCallister"));
        lucene.addDocuments(new Document[] {one, two});

        Document[] results = lucene.searchForDocuments(lucene.parseQuery("Eric", "name"));
        assertEquals(1, results.length);
        assertEquals("Eric McCallister", results[0].get("name"));
    }
}
