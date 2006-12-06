/**
 * Created on Jan 24, 2006
 *
 * $Id: JbpmFactoryLocatorTests.java,v 1.2 2006/12/06 14:13:18 costin Exp $
 * $Revision: 1.2 $
 */
package org.springmodules.workflow.jbpm31;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.access.BeanFactoryReference;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

/**
 * @author Costin Leau
 *
 */
public class JbpmFactoryLocatorTests extends AbstractDependencyInjectionSpringContextTests {

	protected String[] getConfigLocations() {
		return new String[] { "org/springmodules/workflow/jbpm31/locatorContext.xml" };
	}

	JbpmFactoryLocator locator1, locator2;
	String INSTANCE_1 = "instance1";
	String INSTANCE_2 = "instance2";

	public void onSetUp() {
		locator1 = new JbpmFactoryLocator();
		locator1.setBeanName(INSTANCE_1);
		locator1.setBeanFactory(applicationContext.getBeanFactory());
		
		locator2 = new JbpmFactoryLocator();
		locator2.setBeanName(INSTANCE_2);
		locator2.setBeanFactory(applicationContext.getBeanFactory());
	}

	public void onTearDown() {
		// get a reference and kill it to make sure we don't clean the map in case
		// nobody used the reference
		
		BeanFactoryReference ref1;
		try {
			ref1 = locator1.useBeanFactory(INSTANCE_1);
			ref1.release();
			BeanFactoryReference ref2 = locator2.useBeanFactory(INSTANCE_2);
			ref2.release();

		}
		catch (IllegalArgumentException e) {
			// it's okay
		}
		locator1 = null;
		locator2 = null;
	}

	public void testJbpmFactoryLocator() {

		BeanFactoryReference reference1 = locator1.useBeanFactory(INSTANCE_1);
		BeanFactoryReference reference2 = locator2.useBeanFactory(INSTANCE_2);
		BeanFactoryReference aliasRef1 = locator1.useBeanFactory("alias1");
		BeanFactoryReference aliasRef2 = locator1.useBeanFactory("alias2");

		// verify the static map
		BeanFactory factory1 = reference1.getFactory();
		BeanFactory factory2 = reference2.getFactory();
		BeanFactory factory3 = reference2.getFactory();
		// get the alias from different factories
		BeanFactory alias1 = aliasRef1.getFactory();
		BeanFactory alias2 = aliasRef2.getFactory();

		assertSame(factory1, factory2);
		assertSame(factory1, factory3);
		// verify it's the same bean factory as the application context
		assertSame(factory1, applicationContext.getBeanFactory());

		// verify aliases
		assertSame(alias1, alias2);
		assertSame(factory1, alias1);

		aliasRef1.release();
		aliasRef2.release();
		reference1.release();
		reference2.release();
	}

	public void testFactoryLocatorDefault() {
		try {
			locator1.useBeanFactory(null);
			fail("there are more then one bean factories registered - should have thrown exception");
		}
		catch (IllegalArgumentException e) {
			// it's okay
		}

	}

	public void testFactoryLocatorOverride() {
		JbpmFactoryLocator locator = new JbpmFactoryLocator();
		// apply the correct order
		locator.setBeanName(INSTANCE_1);
		try {
			locator.setBeanFactory(applicationContext);
			fail("should have received exception");
		}
		catch (IllegalArgumentException e) {
			// it's okay
		}
	}
	
	public void testBeanFactoryLocatorContract() {
		BeanFactoryReference factory1 = locator1.useBeanFactory(INSTANCE_1);
		assertNotNull(factory1.getFactory());

		factory1.release();
		try {
			factory1.getFactory();
			fail("should have received exception");
		}
		catch (IllegalArgumentException e) {
			// it's okay
		}
	}

	public void testBeanFactoryRelease() {
		// make sure the setUp is properly tearedDown
	}
	
}
