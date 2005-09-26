/**
 * Created on Sep 12, 2005
 *
 * $Id: JcrInterceptor.java,v 1.1 2005/09/26 10:21:50 costin Exp $
 * $Revision: 1.1 $
 */
package org.springmodules.jcr.jackrabbit;

import javax.jcr.Session;

import org.springmodules.jcr.SessionHolder;

/**
 * JackRabbit specific sessionHolder dependent Interceptor.
 * 
 * @author Costin Leau
 *
 */
public class JcrInterceptor extends org.springmodules.jcr.JcrInterceptor {

    /**
     * @see org.springmodules.jcr.TransactionSessionHolder#createSessionHolder(javax.jcr.Session)
     */
    public SessionHolder createSessionHolder(Session session) {
        return new UserTxSessionHolder(session);
    }

}
