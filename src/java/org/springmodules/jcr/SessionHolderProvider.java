/**
 * Created on Oct 4, 2005
 *
 * $Id: SessionHolderProvider.java,v 1.1 2005/10/21 08:17:06 costin Exp $
 * $Revision: 1.1 $
 */
package org.springmodules.jcr;

import javax.jcr.Session;


/**
 * SessionHolderProvider returns a session holder for classes which require collaboration with 
 * TransactionSynchronizationManager. Because there is no standard on how to a Jcr repository
 * participates inside transactions, each implementation has it's own support (XAResource,Transaction)
 * which has to be wrapped in the approapriate holder.
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
}
