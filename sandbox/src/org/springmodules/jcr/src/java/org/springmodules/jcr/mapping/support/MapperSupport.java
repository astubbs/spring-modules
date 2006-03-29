/**
 * Created on Oct 10, 2005
 *
 * $Id$
 * $Revision$
 */
package org.springmodules.jcr.mapping.support;

import org.apache.portals.graffito.jcr.exception.JcrMappingException;
import org.apache.portals.graffito.jcr.mapper.Mapper;
import org.apache.portals.graffito.jcr.mapper.model.ClassDescriptor;
import org.apache.portals.graffito.jcr.mapper.model.MappingDescriptor;

/**
 * For some reason the Jcr-mapping framework has an interface and class which contain
 * the same contract: {@link org.apache.portals.graffito.jcr.mapper.Mapper} and
 * {@link org.apache.portals.graffito.jcr.mapper.model.MappingDescriptor} but they 
 * are not related; actually they reside in different packages.
 * Thus if the mapping descriptors are created programatically or by other means
 * it's not possible to use them because a mapper is required.
 * 
 * This class bridges the two making possible to have mapper implementations different
 * then the digester.
 * 
 * @author Costin Leau
 *
 */
public class MapperSupport implements Mapper {

    private MappingDescriptor descriptor;

    public MapperSupport(MappingDescriptor descriptor) {
        this.descriptor = descriptor;
    }

    /**
     * @see org.apache.portals.graffito.jcr.mapper.Mapper#getClassDescriptor(java.lang.Class)
     */
    public ClassDescriptor getClassDescriptor(Class clazz) throws JcrMappingException {
        if (clazz == null)
            throw new IllegalArgumentException("class should be not null");

        return descriptor.getClassDescriptor(clazz.getName());
    }

    /**
     * @return Returns the descriptor.
     */
    public MappingDescriptor getDescriptor() {
        return descriptor;
    }

    /**
     * @param descriptor The descriptor to set.
     */
    public void setDescriptor(MappingDescriptor descriptor) {
        this.descriptor = descriptor;
    }

}
