/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package org.springmodules.lucene.index.support;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.BeanInitializationException;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;

public class FSDirectoryFactoryBean implements FactoryBean, InitializingBean
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
        directory = FSDirectory.getDirectory(location,create);
        System.out.println("FSDirectoryFactoryBean.afterPropertiesSet = "+directory);
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
