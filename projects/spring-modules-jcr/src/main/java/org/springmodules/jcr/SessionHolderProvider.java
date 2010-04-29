/**
 * Created on Oct 4, 2005
 *
 * $Id: SessionHolderProvider.java,v 1.1 2005/12/20 17:38:09 costin Exp $
 * $Revision: 1.1 $
 */
package org.springmodules.jcr;

import javax.jcr.Session;

/**
 * SessionHolderProvider is a factory that creates a session holder for classes which require collaboration with 
 * TransactionSynchronizationManager. Because there is no standard on how to a Jcr repository
 * participates inside transactions, each implementation has it's own support (XAResource,Transaction)
 * which has to be wrapped in the approapriate holder.
 * 
 * 
 * @author Costin Leau
 *
 */
public interface SessionHolderProvider {

	/**
	 * Return the specific session holder.
	 * 
	 * @param session
	 * @return
	 */
	public SessionHolder createSessionHolder(Session session);

	/**
	 * Method for maching the sessionHolderProvider against a repository (given by name).
	 * 
	 * @param repositoryName
	 * @return true if the sessionHolderProvider is suitable for the given repository name, false otherwise.
	 */
	public boolean acceptsRepository(String repositoryName);
}
