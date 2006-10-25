package org.springmodules.ant.type;

import junit.framework.TestCase;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.springmodules.ant.util.BeanFactoryLoader;

public class SpringBeanTest extends TestCase {
	SpringBean bean = new SpringBean();

	Project project = new Project();

	protected void setUp() throws Exception {
		super.setUp();
		bean.setProject(project);
	}

	/**
	 * Check that correct exception is thrown.
	 * @throws Exception
	 */
	public void testObjectWrongBeanName() throws Exception {
		bean.setContextRef(BeanFactoryLoader.DEFAULT_CONTEXT_REF);
		bean.setFactoryKey("test.bootstrap");
		bean.setName("foo");
		try {
			bean.getValue();
		} catch (BuildException e) {
			assertTrue(e.getMessage().startsWith("The BeanFactory does not contain"));
		}
	}
	
	/**
	 * Chekc that a bean can be retrieved from a valid factory.
	 * @throws Exception
	 */
	public void testObjectSunnyDay() throws Exception {
		bean.setContextRef(BeanFactoryLoader.DEFAULT_CONTEXT_REF);
		bean.setFactoryKey("test.bootstrap");
		bean.setName("properties");
		Object value = bean.getValue();
		assertNotNull(value);
		assertEquals(value, bean.getValue());
	}

	/**
	 * Test that toString always returns something.
	 * @throws Exception
	 */
	public void testToString() throws Exception {
		assertNotNull(bean.toString());
		bean.setContextRef(BeanFactoryLoader.DEFAULT_CONTEXT_REF);
		bean.setFactoryKey("test.bootstrap");
		bean.setName("properties");
		assertTrue(bean.toString().indexOf("test.name")>=0);
	}

}
