package org.springmodules.ant.util;

import junit.framework.TestCase;

import org.apache.tools.ant.BuildException;
import org.springframework.beans.factory.BeanFactory;

public class BeanFactoryLoaderTest extends TestCase {

	/*
	 * Test method for 'org.springmodules.ant.util.BeanFactoryLoader'
	 */
	public void testExecuteFailNoBeanFactory() throws Exception {
		try {
			BeanFactoryLoader.getBeanFactory("foo", "rubbish");
			fail("Expected BuildException - the context ref name is rubbish");
		} catch (BuildException e) {
			// expected
			assertTrue(e.getMessage().startsWith(
					"Cannot locate the bean factory"));
		}
	}

	/*
	 * Test method for 'org.springmodules.ant.util.BeanFactoryLoader'
	 */
	public void testExecuteFailNoContextRef() throws Exception {
		try {
			BeanFactoryLoader.getBeanFactory("rubbish");
			fail("Expected BuildException - the bean factory name is rubbish");
		} catch (BuildException e) {
			// expected
			assertTrue(e.getMessage().startsWith(
					"Cannot locate the bean factory"));
		}
	}

	/*
	 * Test method for 'org.springmodules.ant.util.BeanFactoryLoader'
	 */
	public void testBeanFactoryWithContext() throws Exception {
		BeanFactory factory = BeanFactoryLoader.getBeanFactory("classpath:beanRefContext.xml", "test.bootstrap");
		assertNotNull("Null bean factory test.child", factory);
		assertTrue("Factory does not contain bean", factory.containsBean("properties"));
	}

	/*
	 * Test method for 'org.springmodules.ant.util.BeanFactoryLoader'
	 */
	public void testBeanFactoryDefaultContext() throws Exception {
		BeanFactory factory = BeanFactoryLoader.getBeanFactory("test.bootstrap");
		assertNotNull("Null bean factory test.child", factory);
		assertTrue("Factory does not contain bean", factory.containsBean("properties"));
	}

	/*
	 * Test method for 'org.springmodules.ant.util.BeanFactoryLoader'
	 */
	public void testParentBeanFactory() throws Exception {
		BeanFactory factory = BeanFactoryLoader.getBeanFactory("test.child");
		assertNotNull("Null bean factory test.child", factory);
		assertTrue("Factory does not contain bean", factory.containsBean("properties"));
	}

	/*
	 * Test method for 'org.springmodules.ant.util.BeanFactoryLoader'
	 */
	public void testBeanFactoryCache() throws Exception {
		BeanFactory factory1 = BeanFactoryLoader.getBeanFactory("test.bootstrap");
		BeanFactory factory2 = BeanFactoryLoader.getBeanFactory("test.bootstrap");
		assertEquals("Bean factory not cached", factory1, factory2);
	}
}
