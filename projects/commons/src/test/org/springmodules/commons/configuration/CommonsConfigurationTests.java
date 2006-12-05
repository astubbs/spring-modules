/**
 * Created on Feb 14, 2006
 *
 * $Id: CommonsConfigurationTests.java,v 1.2 2006/12/05 16:20:13 costin Exp $
 * $Revision: 1.2 $
 */
package org.springmodules.commons.configuration;

import java.io.StringReader;
import java.util.Properties;

import junit.framework.TestCase;

import org.apache.commons.configuration.BaseConfiguration;
import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * Commons Configuration FactoryBean test.
 * @author Costin Leau
 * 
 */
public class CommonsConfigurationTests extends TestCase {

	CommonsConfigurationFactoryBean configurationFactory;

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		configurationFactory = new CommonsConfigurationFactoryBean();
	}

	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
		configurationFactory = null;
	}

	public void testAfterPropertiesSet() throws Exception {
		try {
			configurationFactory.afterPropertiesSet();
			fail("expected exception");
		}
		catch (Exception e) {
			// it's okay
		}
	}

	public void testGetObject() throws Exception {
		configurationFactory.setConfigurations(new Configuration[] { new BaseConfiguration() });
		assertNull(configurationFactory.getObject());
		configurationFactory.afterPropertiesSet();
		assertNotNull(configurationFactory.getObject());
	}

	public void testMergeConfigurations() throws Exception {
		Configuration one = new BaseConfiguration();
		one.setProperty("foo", "bar");
		PropertiesConfiguration two = new PropertiesConfiguration();
		String properties = "## some header \n" + "foo = bar1\n" + "bar = foo\n";
		two.load(new StringReader(properties));

		configurationFactory.setConfigurations(new Configuration[] { one, two });
		configurationFactory.afterPropertiesSet();
		Properties props = (Properties) configurationFactory.getObject();
		assertEquals("foo", props.getProperty("bar"));
		assertEquals("bar", props.getProperty("foo"));
	}

	public void testLoadResources() throws Exception {
		configurationFactory.setLocations(new Resource[] { new ClassPathResource("configuration.file") });
		configurationFactory.setConfigurations(new Configuration[] { new BaseConfiguration() });
		configurationFactory.afterPropertiesSet();
		
		Properties props = (Properties) configurationFactory.getObject();
		assertEquals("satriani", props.getProperty("joe"));
	}
	
	public void testInitialConfiguration() throws Exception {
		configurationFactory = new CommonsConfigurationFactoryBean(new BaseConfiguration());
		configurationFactory.afterPropertiesSet();
		assertNotNull(configurationFactory.getConfiguration());
	}
}
