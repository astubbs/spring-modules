/**
 * Created on Oct 4, 2005
 *
 * $Id: DefaultSessionHolderProvider.java,v 1.1 2005/10/21 08:16:43 costin Exp $
 * $Revision: 1.1 $
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
     * @see org.springmodules.jcr.SessionHolderProvider#createSessionHolder(javax.jcr.Session)
     */
    public SessionHolder createSessionHolder(Session session) {
        return new SessionHolder(session);
    }

}
