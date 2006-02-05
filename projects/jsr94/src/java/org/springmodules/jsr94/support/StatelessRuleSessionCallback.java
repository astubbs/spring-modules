/**
 * 
 */

package org.springmodules.jsr94.support;

import java.rmi.RemoteException;

import javax.rules.InvalidRuleSessionException;
import javax.rules.StatelessRuleSession;

/**
 * Implement this interface to execute methods on a StatelessRuleSession.
 *
 * @author janm
 * @see StatelessRuleSession
 */
public interface StatelessRuleSessionCallback {

	/**
	 * Perform operations on the session.
	 * @param session The StatelessRuleSession
	 * @return Any value
	 * @throws InvalidRuleSessionException If the session is invalid
	 * @throws RemoteException If the remote call fails
	 */
	Object execute(StatelessRuleSession session) throws InvalidRuleSessionException, RemoteException;
}
