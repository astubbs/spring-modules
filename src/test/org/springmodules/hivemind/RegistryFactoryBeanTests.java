
package org.springmodules.hivemind;

import java.net.URL;

import junit.framework.TestCase;
import org.apache.hivemind.Registry;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

/**
 * @author Rob Harrop
 * @author Thierry Templier
 */
public class RegistryFactoryBeanTests extends TestCase {

	public static final String HIVEMODULE_CONFIG_CLASSPATH = "org/springmodules/hivemind/hivemodule.xml";

	public void testGetRegistry() throws Exception {
		RegistryFactoryBean bean = getRegistryFactoryBean();
		Registry reg = (Registry) bean.getObject();

		assertNotNull("Registry should not be null", reg);

		bean.destroy();
	}

	public void testRegistryIsSingleton() throws Exception {
		RegistryFactoryBean bean = getRegistryFactoryBean();
		Registry reg1 = (Registry) bean.getObject();
		Registry reg2 = (Registry) bean.getObject();

		assertSame("References do not point to same object", reg1, reg2);

		bean.destroy();
	}

	private RegistryFactoryBean getRegistryFactoryBean() throws Exception {
		RegistryFactoryBean bean = new RegistryFactoryBean();
		bean.afterPropertiesSet();
		return bean;
	}

	public void testRegistryWithConfigLocationClasspath() throws Exception {
		RegistryFactoryBean bean = getRegistryFactoryBeanFromClasspath(HIVEMODULE_CONFIG_CLASSPATH);
		Registry reg = (Registry) bean.getObject();

		reg.getService("org.springmodules.hivemind1.FooServiceThree", FooService.class);
		reg.getService("org.springmodules.hivemind1.FooServiceFour", FooService.class);

		bean.destroy();
	}

	public void testRegistryWithConfigLocationFile() throws Exception {
		URL configUrl = getClass().getClassLoader().getResource(HIVEMODULE_CONFIG_CLASSPATH);
		String configFile = configUrl.getFile();
		RegistryFactoryBean bean = getRegistryFactoryBeanFromFile(configFile);
		Registry reg = (Registry) bean.getObject();

		reg.getService("org.springmodules.hivemind1.FooServiceThree", FooService.class);
		reg.getService("org.springmodules.hivemind1.FooServiceFour", FooService.class);

		bean.destroy();
	}

	public void testRegistryWithConfigLocationUrl() throws Exception {
		URL configUrl = getClass().getClassLoader().getResource(HIVEMODULE_CONFIG_CLASSPATH);
		String configFile = configUrl.getFile();
		RegistryFactoryBean bean = getRegistryFactoryBeanFromUrl("file:" + configFile);
		Registry reg = (Registry) bean.getObject();

		reg.getService("org.springmodules.hivemind1.FooServiceThree", FooService.class);
		reg.getService("org.springmodules.hivemind1.FooServiceFour", FooService.class);

		bean.destroy();
	}

	private RegistryFactoryBean getRegistryFactoryBeanFromClasspath(String configLocation) throws Exception {
		RegistryFactoryBean bean = new RegistryFactoryBean();
		Resource[] configLocations = new Resource[1];
		configLocations[0] = new ClassPathResource(configLocation);
		bean.setConfigLocations(configLocations);
		bean.afterPropertiesSet();
		return bean;
	}

	private RegistryFactoryBean getRegistryFactoryBeanFromFile(String configLocation) throws Exception {
		RegistryFactoryBean bean = new RegistryFactoryBean();
		Resource[] configLocations = new Resource[1];
		configLocations[0] = new FileSystemResource(configLocation);
		bean.setConfigLocations(configLocations);
		bean.afterPropertiesSet();
		return bean;
	}

	private RegistryFactoryBean getRegistryFactoryBeanFromUrl(String configLocation) throws Exception {
		RegistryFactoryBean bean = new RegistryFactoryBean();
		Resource[] configLocations = new Resource[1];
		configLocations[0] = new UrlResource(configLocation);
		bean.setConfigLocations(configLocations);
		bean.afterPropertiesSet();
		return bean;
	}
}
