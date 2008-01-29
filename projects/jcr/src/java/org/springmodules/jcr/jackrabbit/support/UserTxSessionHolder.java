/**
 * Created on Sep 8, 2005
 *
 * $Id: UserTxSessionHolder.java,v 1.3 2008/01/29 12:28:22 coliny Exp $
 * $Revision: 1.3 $
 */
package org.springmodules.jcr.jackrabbit.support;

import javax.jcr.Session;
import javax.transaction.UserTransaction;

import org.apache.jackrabbit.api.XASession;
import org.springmodules.jcr.SessionHolder;

/**
 * Extension of Session Holder which includes a UserTransaction which handles
 * the XASession returned by the JackRabbit repository implementation.
 * 
 * @see org.springmodules.jcr.jackrabbit.support.JackRabbitUserTransaction
 * @author Costin Leau
 * 
 */
public class UserTxSessionHolder extends SessionHolder {

	private UserTransaction transaction;

	/**
	 * @param session
	 */
	public UserTxSessionHolder(Session session) {
		super(session);
	}

	/**
	 * @return Returns the transaction.
	 */
	public UserTransaction getTransaction() {
		return transaction;
	}

	/**
	 * @see org.springmodules.jcr.SessionHolder#setSession(javax.jcr.Session)
	 */
	public void setSession(Session session) {
		/*
		 * if (!(session instanceof XASession)) throw new
		 * IllegalArgumentException( "Session not of type XASession; actual type
		 * is " + session.getClass());
		 */

		// when using JCA we have another session type
		if (session instanceof XASession) {
			transaction = new JackRabbitUserTransaction(session);
		}
		super.setSession(session);
	}

	/**
	 * @see org.springframework.transaction.support.ResourceHolderSupport#clear()
	 */
	public void clear() {
		super.clear();
		transaction = null;
	}

}
