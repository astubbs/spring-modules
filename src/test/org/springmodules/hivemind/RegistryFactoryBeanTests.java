package org.springmodules.hivemind;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.apache.hivemind.Registry;

/**
 * @author Rob Harrop
 */
public class RegistryFactoryBeanTests extends TestCase {
	public static final String HIVEMODULE_CONFIG="classpath:/org/springmodules/hivemind/hivemodule.xml";

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

	public void testRegistryWithConfigLocation() throws Exception {
		RegistryFactoryBean bean = getRegistryFactoryBean(HIVEMODULE_CONFIG);
		Registry reg = (Registry)bean.getObject();
		reg.getService("org.springmodules.hivemind1.FooServiceThree",FooService.class);
		reg.getService("org.springmodules.hivemind1.FooServiceFour",FooService.class);
	}

	private RegistryFactoryBean getRegistryFactoryBean(String configLocation) throws Exception {
		RegistryFactoryBean bean = new RegistryFactoryBean();
		List configLocations=new ArrayList();
		configLocations.add(configLocation);
		bean.setConfigLocations(configLocations);
		bean.afterPropertiesSet();
		return bean;
	}

}
