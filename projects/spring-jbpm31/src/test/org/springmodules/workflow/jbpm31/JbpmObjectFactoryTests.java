/**
 * Created on Feb 28, 2006
 *
 * $Id: JbpmObjectFactoryTests.java,v 1.1 2006/03/01 16:55:29 costin Exp $
 * $Revision: 1.1 $
 */
package org.springmodules.workflow.jbpm31;

import junit.framework.TestCase;

import org.easymock.MockControl;
import org.springframework.beans.factory.BeanFactory;

/**
 * @author Costin Leau
 *
 */
public class JbpmObjectFactoryTests extends TestCase {

	private JbpmObjectFactory objectFactory;
	String beanName = "someBeanName";
	MockControl beanCtrl;
	BeanFactory factory;

	
	public void setUp() {
		objectFactory = new JbpmObjectFactory();
		beanCtrl = MockControl.createControl(BeanFactory.class);
		factory = (BeanFactory) beanCtrl.getMock();
		objectFactory.setBeanFactory(factory);
		
	}

	public void tearDown() {
		beanCtrl.verify();
		
		objectFactory = null;
		beanCtrl = null;
		factory = null;
	}

	/*
	 * Test method for 'org.springmodules.workflow.jbpm31.JbpmObjectFactory.createObject(String)'
	 */
	public void testCreateObject() {
		Object bean = new Object();
		beanCtrl.expectAndReturn(factory.getBean(beanName), bean);
		beanCtrl.replay();
		assertSame(bean, objectFactory.createObject(beanName));
	}

	/*
	 * Test method for 'org.springmodules.workflow.jbpm31.JbpmObjectFactory.hasObject(String)'
	 */
	public void testHasObject() {
		beanCtrl.expectAndReturn(factory.containsBean(beanName), true);
		beanCtrl.replay();
		assertTrue(objectFactory.hasObject(beanName));
	}

}
