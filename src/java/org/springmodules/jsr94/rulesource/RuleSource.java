/**
 * 
 */
package org.springmodules.jsr94.rulesource;

import java.rmi.RemoteException;
import java.util.Map;

import javax.rules.RuleExecutionSetNotFoundException;
import javax.rules.RuleSession;
import javax.rules.RuleSessionCreateException;
import javax.rules.RuleSessionTypeUnsupportedException;

/**
 * The RuleSource interface creates a RuleSession subclass implementation.
 * 
 * @author janm
 */
public interface RuleSource {

	/**
	 * Gets a RuleSession implementation for the specified bindUri, properties and type
	 * @param uri The ruleset uri
	 * @param properties The properties for the session
	 * @param type The session type
	 * @return RuleSession
	 * @throws RuleExecutionSetNotFoundException If the bindUri points to a non-registered ruleExecutionSet
	 * @throws RemoteException If an remoting error is encountered
	 * @throws RuleSessionTypeUnsupportedException If the specified RuleSession type cannot be created
	 * @throws RuleSessionCreateException If the RuleSession cannot be created
	 * @see javax.rules.RuleRuntime#STATEFUL_SESSION_TYPE
	 * @see javax.rules.RuleRuntime#STATELESS_SESSION_TYPE
	 */
	RuleSession createSession(String uri, Map properties, int type) throws RuleExecutionSetNotFoundException, RemoteException, RuleSessionTypeUnsupportedException, RuleSessionCreateException;
}
