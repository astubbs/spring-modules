/**
 * Created on Mar 14, 2006
 *
 * $Id: GigaSpaceTests.java,v 1.3 2006/06/21 17:53:04 costin Exp $
 * $Revision: 1.3 $
 */
package org.springmodules.javaspaces.gigaspaces;


import net.jini.core.transaction.server.TransactionManager;
import net.jini.space.JavaSpace;
import com.j_spaces.core.IJSpace;

import org.springframework.test.AbstractDependencyInjectionSpringContextTests;
import org.springmodules.jini.JiniServiceFactoryBean;
import org.springmodules.transaction.jini.JiniTransactionManagerFactoryBean;

/**
 * @author Costin Leau
 *
 */
public class GigaSpaceTests extends AbstractDependencyInjectionSpringContextTests {

	/**
	 * @see org.springmodules.test.AbstractDependencyInjectionSpringContextTests#getConfigLocations()
	 */
	protected String[] getConfigLocations() {
		return new String[] { "/config/giga-context.xml" };
	}

	private JavaSpace space;

	public void testStartUp() {
		assertNotNull(space);
	}

	public void testJiniServiceLocator() throws Exception {
		JiniServiceFactoryBean serviceFactory = new JiniServiceFactoryBean();
		serviceFactory.setServiceClass(JavaSpace.class);
		serviceFactory.afterPropertiesSet();
		JavaSpace jiniSpace = (JavaSpace) serviceFactory.getObject();

		assertNotNull(space);
		assertNotNull(jiniSpace);
		// check class
		assertSame(space.getClass(), jiniSpace.getClass());

		// check instance (redundant but this may be changed in the future if another protocol is used)
		//assertEquals(space, jiniSpace);
	}

	/**
	 * Will fail if the tx manager was not started separately.
	 *
	 * @throws Exception
	 */
	public void testRetrieveTransactionManager() throws Exception {
		JiniServiceFactoryBean serviceFactory = new JiniServiceFactoryBean();
		serviceFactory.afterPropertiesSet();

		JiniTransactionManagerFactoryBean txFactory = new JiniTransactionManagerFactoryBean();
		txFactory.afterPropertiesSet();
		TransactionManager tm = (TransactionManager) txFactory.getObject();
		//assertNotNull(tm);
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

}
