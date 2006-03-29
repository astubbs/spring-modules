package org.springmodules.jcr.mapping;

import java.util.Collection;
import java.util.Iterator;

import junit.framework.TestCase;

import org.apache.portals.graffito.jcr.mapper.model.ClassDescriptor;
import org.apache.portals.graffito.jcr.mapper.model.MappingDescriptor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class MappingDescriptorFactoryBeanTests extends TestCase {

    MappingDescriptorFactoryBean factory;

    Resource mappingA, mappingB;

    MappingDescriptor mappingDescriptor1, mappingDescriptor2;

    Resource[] res;

    protected void setUp() throws Exception {
        super.setUp();
        factory = new MappingDescriptorFactoryBean();

        mappingA = new ClassPathResource("/org/springmodules/jcr/mapping/mappingA.xml");
        mappingB = new ClassPathResource("/org/springmodules/jcr/mapping/mappingB.xml");

        res = new Resource[] { mappingA, mappingB };
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /*
     * Test method for 'org.springmodules.jcr.mapping.MappingDescriptorFactoryBean.afterPropertiesSet()'
     */
    public void testAfterPropertiesSet() throws Exception {
        try {
            factory.afterPropertiesSet();
            fail("should have thrown IllegalArgumentException");
        } catch (Exception e) {
            // it's okay
        }
        factory.setMappings(res);
        assertSame(res, factory.getMappings());
        factory.afterPropertiesSet();

    }

    /*
     * Test method for 'org.springmodules.jcr.mapping.MappingDescriptorFactoryBean.createMappingDescriptor()'
     */
    public void testCreateMappingDescriptor() throws Exception {

        factory.setMappings(res);
        factory.afterPropertiesSet();

        MappingDescriptor descriptor = (MappingDescriptor) factory.getObject();
        Collection col = descriptor.getClassDescriptors();
        assertEquals(" different number of descriptors ", 2, col.size());
        Iterator iter = col.iterator();
        ClassDescriptor descr = (ClassDescriptor) iter.next();
        assertEquals("wrong class name descriptor ", "org.springmodules.jcr.mapping.A", descr.getClassName());
        descr = (ClassDescriptor) iter.next();
        assertEquals("wrong class name descriptor ", "org.springmodules.jcr.mapping.B", descr.getClassName());
    }

}
