/**
 * Created on Mar 11, 2006
 *
 * $Id$
 * $Revision$
 */
package org.springmodules.javaspaces.blitz;

import net.jini.space.JavaSpace;

import org.dancres.blitz.remote.NullJavaSpace;
import org.springmodules.javaspaces.AbstractJavaSpaceFactoryBean;

/**
 * @author Costin Leau
 *
 */
public class NullSpaceFactoryBean extends AbstractJavaSpaceFactoryBean {

	/**
	 * @see org.springmodules.javaspaces.AbstractJavaSpaceFactoryBean#createSpace()
	 */
	protected JavaSpace createSpace() throws Exception {
		return new NullJavaSpace();
	}

}
