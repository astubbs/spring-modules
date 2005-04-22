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
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.store.Directory;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Searcher;

public class LuceneTemplateTests extends TestCase
{
    private Analyzer analyzer;
    private Directory directory;
    private LuceneTemplate template;

    public void setUp() throws Exception
    {
        analyzer = new SimpleAnalyzer();
        directory = new RAMDirectory();
        template = new LuceneTemplate(directory, analyzer);

        IndexWriter writer = new IndexWriter(directory, analyzer, true);
        writer.close();
    }

    public void testWriterCallback() throws Exception
    {
        template.withWriter(new WriterCallback()
        {
            public void withWriter(IndexWriter writer) throws Exception
            {
                Document d = new Document();
                d.add(Field.Text("name", "Brian McCallister"));
                writer.addDocument(d);
            }
        });

        IndexReader r = IndexReader.open(directory);
        assertEquals(1, r.numDocs());
        r.close();
    }

    public void testAddDocuments() throws Exception
    {
        Document d = new Document();
        d.add(Field.Text("name", "Brian McCallister"));
        template.addDocuments(new Document[] { d });

        IndexReader r = IndexReader.open(directory);
        assertEquals(1, r.numDocs());
        r.close();
    }

    public void testAddDocument() throws Exception
    {
        Document d = new Document();
        d.add(Field.Text("name", "Brian McCallister"));
        template.addDocument(d);

        IndexReader r = IndexReader.open(directory);
        assertEquals(1, r.numDocs());
        r.close();
    }

    public void testSimpleSearch() throws Exception
    {
        Document brian = new Document();
        brian.add(Field.Text("name", "Brian McCallister"));

        Document eric = new Document();
        eric.add(Field.Text("name", "Eric McCallister"));

        Document keith = new Document();
        keith.add(Field.Text("name", "Keith McCallister"));

        template.addDocuments(new Document[] {brian, keith, eric});

        Query query = QueryParser.parse("McCallister", "name", analyzer);
        Document[] docs = template.searchForDocuments(query, 2);
        assertEquals(2, docs.length);
    }

    public void testSearcherCallback() throws Exception
    {
        final boolean[] called = {false};
        template.withSearcher(new SearcherCallback() {

            public Object withSearcher(Searcher searcher) throws Exception
            {
                called[0] = true;
                assertNotNull(searcher);
                return null;
            }
        });
        assertTrue(called[0]);
    }

    public void testReaderCallback() throws Exception
    {
        final boolean[] called = {false};
        template.withIndexReader(new ReaderCallback() {

            public Object withReader(IndexReader searcher) throws Exception
            {
                called[0] = true;
                assertNotNull(searcher);
                return null;
            }
        });
        assertTrue(called[0]);
    }

}
