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

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.Directory;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.BeanCreationException;

/**
 * Obtain a new IndexReader every time
 */
public class IndexReaderBean implements FactoryBean, InitializingBean
{
    private Directory directory;

    public Object getObject() throws Exception
    {
        if (!IndexReader.indexExists(directory))
        {
            throw new BeanCreationException("no index exists at the directory being read");
        }
        return IndexReader.open(directory);
    }

    public Class getObjectType()
    {
        return IndexReader.class;
    }

    /**
     * @return false
     */
    public boolean isSingleton()
    {
        return false;
    }

    public void setDirectory(Directory directory)
    {
        this.directory = directory;
    }

    public void afterPropertiesSet() throws Exception
    {
        if (this.directory == null) throw new BeanInitializationException("directory property required");
    }
}
