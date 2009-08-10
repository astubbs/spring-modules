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

import java.io.IOException;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

/**
 * This Spring factory bean configures a Lucene RAM
 * directory.

 * @author Brian McCallister
 * @author Thierry Templier
 * @see org.apache.lucene.store.RAMDirectory
 */
public class RAMDirectoryFactoryBean extends AbstractDirectoryFactoryBean {

    /**
     * Return that the type of the directory is RAMDirectory
     */
    public Class getObjectType() {
        return RAMDirectory.class;
    }

    public void checkFactoryBeanConfiguration() {
    }

    /**
	 * This method constructs a RAM Lucene directory.
	 */
	protected Directory initializeDirectory() throws IOException {
        return new RAMDirectory();
    }

}
