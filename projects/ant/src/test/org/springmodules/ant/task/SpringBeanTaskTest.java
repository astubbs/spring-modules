package org.springmodules.ant.task;

import junit.framework.TestCase;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;

/**
 * Unit tests for the spring bean task. In practice it only works if ANT is
 * called with a CLASSPATH pre-loaded with all the application classes, so that
 * the Spring beans are reachable.
 * 
 * The only way to run this task seems to be to wrap it in an exec task with the
 * classpath set via an env entry. There is a general purpose task called
 * "runant" in this project's build.xml which does this. It takes the value of
 * the property "runant.target" and runs that target in a sub process.
 * 
 * @author dsyer
 * 
 */
public class SpringBeanTaskTest extends TestCase {

	SpringBeanTask task = new SpringBeanTask();

	Project project = new Project();

	protected void setUp() throws Exception {
		super.setUp();
		task.setProject(project);
	}

	/*
	 * Test method for 'org.springmodules.ant.task.SpringBeanTask.execute(Date)'
	 */
	public void testExecuteFailNoBean() throws Exception {
		try {
			task.execute();
			fail("Expected BuildException - there is no default bean name");
		} catch (BuildException e) {
			// expected
			assertTrue("The bean name was not null", e.getMessage().endsWith(
					"null bean name"));
		}
	}

	/*
	 * Test method for 'org.springmodules.ant.task.SpringBeanTask.execute(Date)'
	 */
	public void testExecuteWithNoExpression() throws Exception {
		try {
			task.setName("properties");
			task.execute();
			fail("Expected BuildException - there is no default expression");
		} catch (BuildException e) {
			// expected
			assertTrue("The build stopped for the wrong reason", e.getMessage()
					.startsWith("Cannot execute"));
		}
	}

	/*
	 * Test method for 'org.springmodules.ant.task.SpringBeanTask.execute(Date)'
	 */
	public void testExecuteFailBadExpression() throws Exception {
		try {
			task.setFactoryKey("test.bootstrap");
			task.setName("properties");
			task.setExpression("foo'");
			task.execute();
			fail("Expected BuildException - the expression is rubbish");
		} catch (BuildException e) {
			// expected
			assertTrue(e.getMessage().startsWith("Invalid OGNL"));
		}
	}

	/*
	 * Test method for 'org.springmodules.ant.task.SpringBeanTask.execute(Date)'
	 */
	public void testExecuteSunnyDay() throws Exception {
		task.setFactoryKey("test.bootstrap");
		task.setName("properties");
		task.setExpression("get('test.name')");
		task.setProperty("foo");
		task.execute();
		String result = task.getProject().getUserProperty("foo");
		assertNotNull("Null result of computation", result);
		assertEquals("Wrong result", "springmodules-ant", result);
	}

	/*
	 * Test method for 'org.springmodules.ant.task.SpringBeanTask.execute(Date)'
	 */
	public void testExecuteWithOgnlContext() throws Exception {
		task.setFactoryKey("test.bootstrap");
		task.setName("properties");
		// we can refer to the ant project as #project
		task.setExpression("#project.setUserProperty('bar', get('test.name'))");
		task.execute();
		String result = task.getProject().getUserProperty("bar");
		assertNotNull("Null result of computation", result);
		assertEquals("Wrong result", "springmodules-ant", result);
	}

}
