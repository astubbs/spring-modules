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

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Provides for transactionally bound write-behind index additions
 */
class IndexWriterProxy extends IndexWriter
{
    private List addedDocuments = new ArrayList();
    private boolean flushing = false;

    IndexWriterProxy(Directory directory, Analyzer analyzer, boolean b) throws IOException
    {
        super(directory, analyzer, b);
    }

    public synchronized void close() throws IOException
    {
        this.flushDocuments();
        super.close();
    }

    public void addDocument(final Document document) throws IOException
    {
        if (flushing)
        {
            super.addDocument(document);
        }
        else
        {
            addedDocuments.add(new Adder()
            {
                public void add() throws IOException
                {
                    IndexWriterProxy.super.addDocument(document);
                }
            });
        }
    }

    public void addDocument(final Document document, final Analyzer analyzer) throws IOException
    {
        if (flushing)
        {
            super.addDocument(document, analyzer);
        }
        else
        {
            addedDocuments.add(new Adder()
            {
                public void add() throws IOException
                {
                    IndexWriterProxy.super.addDocument(document, analyzer);
                }
            });
        }
    }

    void flushDocuments()
    {
        this.flushing = true;
        for (Iterator iterator = addedDocuments.iterator(); iterator.hasNext();)
        {
            IndexWriterProxy.Adder adder = (IndexWriterProxy.Adder) iterator.next();
            try
            {
                adder.add();
            }
            catch (IOException e)
            {
                throw new RuntimeException("IOException adding document during flush", e);
            }
            iterator.remove();
        }
        this.flushing = false;
    }

    void clearDocuments()
    {
        this.addedDocuments.clear();
    }

    /**
     * lazily add documents to handle polymorphism between the different addDocument(..) overloads
     */
    private static interface Adder
    {
        public void add() throws IOException;
    }
}
