package org.springmodules.jcr;

import javax.jcr.Session;

import org.springframework.transaction.support.ResourceHolderSupport;

/**
 * Holder object for Jcr Session.
 * 
 * @author Guillaume Bort <guillaume.bort@zenexity.fr>
 * @author Costin Leau
 * 
 */
public class SessionHolder extends ResourceHolderSupport {

    private Session session;

    public SessionHolder(Session session) {
        setSession(session);
    }

    protected void setSession(Session session) {
        this.session = session;
    }

    public Session getSession() {
        return session;
    }

    /**
     * @see org.springframework.transaction.support.ResourceHolderSupport#clear()
     */
    public void clear() {
        super.clear();
        session = null;
    }
}
