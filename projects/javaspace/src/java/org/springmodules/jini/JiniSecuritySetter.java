/**
 * Created on Mar 14, 2006
 *
 * $Id$
 * $Revision$
 */
package org.springmodules.jini;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;

/**
 * Utility class used for setting a security manager and a policy on the running JVM.
 * The class will save the existing security manager which will be restored once
 * the bean is destroyed.
 * 
 * The class acts as a shortcut for setting policies and a security manager just during
 * the lifetime of a Spring application context.
 * 
 * @author Costin Leau
 *
 */
public class JiniSecuritySetter implements InitializingBean, DisposableBean {

	private Resource policy;
	private String oldPolicy;
	private SecurityManager securityManager, oldSecurityManager;

	private static final String POLICY_PROPERTY = "java.security.policy";

	/**
	 * @see org.springmodules.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		if (securityManager == null)
			throw new IllegalArgumentException("securityManager property is required");
		placeSecurity();
	}

	/**
	 * @see org.springmodules.beans.factory.DisposableBean#destroy()
	 */
	public void destroy() throws Exception {
		if (oldPolicy != null)
			System.setProperty(POLICY_PROPERTY, oldPolicy);
		else
			// remove the key in a 1.3 compliant way
			System.getProperties().remove(POLICY_PROPERTY);
		
		System.setSecurityManager(oldSecurityManager);
	}

	protected void placeSecurity() throws Exception {
		oldPolicy = System.getProperty(POLICY_PROPERTY);
		oldSecurityManager = System.getSecurityManager();

		System.setProperty(POLICY_PROPERTY, policy.getURL().toExternalForm());
		System.setSecurityManager(securityManager);
	}

	/**
	 * @return Returns the policy.
	 */
	public Resource getPolicy() {
		return policy;
	}

	/**
	 * @param policy The policy to set.
	 */
	public void setPolicy(Resource policy) {
		this.policy = policy;
	}

	/**
	 * @return Returns the securityManager.
	 */
	public SecurityManager getSecurityManager() {
		return securityManager;
	}

	/**
	 * @param securityManager The securityManager to set.
	 */
	public void setSecurityManager(SecurityManager securityManager) {
		this.securityManager = securityManager;
	}

}
