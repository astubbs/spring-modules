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
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

public class TransactionIntegrationTests extends TestCase
{
    private MockTransactionManager manager;
    private TransactionTemplate tx;
    private Directory directory;
    private Analyzer analyzer;
    private LuceneTemplate lucene;

    public void setUp() throws Exception
    {
        manager = new MockTransactionManager(false, true);
        tx = new TransactionTemplate();
        tx.setTransactionManager(manager);
        tx.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        tx.afterPropertiesSet();

        directory = new RAMDirectory();
        analyzer = new SimpleAnalyzer();
        IndexWriter writer = new IndexWriter(directory, analyzer, true);
        writer.close();

        lucene = new LuceneTemplate(directory, analyzer);
    }


    public void testWriteOnTxCommit() throws Exception
    {
        tx.execute(new TransactionCallback()
        {
            public Object doInTransaction(TransactionStatus status)
            {
                Document doc = new Document();
                doc.add(Field.Text("name", "Brian McCallister"));
                lucene.addDocument(doc);
                return null;
            }
        });

        Document[] docs = lucene.searchForDocuments(QueryParser.parse("Brian", "name", analyzer), 10);
        assertEquals(1, docs.length);
    }

    public void testDontWriteOnTxRollback() throws Exception
    {
        tx.execute(new TransactionCallback()
        {
            public Object doInTransaction(TransactionStatus status)
            {
                Document doc = new Document();
                doc.add(Field.Text("name", "Brian McCallister"));
                lucene.addDocument(doc);
                status.setRollbackOnly();
                return null;
            }
        });

        Document[] docs = lucene.searchForDocuments(QueryParser.parse("Brian", "name", analyzer), 10);
        assertEquals(0, docs.length);
    }

    public void testDeleteOnTxCommit() throws Exception
    {
        Document one = new Document();
        one.add(Field.Text("name", "Bob"));
        Document two = new Document();
        two.add(Field.Text("name", "Jane"));
        lucene.addDocuments(new Document[]{one, two});

        tx.execute(new TransactionCallback()
        {
            public Object doInTransaction(TransactionStatus status)
            {
                return lucene.withIndexReader(new ReaderCallback()
                {
                    public Object withReader(IndexReader reader) throws Exception
                    {
                        reader.delete(0);
                        return null;
                    }
                });
            }
        });

        IndexReader reader = IndexReader.open(directory);
        assertEquals(1, reader.numDocs());
        reader.close();
    }

    public void testDontDeleteOnTxRollback() throws Exception
    {
        Document one = new Document();
        one.add(Field.Text("name", "Bob"));
        Document two = new Document();
        two.add(Field.Text("name", "Jane"));
        lucene.addDocuments(new Document[]{one, two});

        tx.execute(new TransactionCallback()
        {
            public Object doInTransaction(final TransactionStatus status)
            {
                return lucene.withIndexReader(new ReaderCallback()
                {
                    public Object withReader(IndexReader reader) throws Exception
                    {
                        reader.delete(0);
                        status.setRollbackOnly();
                        return null;
                    }
                });
            }
        });

        IndexReader reader = IndexReader.open(directory);
        assertEquals(2, reader.numDocs());
        reader.close();
    }

    public void testDeleteIsolation() throws Exception
    {
        Document one = new Document();
        one.add(Field.Text("name", "Bob"));
        Document two = new Document();
        two.add(Field.Text("name", "Jane"));
        lucene.addDocuments(new Document[]{one, two});

        IndexReader reader = IndexReader.open(directory);
        reader.delete(0);

        tx.execute(new TransactionCallback()
        {
            public Object doInTransaction(final TransactionStatus status)
            {
                return lucene.withIndexReader(new ReaderCallback()
                {
                    public Object withReader(IndexReader reader) throws Exception
                    {
                        assertEquals(2, reader.numDocs());
                        status.setRollbackOnly();
                        return null;
                    }
                });
            }
        });

        reader.close();
        reader = IndexReader.open(directory);
        assertEquals(1, reader.numDocs());
        reader.close();
    }
}
