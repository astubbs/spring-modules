package org.springmodules.hivemind;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.apache.hivemind.Registry;
import org.apache.hivemind.impl.RegistryBuilder;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContextException;


/**
 * @author Rob Harrop
 * @author Thierry Templier
 */
public class ServiceFactoryBeanWithConfigLocationTests extends TestCase {
	public static final String HIVEMODULE_CONFIG="classpath:/org/springmodules/hivemind/hivemodule.xml";

    public void testGetServiceByInterface() throws Exception {
        Object service = getService(null, MessageService.class);
        assertNotNull("Service should not be null.", service);
        assertEquals("Incorrect value returned from service", "Hello World!", ((MessageService) service).getMessage());
    }

    public void testGetServiceByNameAndInterface() throws Exception {
        FooService one = (FooService) getService("org.springmodules.hivemind.FooServiceOne", FooService.class);
        FooService two = (FooService) getService("org.springmodules.hivemind.FooServiceTwo", FooService.class);

        assertNotNull("Service one should not be null", one);
        assertNotNull("Service two  should not be null", two);

        assertEquals("Incorrect return value", "one", one.getFoo());
        assertEquals("Incorrect return value", "two", two.getFoo());
    }

    public void testGetNonExistentServiceByInterface() throws Exception {
        try {
            getService(null, FactoryBean.class);
            fail();
        } catch (ApplicationContextException ex) {

        }
    }

    public void testGetNonExistentServiceByNameAndInterface() throws Exception {
        try {
            getService("foo", FactoryBean.class);
            fail();
        } catch (ApplicationContextException ex) {

        }
    }

    public void testWithoutServiceInterface() throws Exception {
        ServiceFactoryBean bean = new ServiceFactoryBean();
        bean.setRegistry(RegistryBuilder.constructDefaultRegistry());

        try {
            bean.afterPropertiesSet();

        } catch (ApplicationContextException ex) {
			ex.printStackTrace();
        }
    }

    public void testWithoutRegistry() throws Exception {
        ServiceFactoryBean bean = new ServiceFactoryBean();
        bean.setServiceInterface(FactoryBean.class);

        try {
            bean.afterPropertiesSet();

        } catch (ApplicationContextException ex) {
			ex.printStackTrace();
        }
    }

    private Object getService(String serviceName, Class serviceInterface) throws Exception {
		RegistryFactoryBean registryBean = new RegistryFactoryBean();
		List configLocations=new ArrayList();
		configLocations.add(HIVEMODULE_CONFIG);
		registryBean.setConfigLocations(configLocations);
		registryBean.afterPropertiesSet();
		Registry reg = (Registry)registryBean.getObject();
		ServiceFactoryBean bean = new ServiceFactoryBean();
		bean.setRegistry(reg);
		bean.setServiceInterface(serviceInterface);
		bean.setServiceName(serviceName);
		bean.afterPropertiesSet();
		return bean.getObject();
	}
}
