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
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.store.Directory;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.io.IOException;

/**
 * Utility functions for working with transactionally bound Lucene components
 */
public class LuceneUtils
{
    /**
     * Obtain an open IndexWriter, using the transactionally bound one if it exists.
     *
     * @param directory the directory this IndexWriter is for
     * @param analyzer the analyzer to be used by this IndexWriter
     * @return an open IndexWriter
     * @throws RuntimeException if there is an IOException while opening the index.
     */
    public static IndexWriter getIndexWriter(Directory directory, Analyzer analyzer)
    {
        LuceneTuple tuple = (LuceneTuple) TransactionSynchronizationManager.getResource(directory);
        IndexWriterProxy writer = tuple != null ? tuple.writer : null;
        if (writer == null)
        {
            try
            {
            boolean create = !IndexReader.indexExists(directory);
            writer = new IndexWriterProxy(directory, analyzer, create);
            }
            catch (IOException e)
            {
                throw new RuntimeException("Unable to open a lucene index", e);
            }
            if (TransactionSynchronizationManager.isSynchronizationActive())
            {
                tuple = tuple != null ? tuple : new LuceneTuple();
                tuple.writer = writer;
                TransactionSynchronizationManager.bindResource(directory, tuple);
                TransactionSynchronizationManager.registerSynchronization(new IndexWriterSynchronization(directory, writer));
            }
        }
        return writer;
    }

    /**
     * Obtain an open IndexReader, using the transactionally bound one if it exists.
     *
     * @param directory the directory to be read
     * @return an open IndexReader
     * @throws RuntimeException if there is an IOException while opening the index.
     */
    public static IndexReader getIndexReader(Directory directory)
    {
        LuceneTuple tuple = (LuceneTuple) TransactionSynchronizationManager.getResource(directory);
        IndexReader reader = tuple != null ? tuple.reader : null;
        if (reader == null)
        {
            try
            {
                reader = IndexReader.open(directory);
            }
            catch (IOException e)
            {
                throw new RuntimeException("Unable to open lucene index reader", e);
            }
            if (TransactionSynchronizationManager.isSynchronizationActive())
            {
                tuple = tuple != null ? tuple : new LuceneTuple();
                tuple.reader = reader;
                TransactionSynchronizationManager.bindResource(directory, tuple);
                TransactionSynchronizationManager.registerSynchronization(new IndexReaderSynchronization(directory, reader));
            }
        }
        return reader;
    }


    /**
     * Obtain an open Searcher, using the transactionally bound one if it exists.
     *
     * @param directory the directory to be read
     * @return an open Searcher
     * @throws RuntimeException if there is an IOException while opening the index.
     */
    public static Searcher getSearcher(Directory directory)
    {
        LuceneTuple tuple = (LuceneTuple) TransactionSynchronizationManager.getResource(directory);
        Searcher searcher = tuple != null ? tuple.searcher : null;

        if (searcher == null)
        {
            searcher = new IndexSearcher(getIndexReader(directory));
            if (TransactionSynchronizationManager.isSynchronizationActive())
            {
                tuple = tuple != null ? tuple : new LuceneTuple();
                tuple.searcher = searcher;
                TransactionSynchronizationManager.bindResource(directory, tuple);
                TransactionSynchronizationManager.registerSynchronization(new SearcherSynchronization(directory,
                                                                                                      searcher));
            }
        }
        return searcher;
    }

    /**
     * Close this IndexReader if it is <i>not</i> the transactionally bound one, otherwise defer to the
     * transaction synchronization
     */
    public static void closeIfNecessary(Directory directory, IndexReader reader)
    {
        final LuceneTuple tuple = (LuceneTuple) TransactionSynchronizationManager.getResource(directory);
        if (tuple == null || tuple.reader != reader)
        {
            try
            {
                reader.close();
            }
            catch (IOException e)
            {
                throw new RuntimeException("Unable to close index searcher", e);
            }
        }
    }

    /**
     * Close this IndexWriter if it is <i>not</i> the transactionally bound one, otherwise defer to the
     * transaction synchronization
     */
    public static void closeIfNecessary(Directory directory, IndexWriter writer)
    {
        final LuceneTuple tuple = (LuceneTuple) TransactionSynchronizationManager.getResource(directory);
        if (tuple == null || tuple.writer != writer)
        {
            try
            {
                writer.close();
            }
            catch (IOException e)
            {
                throw new RuntimeException("Unable to close index searcher", e);
            }
        }
    }

