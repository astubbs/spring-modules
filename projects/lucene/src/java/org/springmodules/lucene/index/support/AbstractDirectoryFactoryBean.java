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
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author Thierry Templier
 * @see org.apache.lucene.store.Directory
 */
public abstract class AbstractDirectoryFactoryBean implements FactoryBean, InitializingBean, DisposableBean {
    private Directory directory;

    /**
	 * Return the directory created by the FactoryBean.
     */
    public final Object getObject() throws Exception {
        return directory;
    }

    /**
     * Specify that directory factory beans allways create singleton.
     */
    public boolean isSingleton() {
        return true;
    }

    /**
     * This method constructs a filesystem Lucene directory.
     *  
     * <p>The location property must be set, and be a directory
     */
    public final void afterPropertiesSet() throws Exception {
    	checkFactoryBeanConfiguration();
    	this.directory = initializeDirectory();
    }

    /**
     * This method checks that the configuration of the concrete
     * sub class of the FactoryBean is correct.
     */
    protected abstract void checkFactoryBeanConfiguration();

    /**
     * This method initializes and returns the directory manages by the
     * FactoryBean.
     *
     * @return the created directory
     */
    protected abstract Directory initializeDirectory() throws IOException;
    
    /**
     * This method close a filesystem Lucene directory.
     */
	public final void destroy() throws Exception {
		if( directory!=null ) {
			directory.close();
		}
	}
}
