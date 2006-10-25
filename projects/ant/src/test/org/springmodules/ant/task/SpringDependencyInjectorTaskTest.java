package org.springmodules.ant.task;

import java.util.Map;

import junit.framework.TestCase;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springmodules.ant.util.BeanFactoryLoader;

public class SpringDependencyInjectorTaskTest extends TestCase {

	SpringDependencyInjectorTask task = new SpringDependencyInjectorTask();

	Project project = new Project();

	protected void setUp() throws Exception {
		super.setUp();
		task.setProject(project);
	}

	/**
	 * Test that autowiring can be done on a DefaultListableBeanFactory.
	 * @throws Exception
	 */
	public void testAutowireDefaultListableBeanFactory() throws Exception {
		BeanFactory parent = BeanFactoryLoader.getBeanFactory("test.bootstrap");
		DefaultListableBeanFactory context = new DefaultListableBeanFactory();
		context.setParentBeanFactory(parent);
		context.registerSingleton("this", this);
		SpringDependencyInjectorTaskTest test = (SpringDependencyInjectorTaskTest) context.getBean("this");
		context.autowireBeanProperties(test, AutowireCapableBeanFactory.AUTOWIRE_BY_NAME, false);
		assertNotNull("Null test bean", test);
		assertNotNull("Null properties", test.properties);
	}
	
	/**
	 * Test that the task executes and wires a task correctly
	 * @throws Exception
	 */
	public void testDefaultExecute() throws Exception {
		TestTask test = new TestTask();
		test.setProject(project);
		project.addReference("task", test);
		task.setTaskRef("task");
		task.setFactoryKey("test.bootstrap");
		task.execute();
		assertNotNull("Null properties", test.properties);
	}

	/**
	 * Test that the task executes and wires a task correctly by type
	 * @throws Exception
	 */
	public void testAutowireByType() throws Exception {
		TestTask test = new TestTask();
		test.setProject(project);
		project.addReference("task", test);
		task.setTaskRef("task");
		task.setAutowire("byType");
		task.setFactoryKey("test.bootstrap");
		task.execute();
		assertNotNull("Null properties", test.properties);
	}

	/**
	 * Test that the task throws BuildException when task ref is missing
	 * @throws Exception
	 */
	public void testMissingRef() throws Exception {
		task.setTaskRef("task");
		task.setFactoryKey("test.bootstrap");
		try {
			task.execute();
			fail("Expected BuildException when task ref invalid");
		} catch (BuildException e) {
			// expected
		}
	}

	/**
	 * Test that the task throws BuildException when task ref is wrong type
	 * @throws Exception
	 */
	public void testRefWrongClass() throws Exception {
		task.setTaskRef("task");
		task.setFactoryKey("test.bootstrap");
		project.addReference("task", "foo");
		try {
			task.execute();
			fail("Expected BuildException when task ref invalid");
		} catch (BuildException e) {
			// expected
			assertTrue("Wrong message", e.getMessage().startsWith("Reference ("));
		}
	}

	private Map properties;

	/**
	 * Injected during test.
	 * @param properties
	 */
	public void setProperties(Map properties) {
		this.properties = properties;
	}
}
