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
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.BeanInitializationException;

/**
 * Convenience class for working with Apache Lucene. Provides callbacks for
 * resource handling and convenience methods for common operations.
 * <p/>
 * The LuceneTemplate will make use of transactionally bound lucene components
 */
public class LuceneTemplate implements InitializingBean
{
    private Directory directory;
    private Analyzer analyzer;
    private boolean optimize = true;

    /**
     * The only state in a LuceneTemplate is the <code>optimize</code> property. It is safe
     * to use this in a multi-threaded environment (ie, as a singleton) as long as the optimize
     * property is not manipulated.
     *
     * @param directory directory containing index
     * @param analyzer  Analyzer to be used for all operations
     */
    public LuceneTemplate(Directory directory, Analyzer analyzer)
    {
        this.directory = directory;
        this.analyzer = analyzer;
    }

    /**
     * The only state in a LuceneTemplate is the <code>optimize</code> property. It is safe
     * to use this in a multi-threaded environment (ie, as a singleton) as long as the optimize
     * property is not manipulated.
     */
    public LuceneTemplate()
    {

    }

    /**
     * Provide resource handling for a Lucene IndexWriter usage
     *
     * @param callback will be executed with an open index writer.
     */
    public void withWriter(final WriterCallback callback)
    {
        IndexWriter writer = null;
        try
        {
            writer = LuceneUtils.getIndexWriter(directory, analyzer);
            try
            {
                callback.withWriter(writer);
            }
            catch (Exception e)
            {
                throw new RuntimeException("Exception thrown from template callback", e);
            }

        }
        finally
        {
            if (writer != null)
            {
                LuceneUtils.closeIfNecessary(directory, writer);
            }
        }
    }

    /**
     * Add several documents to the index
     */
    public void addDocuments(final Document[] documents)
    {
        withWriter(new WriterCallback()
        {
            public void withWriter(IndexWriter writer) throws Exception
            {
                for (int i = 0; i < documents.length; i++)
                {
                    writer.addDocument(documents[i]);
                }
                if (optimize) writer.optimize();
            }
        });
    }

    /**
     * Add a single document to the index
     */
    public void addDocument(Document d)
    {
        this.addDocuments(new Document[]{d});
    }

    /**
     * Convenience method for executing a query and returning documents from the index.
     *
     * @param query   The query to execute
     * @param maxDocs the maximum number of documents to return
     * @return the default ordering of documents
     */
    public Document[] searchForDocuments(final Query query, final int maxDocs)
    {
        return (Document[]) withSearcher(new SearcherCallback()
        {
            public Object withSearcher(Searcher searcher) throws Exception
            {
                Hits h = searcher.search(query);
                int max = maxDocs < h.length() ? maxDocs : h.length();
                Document[] docs = new Document[max];
                for (int i = 0; i != max; ++i)
                {
                    docs[i] = h.doc(i);
                }
                return docs;
            }
        });
    }

    /**
     * Convenience method for executing a query and returning documents from the index.
     *
     * @param query   The query to execute
     * @return the default ordering of documents
     */
    public Document[] searchForDocuments(final Query query)
    {
        return searchForDocuments(query, Integer.MAX_VALUE);
    }

    /**
     * Provide resource handling for a Lucene Searcher usage
     *
     * @param callback will be executed with an open seacrher.
     * @return value returned by the callback
     */
    public Object withSearcher(SearcherCallback callback)
    {
        Searcher searcher = null;
        try
        {

            searcher = LuceneUtils.getSearcher(directory);

            try
            {
                return callback.withSearcher(searcher);
            }
            catch (Exception e)
            {
                throw new RuntimeException("Exception thrown from callback", e);
            }
        }
        finally
        {
            if (searcher != null)
            {

                LuceneUtils.closeIfNecessary(directory, searcher);
            }
        }
    }

    /**
     * Provide resource handling for a Lucene Reader usage
     *
     * @param callback will be executed with an open index reader.
     * @return value returned by the callback
     */
    public Object withIndexReader(ReaderCallback callback)
    {
        IndexReader reader = null;
        try
        {
            reader = LuceneUtils.getIndexReader(directory);
            try
            {
                return callback.withReader(reader);
            }
            catch (Exception e)
            {
                throw new RuntimeException("Exception thrown from callback", e);
            }
        }
        finally
        {
            if (reader != null)
            {
                LuceneUtils.closeIfNecessary(directory, reader);
            }
        }
    }

    /**
     * Parse a query using the Lucene QueryParser and the analyzer registered with the template
     * @param query the query text
     * @param defaultField the default field name to use for the query
     * @return the full query instance
     */
    public Query parseQuery(String query, String defaultField)
    {
        try
        {
            return QueryParser.parse(query, defaultField, analyzer);
        }
        catch (ParseException e)
        {
            throw new RuntimeException("Unable to parse query [" + query + "]", e);
        }
    }

    /**
     * Should the template aggressively optimize indexes by optimizing
     * after document additions? The default is <code>true</code>
     *
     * @param optimize set to false to disable automatic optimizing
     */
    public void setOptimize(boolean optimize)
    {
        this.optimize = optimize;
    }

    /**
     * Must set if not specified in the constructor
     */
    public void setAnalyzer(Analyzer analyzer)
    {
        this.analyzer = analyzer;
    }

    /**
     * Must set if not specified in the constructor
     */
    public void setDirectory(Directory directory)
    {
        this.directory = directory;
    }

    /**
     * Validation of required properties
     */
    public void afterPropertiesSet() throws Exception
    {
        if (directory == null) throw new BeanInitializationException("Must specify a directory property");
        if (analyzer == null) throw new BeanInitializationException("Must specify an analyzer property");
    }
}
