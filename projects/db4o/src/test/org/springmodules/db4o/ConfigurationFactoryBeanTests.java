/*
 * Copyright 2002-2007 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springmodules.db4o;

import java.lang.reflect.Field;

import junit.framework.TestCase;

import org.easymock.MockControl;

import com.db4o.Db4o;
import com.db4o.config.Configuration;

/**
 * @author Costin Leau
 * 
 */
public class ConfigurationFactoryBeanTests extends TestCase {

	ConfigurationFactoryBean cfb;

	Configuration config;

	/*
	 * (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		cfb = new ConfigurationFactoryBean();
		Db4o.configure().optimizeNativeQueries(true);
	}

	/*
	 * (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		cfb = null;
		config = null;
	}

	/**
	 * Test method for
	 * {@link org.springmodules.orm.db4o.ConfigurationFactoryBean#afterPropertiesSet()}.
	 */
	public void testAfterPropertiesSet() {

	}

	public void testCreateConfigurationWithExistingConfiguration() throws Exception {
		Configuration mockConfiguration = (Configuration) MockControl.createControl(Configuration.class).getMock();
		cfb.setConfiguration(mockConfiguration);
		cfb.afterPropertiesSet();

		assertSame("a different configuration was created", mockConfiguration, cfb.getObject());
	}

	/**
	 * Test method for
	 * {@link org.springmodules.orm.db4o.ConfigurationFactoryBean#createConfiguration(int)}.
	 */
	public void testCreateNewConfiguration() {
		// change the default configuration and make sure the default is not
		// propagated
		// override default
		Db4o.configure().optimizeNativeQueries(false);
		Configuration cfg = cfb.createConfiguration(ConfigurationFactoryBean.CONFIGURATION_NEW);
		assertTrue(cfg.optimizeNativeQueries());
	}

	/**
	 * Test method for
	 * {@link org.springmodules.orm.db4o.ConfigurationFactoryBean#createConfiguration(int)}.
	 */

	public void testCreateClonedConfiguration() {
		// override default
		Db4o.configure().optimizeNativeQueries(true);
		Configuration cfg = cfb.createConfiguration(ConfigurationFactoryBean.CONFIGURATION_CLONED);
		assertTrue(cfg.optimizeNativeQueries());
		// override default again
		Db4o.configure().optimizeNativeQueries(false);
		assertTrue(cfg.optimizeNativeQueries());
	}

	/**
	 * Test method for
	 * {@link org.springmodules.orm.db4o.ConfigurationFactoryBean#createConfiguration(int)}.
	 */
	public void testCreateJVMConfiguration() {
		assertSame(Db4o.configure(), cfb.createConfiguration(ConfigurationFactoryBean.CONFIGURATION_GLOBAL));
	}

	public void testCreateInvalidConfiguration() {
		try {
			cfb.createConfiguration(123);
			fail("should have thrown exception");
		}
		catch (IllegalArgumentException iae) {
			// expected
		}
	}

	/**
	 * Test method for
	 * {@link org.springmodules.orm.db4o.ConfigurationFactoryBean#setConfigurationCreationMode(java.lang.String)}.
	 */
	public void testSetConfigurationCreationModeInvalid() {
		try {
			cfb.setConfigurationCreationMode("bla-bla");
			fail("should have thrown exception");
		}
		catch (IllegalArgumentException iae) {
			// expected
		}
	}

	public void testSetConfigurationCreationModeWOPrefix() throws Exception {
		cfb.setConfigurationCreationMode("new");
		assertConfigMode(ConfigurationFactoryBean.CONFIGURATION_NEW);
		cfb.setConfigurationCreationMode("GlobAL");
		assertConfigMode(ConfigurationFactoryBean.CONFIGURATION_GLOBAL);
	}

	public void testSetConfigurationCreationModeWPrefix() throws Exception {
		cfb.setConfigurationCreationMode("CONFIGURATION_new");
		assertConfigMode(ConfigurationFactoryBean.CONFIGURATION_NEW);
		cfb.setConfigurationCreationMode("configuration_CloNED");
		assertConfigMode(ConfigurationFactoryBean.CONFIGURATION_CLONED);
	}

	/**
	 * Test method for
	 * {@link org.springmodules.orm.db4o.ConfigurationFactoryBean#setConfigurationCreationModeNumber(int)}.
	 */
	public void testSetConfigurationCreationModeNumber() throws Exception {
		cfb.setConfigurationCreationModeNumber(ConfigurationFactoryBean.CONFIGURATION_CLONED);
		assertConfigMode(ConfigurationFactoryBean.CONFIGURATION_CLONED);
	}

	public void testSetInvalidConfigurationCreationModeNumber() {
		try {
			cfb.setConfigurationCreationModeNumber(1234);
			fail("should have thrown exception");
		}
		catch (IllegalArgumentException iae) {
			// expected
		}
	}

	private void assertFieldValue(String fieldName, Object target, Object assertedValue) throws Exception {
		Field field = target.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);

		assertEquals("incorrect value", assertedValue, field.get(target));
	}

	private void assertConfigMode(int mode) throws Exception {
		assertFieldValue("configurationCreationMode", cfb, new Integer(mode));
	}
}