    /**
     * Close this Searcher if it is <i>not</i> the transactionally bound one, otherwise defer to the
     * transaction synchronization
     */
    public static void closeIfNecessary(Directory directory, Searcher searcher)
    {
        final LuceneTuple tuple = (LuceneTuple) TransactionSynchronizationManager.getResource(directory);
        if (tuple == null || tuple.searcher != searcher)
        {
            try
            {
                searcher.close();
            }
            catch (IOException e)
            {
                throw new RuntimeException("Unable to close index searcher", e);
            }
        }
    }

//    /**
//     * Flushes out documents to be added to the index which were done as write-behind
//     */
//    static void flushDocuments(IndexWriterProxy writer)
//    {
//        List docs = writer.getAddedDocuments();
//        writer.setFlushing(true);
//        for (Iterator iterator = docs.iterator(); iterator.hasNext();)
//        {
//            IndexWriterProxy.Adder adder = (IndexWriterProxy.Adder) iterator.next();
//            try
//            {
//                adder.add();
//            }
//            catch (IOException e)
//            {
//                throw new RuntimeException("IOException adding document during commit", e);
//            }
//            iterator.remove();
//        }
//        writer.setFlushing(false);
//    }

    /**
     * As all three of these are logically keyed to the directory in the transaction context,
     * the the tuple is used to store them
     */
    private static class LuceneTuple
    {
        Searcher searcher;
        IndexReader reader;
        IndexWriterProxy writer;
    }

    /* Synchronization adaptors */

    private static class SearcherSynchronization extends TransactionSynchronizationAdapter
    {
        private Directory directory;
        private Searcher searcher;

        SearcherSynchronization(Directory directory, Searcher writer)
        {
            this.directory = directory;
            this.searcher = writer;
        }

        public void suspend()
        {
            TransactionSynchronizationManager.unbindResource(directory);
        }

        public void resume()
        {
            TransactionSynchronizationManager.bindResource(directory, searcher);
        }
    }

    private static class IndexReaderSynchronization extends TransactionSynchronizationAdapter
    {
        private Directory directory;
        private IndexReader reader;
        private boolean committed = false;

        IndexReaderSynchronization(Directory directory, IndexReader writer)
        {
            this.directory = directory;
            this.reader = writer;
        }

        public void suspend()
        {
            TransactionSynchronizationManager.unbindResource(directory);
        }

        public void resume()
        {
            TransactionSynchronizationManager.bindResource(directory, reader);
        }

        public void beforeCommit(boolean b)
        {
            committed = true;
        }

        public void beforeCompletion()
        {
            TransactionSynchronizationManager.unbindResource(directory);
            if (!committed)
            {
                if (reader.hasDeletions())
                {
                    try
                    {
                        reader.undeleteAll();
                    }
                    catch (IOException e)
                    {
                        throw new TransactionSystemException("Unable to rollback deletions on transaction", e);
                    }
                }
            }

            try
            {
                reader.close();
            }
            catch (IOException e)
            {
                throw new TransactionSystemException("Failure to close index searcher on transaction close", e);
            }
        }
    }

    private static class IndexWriterSynchronization extends TransactionSynchronizationAdapter
    {
        private Directory directory;
        private IndexWriterProxy writer;

        IndexWriterSynchronization(Directory directory, IndexWriterProxy writer)
        {
            this.directory = directory;
            this.writer = writer;
        }

        public void suspend()
        {
            TransactionSynchronizationManager.unbindResource(directory);
        }

        public void resume()
        {
            TransactionSynchronizationManager.bindResource(directory, writer);
        }

        public void beforeCommit(boolean b)
        {
            try
            {
                // flush write-behind documents
                writer.flushDocuments();
            }
            catch (Exception e)
            {
                throw new TransactionSystemException("IOException adding document during commit", e);
            }
        }

        public void beforeCompletion()
        {
            TransactionSynchronizationManager.unbindResource(directory);
            try
            {
                // Clear before close because if there was a commit, they will have been flushed
                // in beforeCommit(), if it was a rollback they are still there and will be
                // flushed on close()
                writer.clearDocuments();
                writer.close();
            }
            catch (IOException e)
            {
                throw new TransactionSystemException("Failure to close index searcher on transaction close", e);
            }
        }
    }
}
