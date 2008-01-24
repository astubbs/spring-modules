/*
 * Copyright 2002-2005 the original author or authors.
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

import junit.framework.TestCase;

import org.easymock.MockControl;

import com.db4o.config.Configuration;
import com.db4o.config.ObjectClass;
import com.db4o.config.ObjectTranslator;

/**
 * Unit tests to ensure the {@link #ObjectClassFactoryBean()} works correctly.
 *
 * Only way to verify that the explicit config is used is by verifying
 * the relevant methods on ObjectClass are called.
 *
 * @since 0.9
 */
public class ObjectClassFactoryBeanTests extends TestCase {

	@SuppressWarnings("unchecked")
	private Class clazz = String.class;

	private MockControl objectClassControl;
	private ObjectClass objectClass;

	private MockControl configControl;
	private Configuration configuration;

	private ObjectClassFactoryBean ocfb;

	/**
	 * {@inheritDoc}
	 *
	 * Set up an {@link #objectClassControl} and {@link #configControl} and
	 * associated proxies.
	 *
	 * By Default, the constructed {@link #ocfb} will use the
	 * {@link #configuration} which will return the {@link #objectClass}.
	 *
	 * If test methods do not utilise these controls, they must explicitly set
	 * them to be <code>null</code> otherwise {@link #tearDown()} will throw
	 * an exception.
	 */
	@Override
	public void setUp() throws Exception {
		this.objectClassControl = MockControl.createNiceControl(ObjectClass.class);
		this.objectClass = (ObjectClass) this.objectClassControl.getMock();
		this.configControl = MockControl.createNiceControl(Configuration.class);
		this.configuration = (Configuration) this.configControl.getMock();

		this.ocfb = new ObjectClassFactoryBean();

		this.configuration.objectClass(this.clazz);
		this.configControl.expectAndReturn(null, this.objectClass);

		this.ocfb.setClazz(this.clazz);
		this.ocfb.setConfiguration(this.configuration);
		this.configControl.replay();
	}

	/**
	 * {@inheritDoc}
	 *
	 * Verify the mocks have behaved as expected.
	 */
	public void tearDown() {
		if (this.objectClassControl != null) {
			this.objectClassControl.verify();
		}

		if (this.configControl != null) {
			this.configControl.verify();
		}
	}

	public void testMissingClazz() throws Exception {

		// recreate the configuration because the clazz has already been set.
		this.ocfb = new ObjectClassFactoryBean();

		try {
			this.ocfb.afterPropertiesSet();
			fail("Expected IllegalArgumentException because the clazz hasn't been set.");
		} catch (IllegalArgumentException e) {
			// fine, we expected this.
		}

		// now configure it
		this.ocfb.setClazz(this.clazz);
		this.ocfb.afterPropertiesSet();

		// make sure that EasyMock doesn't verify unused mocks
		this.objectClassControl = null;
		this.configControl = null;
	}

	public void testExplicitConfigurationIsUsed() throws Exception {
		/**
		 * This is actually tested in pretty much every other method, but it is
		 * here for completeness.
		 *
		 */

		// construct the objectClass
		ocfb.afterPropertiesSet();

		// make sure that EasyMock doesn't verify unused mocks
		this.objectClassControl = null;
	}

	public void testGetObjectType() throws Exception {
		this.ocfb.afterPropertiesSet();

		assertEquals("factory class is wrong", this.clazz, this.ocfb.getObjectType());

		// turn off EasyMock verification
		this.configControl = null;
		this.objectClassControl = null;
	}

	public void testSingleton() throws Exception {
		this.ocfb.afterPropertiesSet();
		assertFalse("Shouldn't be a singleton", this.ocfb.isSingleton());

		// turn off EasyMock verification
		this.configControl = null;
		this.objectClassControl = null;
	}

	public void testGetObject() throws Exception {
		this.ocfb.afterPropertiesSet();

		// turn off EasyMock verification
		this.objectClassControl = null;

		// construct an empty objectClass. Just make sure it actually works.
		Object object = this.ocfb.getObject();
		assertNotNull("expected at least a not null object", object);
		assertTrue("object is of wrong type", ObjectClass.class
				.isAssignableFrom(object.getClass()));
	}

	public void testDefaultConfigurationIsUsed() throws Exception {
		// err, no way to test this. Could use AOP to intercept the DB4o.config
		// method...

		// bypass EasyMock verification.
		this.objectClassControl = null;
		this.configControl = null;
	}

