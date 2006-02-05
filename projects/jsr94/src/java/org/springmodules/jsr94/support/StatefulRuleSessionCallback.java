/**
 * 
 */

package org.springmodules.jsr94.support;

import java.rmi.RemoteException;

import javax.rules.InvalidHandleException;
import javax.rules.InvalidRuleSessionException;
import javax.rules.StatefulRuleSession;

/**
 * Implement this interface to execute methods on a StatefulRuleSession.
 *
 * @author janm
 * @see StatefulRuleSession
 */
public interface StatefulRuleSessionCallback {

	/**
		 * Perform operations on the session.
		 *
		 * @return Any value
		 * @throws InvalidRuleSessionException If the session is invalid
		 * @throws RemoteException If the remote call fails
		 */
	Object execute(StatefulRuleSession session) throws InvalidRuleSessionException, InvalidHandleException, RemoteException;

}
