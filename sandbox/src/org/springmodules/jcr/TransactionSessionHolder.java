/**
 * Created on Sep 12, 2005
 *
 * $Id: TransactionSessionHolder.java,v 1.1 2005/09/26 10:21:51 costin Exp $
 * $Revision: 1.1 $
 */
package org.springmodules.jcr;

import javax.jcr.Session;

/**
 * Interface required for creating session holder for classes which require colaboration with 
 * TransactionSynchronizationManager. Because there is no standard on how to a Jcr repository
 * participates inside transactions, each implementation has it's own methods.
 * 
 * @author Costin Leau
 *
 */
public interface TransactionSessionHolder {

    /**
     * Needed to be implemented by subclasses in order to provide the approapriate 
     * session holder.
     * 
     * @param session
     * @return
     */
    public SessionHolder createSessionHolder(Session session);
}
