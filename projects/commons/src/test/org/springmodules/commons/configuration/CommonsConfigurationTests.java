/**
 * Created on Feb 14, 2006
 *
 * $Id: CommonsConfigurationTests.java,v 1.1 2006/02/14 10:55:51 costin Exp $
 * $Revision: 1.1 $
 */
package org.springmodules.commons.configuration;

import java.io.StringReader;
import java.util.NoSuchElementException;
import java.util.Properties;

import junit.framework.TestCase;

import org.apache.commons.configuration.BaseConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;

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
}
