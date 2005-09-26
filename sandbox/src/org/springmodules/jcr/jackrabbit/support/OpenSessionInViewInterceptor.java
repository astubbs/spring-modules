/**
 * Created on Sep 12, 2005
 *
 * $Id: OpenSessionInViewInterceptor.java,v 1.1 2005/09/26 10:21:50 costin Exp $
 * $Revision: 1.1 $
 */
package org.springmodules.jcr.jackrabbit.support;

import javax.jcr.Session;

import org.springmodules.jcr.SessionHolder;
import org.springmodules.jcr.jackrabbit.UserTxSessionHolder;

/**
 * @author Costin Leau
 *
 */
public class OpenSessionInViewInterceptor extends org.springmodules.jcr.support.OpenSessionInViewInterceptor {

    /**
     * @see org.springmodules.jcr.TransactionSessionHolder#createSessionHolder(javax.jcr.Session)
     */
    public SessionHolder createSessionHolder(Session session) {
        return new UserTxSessionHolder(session);
    }

}
