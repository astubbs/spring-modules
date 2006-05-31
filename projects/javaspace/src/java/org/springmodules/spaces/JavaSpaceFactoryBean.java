/**
 * Created on Mar 11, 2006
 *
 * $Id$
 * $Revision$
 */
package org.springmodules.spaces;

import net.jini.space.JavaSpace;

import org.springmodules.jini.JiniServiceFactoryBean;

/**
 * Generic FactoryBean for retrieving a JavaSpace from the JINI environment. It uses 
 * JiniServiceFactoryBean internally, which should be used in case more options are 
 * required. 
 * 
 * @see org.springmodules.jini.JiniServiceFactoryBean
 * @author Costin Leau
 * 
 */
public class JavaSpaceFactoryBean extends AbstractJavaSpaceFactoryBean {
	private String spaceName;

	/**
	 * @see org.springmodules.spaces.AbstractJavaSpaceFactoryBean#createSpace()
	 */
	protected JavaSpace createSpace() throws Exception {
		JiniServiceFactoryBean serviceFactory = new JiniServiceFactoryBean();
		serviceFactory.setServiceClass(JavaSpace.class);
		serviceFactory.setServiceName(spaceName);
		serviceFactory.afterPropertiesSet();
		
		JavaSpace space = (JavaSpace) serviceFactory.getObject();
		return space;
	}

	/**
	 * @return Returns the spaceName.
	 */
	public String getSpaceName() {
		return spaceName;
	}

	/**
	 * @param spaceName The spaceName to set.
	 */
	public void setSpaceName(String spaceName) {
		this.spaceName = spaceName;
	}

}
