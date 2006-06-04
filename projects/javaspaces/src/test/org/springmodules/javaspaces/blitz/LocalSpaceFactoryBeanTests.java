/**
 * Created on Mar 11, 2006
 *
 * $Id$
 * $Revision$
 */
package org.springmodules.javaspaces.blitz;

import net.jini.space.JavaSpace;

import org.springframework.test.AbstractDependencyInjectionSpringContextTests;
import org.springmodules.javaspaces.blitz.NullSpaceFactoryBean;

/**
 * @author Costin Leau
 *
 */
public class LocalSpaceFactoryBeanTests extends AbstractDependencyInjectionSpringContextTests {

	/**
	 * @see org.springmodules.test.AbstractDependencyInjectionSpringContextTests#getConfigLocations()
	 */
	protected String[] getConfigLocations() {
		return new String[] { "/org/springmodules/javaspaces/blitz/blitz-context.xml" };
	}

	private JavaSpace space;

	public void testFactory() throws Exception {
		assertNotNull(space);
	}

	public void testNullSpace() throws Exception {
		NullSpaceFactoryBean spaceFactory = new NullSpaceFactoryBean();
		spaceFactory.afterPropertiesSet();
		JavaSpace nullSpace = (JavaSpace) spaceFactory.getObject();
		assertNotNull(nullSpace);
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
