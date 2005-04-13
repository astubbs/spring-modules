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

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * Creates an in-memory instance of a Lucene directory
 */
public class RAMDirectoryBean implements FactoryBean, InitializingBean
{
    private Directory directory;

    public Object getObject() throws Exception
    {
        return directory;
    }

    public Class getObjectType()
    {
        return Directory.class;
    }

    /**
     * @return true
     */
    public boolean isSingleton()
    {
        return true;
    }

    public void afterPropertiesSet() throws Exception
    {
        directory = new RAMDirectory();
    }
}
