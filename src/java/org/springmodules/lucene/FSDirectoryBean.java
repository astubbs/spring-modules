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

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.BeanInitializationException;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;

/**
 * Configures an instance of an FSDirectory
 */
public class FSDirectoryBean implements FactoryBean, InitializingBean
{
    private File location;
    private FSDirectory directory;
    private boolean create = false;

    public Object getObject() throws Exception
    {
        return directory;
    }

    public Class getObjectType()
    {
        return Directory.class;
    }

    /**
     * True
     * 
     * @return true
     */
    public boolean isSingleton()
    {
        return true;
    }

    /**
     * Location must be set, and be a directory
     */
    public void afterPropertiesSet() throws Exception
    {
        if (location == null) throw new BeanInitializationException("Must specify a location property");
        if (!location.isDirectory()) throw new BeanInitializationException("location must be a directory");
        directory = FSDirectory.getDirectory(location, create);
    }

    /**
     * Specify the path on the filesystem to use for this directory storage
     */
    public void setLocation(File location)
    {
        this.location = location;
    }

    /**
     * Same as FSDirectory.getDirectory(File, boolean) boolean argument.
     * <p>
     * Defaults to false
     *
     * @param create if true, create, or erase any existing contents
     */
    public void setCreate(boolean create)
    {
        this.create = create;
    }
}
