/*
 * Copyright 2002-2007 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springmodules.lucene.index.support;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.core.io.Resource;

/**
 * This Spring factory bean configures a Lucene filesystem
 * directory using its location.
 * 
 * <p>The create property can be set to force the index to
 * be created when the directory instance is created. 
 * 
 * @author Brian McCallister
 * @author Thierry Templier
 * @author Rob Moore
 * @see org.apache.lucene.store.FSDirectory
 */
public class FSDirectoryFactoryBean extends AbstractDirectoryFactoryBean {
    private Resource location;
    private boolean create = false;

    /**
     * Return that the type of the directory is FSDirectory
     */
    public Class getObjectType() {
        return FSDirectory.class;
    }

    /**
     * This method constructs a filesystem Lucene directory.
     *  
     * <p>The location property must be set, and be a directory
     */
    public void checkFactoryBeanConfiguration() {
        if (location == null) {
            throw new BeanInitializationException(
                    "Must specify a location property");
        }
    }

    /**
     * This method constructs a filesystem Lucene directory.
     *  
     * <p>The location property must be set, and be a directory
     */
	protected Directory initializeDirectory() throws IOException {
        File locationFile = location.getFile();
        boolean locationExists = locationFile.exists();
        if (!locationExists && !create) {
            throw new BeanInitializationException("location does not exist");
        } else if (locationExists && !locationFile.isDirectory()) {
            throw new BeanInitializationException(
                    "location must be a directory");
        }

        return FSDirectory.getDirectory(locationFile, !locationExists?create:false);
    }

    /**
     * Specify the path on the filesystem to use for this directory storage
     */
    public void setLocation(Resource location) {
        this.location = location;
    }

    /**
     * Same as FSDirectory.getDirectory(File, boolean) boolean argument.
     * 
     * <p>Defaults to false
     *
     * @param create if true, create, or erase any existing contents
     */
    public void setCreate(boolean create) {
        this.create = create;
    }

}
