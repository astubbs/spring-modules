/**
 * 
 */
package org.springmodules.jsr94.core;

import java.rmi.RemoteException;
import java.util.Map;

import javax.rules.InvalidHandleException;
import javax.rules.InvalidRuleSessionException;
import javax.rules.RuleExecutionSetNotFoundException;
import javax.rules.RuleRuntime;
import javax.rules.RuleSession;
import javax.rules.RuleSessionCreateException;
import javax.rules.RuleSessionTypeUnsupportedException;
import javax.rules.StatefulRuleSession;
import javax.rules.StatelessRuleSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springmodules.jsr94.Jsr94InvalidHandleException;
import org.springmodules.jsr94.Jsr94InvalidRuleSessionException;
import org.springmodules.jsr94.Jsr94RemoteException;
import org.springmodules.jsr94.Jsr94RuleExecutionSetNotFoundException;
import org.springmodules.jsr94.Jsr94RuleSessionCreateException;
import org.springmodules.jsr94.Jsr94RuleSessionTypeUnsupportedException;
import org.springmodules.jsr94.rulesource.RuleSource;
import org.springmodules.jsr94.support.Jsr94Accessor;
import org.springmodules.jsr94.support.StatefulRuleSessionCallback;
import org.springmodules.jsr94.support.StatelessRuleSessionCallback;

/**
 * Jsr94Template class contains methods for executing stateful and stateless
 * rule sessions.
 * 
 * @see org.springmodules.jsr94.support.Jsr94Accessor
 * @author janm
 */
public class Jsr94Template extends Jsr94Accessor {

	/**
	 * Log instance for this class
	 */
	protected final Log logger = LogFactory.getLog(Jsr94Template.class);
	
	/**
	 * Creates default instance of Jsr94Template; typically used when constructing the template
	 * in a bean factory. Use setRuleSource() to set the RuleSource, which must exist prior to construction
	 * of this class.
	 * @see Jsr94Accessor#setRuleSource(RuleSource)
	 */
	public Jsr94Template() {
		// noop
	}
	
	/**
	 * Creates instance of Jsr94Template with the specified ruleSource. Typically used outside of
	 * bean factory.
	 * @param ruleSource The ruleSource to be used for this template
	 */
	public Jsr94Template(RuleSource ruleSource) {
		setRuleSource(ruleSource);
		afterPropertiesSet();
	}
	
	/**
	 * Creates instance of RuleSession, maps the checked exceptions and returns a valid RuleSession
	 * @param uri The uri of the session
	 * @param properties The properties for the session
	 * @param type The type of the session
	 * @return RuleSession instance
	 */
	private RuleSession createRuleSession(final String uri, final Map properties, final int type) {
		try {
			return getRuleSource().createSession(uri, properties, type);
		} catch (RuleExecutionSetNotFoundException ex) {
			throw new Jsr94RuleExecutionSetNotFoundException(ex);
		} catch (RuleSessionTypeUnsupportedException ex) {
			throw new Jsr94RuleSessionTypeUnsupportedException(ex);
		} catch (RuleSessionCreateException ex) {
			throw new Jsr94RuleSessionCreateException(ex);
		} catch (RemoteException ex) {
			throw new Jsr94RemoteException(ex);
		}
	}
	
	/**
	 * Releases a stateless session
	 * @param session The session to be released
	 */
	private void releaseStatelessSession(final StatelessRuleSession session) {
		if (session == null) throw new IllegalArgumentException("session must not be null");
		try {
			session.release();
		} catch (InvalidRuleSessionException ex) {
			// this is impossible since we are releasing stateless session!
			throw new Jsr94InvalidRuleSessionException(ex);
		} catch (RemoteException ex) {
			throw new Jsr94RemoteException(ex);
		}
	}
	
	/**
	 * Releases a stateful session
	 * @param session The session to be released
	 */
	private void releaseStatefulSession(final StatefulRuleSession session) {
		if (session == null) throw new IllegalArgumentException("session must not be null");
		try {
			session.release();
		} catch (InvalidRuleSessionException ex) {
			// possible
			throw new Jsr94InvalidRuleSessionException(ex);
		} catch (RemoteException ex) {
			throw new Jsr94RemoteException(ex);
		}
	}
	
	/**
	 * Executes a stateless rule session
	 * @param uri The ruleset uri
	 * @param properties The proeprties for the session
	 * @param callback The executor callback
	 * @return Value returned by the executor implementation
	 */
	public Object executeStateless(final String uri, final Map properties, final StatelessRuleSessionCallback callback) {
		StatelessRuleSession session = (StatelessRuleSession)createRuleSession(uri, properties, RuleRuntime.STATELESS_SESSION_TYPE);
		try {
			return callback.execute(session);
		} catch (InvalidRuleSessionException ex) {
			throw new Jsr94InvalidRuleSessionException(ex);
		} catch (RemoteException ex) {
			throw new Jsr94RemoteException(ex);
		} finally {
			releaseStatelessSession(session);
		} 
	}
	
	/**
	 * Executes a stateful rule session. If called in a transaction, the session will be closed upon
	 * transaction completion; otherwise it will be closed when the callback returns.
	 * @param uri The ruleset uri
	 * @param properties The properties for the stateful rule session
	 * @param callback The callback
	 * @return The result of the callback execution
	 */
	public Object executeStateful(final String uri, final Map properties, final StatefulRuleSessionCallback callback) {
		// get the session
		boolean synchronize = TransactionSynchronizationManager.isSynchronizationActive(); 
		if (!synchronize) {
			logger.debug("Not running in transaction; the session will be closed when the callback returns.");
		}
		StatefulRuleSession session = (StatefulRuleSession)TransactionSynchronizationManager.getResource(getRuleSource());
		if (session == null) {
			logger.debug("Opening StatefulRuleSession");
			session = (StatefulRuleSession)createRuleSession(uri, properties, RuleRuntime.STATEFUL_SESSION_TYPE);
			if (synchronize) TransactionSynchronizationManager.bindResource(getRuleSource(), session);
		}
		try {
			return callback.execute(session);
		} catch (InvalidRuleSessionException ex) {
			throw new Jsr94InvalidRuleSessionException(ex);
		} catch (RemoteException ex) {
			throw new Jsr94RemoteException(ex);
		} catch (InvalidHandleException ex) {
			throw new Jsr94InvalidHandleException(ex);
		} finally {
			if (!synchronize) {
				releaseStatefulSession(session);
				logger.debug("Closed session");
			}
		}
	}

}
