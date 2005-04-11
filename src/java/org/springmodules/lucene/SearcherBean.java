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

import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.Searchable;
import org.apache.lucene.search.MultiSearcher;
import org.apache.lucene.store.Directory;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * Obtain a new IndexSearcher
 * <p>
 * May either configure it against a single directory, or multiple directories, via the
 * directory or directories proeprties respecitvely.
 */
public class SearcherBean implements FactoryBean, InitializingBean
{
    private Directory directory;
    private Directory[] directories;

    public Object getObject() throws Exception
    {
        if (directory != null)
        {
            return new IndexSearcher(directory);
        }
        else
        {
            Searchable[] searchers = new Searchable[directories.length];
            for (int i = 0; i != directories.length; ++i)
            {
                searchers[i] = new IndexSearcher(directory);
            }
            return new MultiSearcher(searchers);
        }
    }

    public Class getObjectType()
    {
        return Searcher.class;
    }

    /**
     * @return false
     */
    public boolean isSingleton()
    {
        return false;
    }

    public void afterPropertiesSet() throws Exception
    {
        if (directory != null && directories != null)
        {
            throw new BeanInitializationException("May set either the directory or directories property, but not both");
        }
        if (directory == null && directories == null)
        {
            throw new BeanInitializationException("Must set either the directory or directories property");
        }
    }

    public void setDirectory(Directory directory)
    {
        this.directory = directory;
    }

    public void setDirectories(Directory[] directories)
    {
        this.directories = directories;
    }
}
