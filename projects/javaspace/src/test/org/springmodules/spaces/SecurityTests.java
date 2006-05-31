/**
 * Created on Mar 11, 2006
 *
 * $Id$
 * $Revision$
 */
package org.springmodules.spaces;

import java.rmi.RMISecurityManager;

import junit.framework.TestCase;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * @author Costin Leau
 *
 */
public class SecurityTests extends TestCase {

	public void testRMISecurityManager() throws Exception {
		SecurityManager securityManager = System.getSecurityManager();

		try {
			Resource policy = new ClassPathResource("policy");
			System.out.println("policy is " + policy);
			System.setProperty("java.security.policy", policy.getFile().getAbsolutePath());
			System.setSecurityManager(new RMISecurityManager());
			System.setSecurityManager(null);
		}
		finally {
			System.setSecurityManager(securityManager);
		}
	}
}
