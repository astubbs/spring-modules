/**
 * Created on Oct 2, 2005
 *
 * $Id: MappingDescriptorFactoryBean.java,v 1.1 2005/10/10 09:27:26 costin Exp $
 * $Revision: 1.1 $
 */
package org.springmodules.jcr.mapping;

import java.io.IOException;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.portals.graffito.jcr.exception.JcrMappingException;
import org.apache.portals.graffito.jcr.mapper.impl.DigesterDescriptorReader;
import org.apache.portals.graffito.jcr.mapper.model.ClassDescriptor;
import org.apache.portals.graffito.jcr.mapper.model.MappingDescriptor;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;

/**
 * Factory bean for loading mapping files. This factory beans can load several file descriptors
 * and assembles them into an overall class descriptor. 
 * 
 * @author Costin Leau
 *
 */
public class MappingDescriptorFactoryBean implements FactoryBean, InitializingBean {

    private static final Log log = LogFactory.getLog(MappingDescriptorFactoryBean.class);

    private MappingDescriptor mappingDescriptor;

    private Resource[] mappings;

    /**
     * @see org.springframework.beans.factory.FactoryBean#getObject()
     */
    public Object getObject() throws Exception {
        return mappingDescriptor;
    }

    /**
     * @see org.springframework.beans.factory.FactoryBean#getObjectType()
     */
    public Class getObjectType() {
        return (this.mappingDescriptor != null) ? this.mappingDescriptor.getClass() : ClassDescriptor.class;
    }

    /**
     * @see org.springframework.beans.factory.FactoryBean#isSingleton()
     */
    public boolean isSingleton() {
        return true;
    }

    /**
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    public void afterPropertiesSet() throws Exception {
        if (mappings == null || mappings.length == 0)
            throw new IllegalArgumentException("at least one mapping file is needed");

        createMappingDescriptor();
    }

    /**
     * Subclasses can extend this method to provide custom behavior when creating 
     * the mapping descriptor
     */
    protected void createMappingDescriptor() throws IOException, JcrMappingException {
        // load the descriptors step by step and concatenate everything in an over-all
        // descriptor
        mappingDescriptor = DigesterDescriptorReader.loadClassDescriptors(mappings[0].getInputStream());
        boolean debug = log.isDebugEnabled();

        for (int i = 1; i < mappings.length; i++) {
            if (mappings[i] != null) {
                MappingDescriptor descriptor = DigesterDescriptorReader.loadClassDescriptors(mappings[i].getInputStream());
                for (Iterator iter = descriptor.getClassDescriptors().iterator(); iter.hasNext();) {
                    mappingDescriptor.addClassDescriptor((ClassDescriptor) iter.next());
                }
            }
        }
    }

    /**
     * @return Returns the descriptors.
     */
    public Resource[] getMappings() {
        return mappings;
    }

    /**
     * @param descriptors The descriptors to set.
     */
    public void setMappings(Resource[] descriptors) {
        this.mappings = descriptors;
    }

}
