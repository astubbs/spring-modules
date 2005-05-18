/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package org.springmodules.lucene.index.support;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * This Spring factory bean configures a Lucene ram
 * directory.

 * @author Brian McCallister
 * @author Thierry Templier
 * @see org.apache.lucene.store.RAMDirectory
 */
public class RAMDirectoryFactoryBean implements FactoryBean, InitializingBean {

    private Directory directory;

    public Object getObject() throws Exception {
        return directory;
    }

    public Class getObjectType() {
        return Directory.class;
    }

    public boolean isSingleton() {
        return true;
    }

	/**
	 * This method constructs a ram Lucene directory.
	 */
    public void afterPropertiesSet() throws Exception {
        directory = new RAMDirectory();
    }

}
