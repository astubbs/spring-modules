/**
 * Created on Mar 12, 2006
 *
 * $Id$
 * $Revision$
 */
package org.springmodules.jini.entry;

import net.jini.core.entry.Entry;
import net.jini.space.JavaSpace;

import org.springframework.aop.framework.ProxyFactory;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;
import org.springmodules.jini.entry.EntryMixinAdvisor;

/**
 * @author Costin Leau
 *
 */
public class EntryMixinAdvisorTests extends AbstractDependencyInjectionSpringContextTests {

	ProxyFactory factory;
	Object obj;
	Object proxy;
	JavaSpace space;
	// 1 sec
	long LEASE = 1000 * 1;

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void onSetUp() throws Exception {
		obj = new Object();
		factory = new ProxyFactory(obj);
		//factory.addInterceptor(new EntryMixin());
		factory.addAdvisor(new EntryMixinAdvisor());
		factory.setProxyTargetClass(true);
		proxy = factory.getProxy();
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void onTearDown() throws Exception {
		factory = null;
		obj = null;
		proxy = null;
	}

	public void testMixin() {
		assertFalse(obj instanceof Entry);
		assertTrue(proxy instanceof Entry);
		assertNotSame(obj, proxy);
	}

	public void testSpaceSerialization() throws Exception {
		space.write((Entry)proxy, null, LEASE);
		
		Object fromSpace = space.read((Entry) proxy, null, LEASE);
		System.out.println(fromSpace);
		System.out.println(proxy);
	}

	/**
	 * @return Returns the space.
	 */
	public JavaSpace getSpace() {
		return space;
	}

	/**
	 * @param space The space to set.
	 */
	public void setSpace(JavaSpace space) {
		this.space = space;
	}

	/**
	 * @see org.springmodules.test.AbstractDependencyInjectionSpringContextTests#getConfigLocations()
	 */
	protected String[] getConfigLocations() {
		return new String[] { "/org/springmodules/javaspaces/space-context.xml" };
	}

}
