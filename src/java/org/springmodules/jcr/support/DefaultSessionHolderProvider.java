/**
 * Created on Oct 4, 2005
 *
 * $Id: DefaultSessionHolderProvider.java,v 1.2 2005/11/11 15:47:09 costin Exp $
 * $Revision: 1.2 $
 */
package org.springmodules.jcr.support;

import javax.jcr.Session;

import org.springmodules.jcr.SessionHolder;
import org.springmodules.jcr.SessionHolderProvider;

/**
 * Default implementation of org.springmodules.jcr.SessionHolderProvider. It creates a generic
 * SessionHolder w/o any transaction support.
 * 
 * @author Costin Leau
 *
 */
public class DefaultSessionHolderProvider implements SessionHolderProvider {

    /**
	 * @see org.springmodules.jcr.SessionHolderProvider#acceptsRepository(java.lang.String)
	 */
	public boolean acceptsRepository(String repositoryName) {
		return true;
	}

	/**
     * @see org.springmodules.jcr.SessionHolderProvider#createSessionHolder(javax.jcr.Session)
     */
    public SessionHolder createSessionHolder(Session session) {
        return new SessionHolder(session);
    }

}
