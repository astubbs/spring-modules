package org.springmodules.hivemind;

import junit.framework.TestCase;
import org.apache.hivemind.Registry;

/**
 * @author Rob Harrop
 */
public class RegistryFactoryBeanTests extends TestCase {

    public void testGetRegistry() throws Exception {
        RegistryFactoryBean bean = getRegistryFactoryBean();
        Registry reg = (Registry)bean.getObject();

        assertNotNull("Registry should not be null", reg);
    }

    public void testRegistryIsSingleton() throws Exception {
        RegistryFactoryBean bean = getRegistryFactoryBean();
        Registry reg1 = (Registry)bean.getObject();
        Registry reg2 = (Registry)bean.getObject();

        assertSame("References do not point to same object", reg1, reg2);
    }

    private RegistryFactoryBean getRegistryFactoryBean() throws Exception {
        RegistryFactoryBean bean = new RegistryFactoryBean();
        bean.afterPropertiesSet();
        return bean;
    }
}