	public void testCallConstructor() throws Exception {
		Boolean value = Boolean.TRUE;
		this.objectClass.callConstructor(value);
		this.objectClassControl.replay();

		this.ocfb.setCallConstructor(value);
		this.ocfb.afterPropertiesSet();
	}

	public void testCascadeOnActivate() throws Exception {
		Boolean value = Boolean.TRUE;
		this.objectClass.cascadeOnActivate(value);
		this.objectClassControl.replay();

		this.ocfb.setCascadeOnActivate(value);
		this.ocfb.afterPropertiesSet();
	}

	public void testCascadeOnDelete() throws Exception {
		Boolean value = Boolean.TRUE;
		this.objectClass.cascadeOnDelete(value);
		this.objectClassControl.replay();

		this.ocfb.setCascadeOnDelete(value);
		this.ocfb.afterPropertiesSet();
	}

	public void testCascadeOnUpdate() throws Exception {
		Boolean value = Boolean.TRUE;
		this.objectClass.cascadeOnUpdate(value);
		this.objectClassControl.replay();

		this.ocfb.setCascadeOnUpdate(value);
		this.ocfb.afterPropertiesSet();
	}

	public void testEnableReplication() throws Exception {
		Boolean value = Boolean.TRUE;
		this.objectClass.enableReplication(value);
		this.objectClassControl.replay();

		this.ocfb.setEnableReplication(value);
		this.ocfb.afterPropertiesSet();
	}

	public void testGenerateUUIDs() throws Exception {
		Boolean value = Boolean.TRUE;
		this.objectClass.generateUUIDs(value);
		this.objectClassControl.replay();

		this.ocfb.setGenerateUUIDs(value);
		this.ocfb.afterPropertiesSet();
	}

	public void testGenerateVersionNumbers() throws Exception {
		Boolean value = Boolean.TRUE;
		this.objectClass.generateVersionNumbers(value);
		this.objectClassControl.replay();

		this.ocfb.setGenerateVersionNumbers(value);
		this.ocfb.afterPropertiesSet();
	}

	public void testIndexed() throws Exception {
		Boolean value = Boolean.TRUE;
		this.objectClass.indexed(value);
		this.objectClassControl.replay();

		this.ocfb.setIndexed(value);
		this.ocfb.afterPropertiesSet();
	}

	public void testMaximumActivationDepth() throws Exception {
		Integer value = Integer.valueOf(10);
		this.objectClass.maximumActivationDepth(value);
		this.objectClassControl.replay();

		this.ocfb.setMaximumActivationDepth(value);
		this.ocfb.afterPropertiesSet();
	}

	public void testMinimumActivationDepth() throws Exception {
		Integer value = Integer.valueOf(10);
		this.objectClass.minimumActivationDepth(value);
		this.objectClassControl.replay();

		this.ocfb.setMinimumActivationDepth(value);
		this.ocfb.afterPropertiesSet();
	}

	public void testPersistStaticFieldValues() throws Exception {
		this.objectClass.persistStaticFieldValues();
		this.objectClassControl.replay();

		this.ocfb.setPersistStaticFieldValues(true);
		this.ocfb.afterPropertiesSet();
	}

	public void testRenameValue() throws Exception {
		String value = "renamed value";
		this.objectClass.rename(value);
		this.objectClassControl.replay();

		this.ocfb.setRenameValue(value);
		this.ocfb.afterPropertiesSet();
	}

	public void testStoreTransientFields() throws Exception {
		Boolean value = Boolean.TRUE;
		this.objectClass.storeTransientFields(value);
		this.objectClassControl.replay();

		this.ocfb.setStoreTransientFields(true);
		this.ocfb.afterPropertiesSet();
	}

	public void testObjectTranslate() throws Exception {
		ObjectTranslator translator = (ObjectTranslator) MockControl
				.createNiceControl(ObjectTranslator.class).getMock();

		this.objectClass.translate(translator);
		this.objectClassControl.replay();

		this.ocfb.setObjectTranslator(translator);
		this.ocfb.afterPropertiesSet();
	}

	public void testUpdateDepth() throws Exception {
		Integer value = Integer.valueOf(10);
		this.objectClass.updateDepth(value);
		this.objectClassControl.replay();

		this.ocfb.setUpdateDepth(value);
		this.ocfb.afterPropertiesSet();
	}

}
