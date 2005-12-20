/**
 * Created on Oct 4, 2005
 *
 * $Id: JackRabbitSessionHolderProvider.java,v 1.1 2005/12/20 17:38:19 costin Exp $
 * $Revision: 1.1 $
 */
package org.springmodules.jcr.jackrabbit.support;

import javax.jcr.Session;

import org.springmodules.jcr.SessionHolder;
import org.springmodules.jcr.SessionHolderProvider;

/**
 * JackRabbit specific session holder. This holder should be used with OpenSessionInViewFilter/Interceptor
 * or JcrInterceptor with JackRabbit if transactional support is required. The default session holder
 * however it is approapritate but transactions will not be supported.
 * 
 * @author Costin Leau
 *
 */
public class JackRabbitSessionHolderProvider implements SessionHolderProvider {

    /**
	 * @see org.springmodules.jcr.SessionHolderProvider#acceptsRepository(java.lang.String)
	 */
	public boolean acceptsRepository(String repositoryName) {
		return "Jackrabbit".equals(repositoryName);
	}

	/**
     * @see org.springmodules.jcr.SessionHolderProvider#createSessionHolder(javax.jcr.Session)
     */
    public SessionHolder createSessionHolder(Session session) {
        return new UserTxSessionHolder(session);
    }

}
