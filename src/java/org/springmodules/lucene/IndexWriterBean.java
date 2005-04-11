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
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.Directory;
import org.apache.lucene.analysis.Analyzer;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.BeanInitializationException;

import java.util.prefs.Preferences;

/**
 * Obtain a new IndexWriter
 */
public class IndexWriterBean implements FactoryBean, InitializingBean
{
    private Directory directory;
    private Analyzer analyzer;

    public Object getObject() throws Exception
    {
        boolean create = ! IndexReader.indexExists(directory);
        return new IndexWriter(directory, analyzer, create);
    }

    public Class getObjectType()
    {
        return IndexWriter.class;
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
        if (this.directory == null) throw new BeanInitializationException("directory property required");
        if (this.analyzer == null) throw new BeanInitializationException("analyzer property required");        
    }

    public void setDirectory(Directory directory)
    {
        this.directory = directory;
    }

    public void setAnalyzer(Analyzer analyzer)
    {
        this.analyzer = analyzer;
    }
}
