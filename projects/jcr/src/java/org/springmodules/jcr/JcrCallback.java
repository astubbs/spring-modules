package org.springmodules.jcr;

import java.io.IOException;

import javax.jcr.RepositoryException;
import javax.jcr.Session;

/**
 * Callback interface for Jcr code. To be used with JcrTemplate's execute method, 
 * assumably often as anonymous classes within a method implementation. The typical 
 * implementation will call Session.get/move/query to perform some operations on 
 * the repository.
 * 
 * @author Costin Leau
 * @author Brian Moseley <bcm@osafoundation.org>
 */
public interface JcrCallback {

    /**
     * Called by {@link JcrTemplate#execute} within an active JCR
     * {@link javax.jcr.JCRSession}. It is not responsible for logging
     * out of the <code>Session</code> or handling transactions.
     *
     * Allows for returning a result object created within the
     * callback, i.e. a domain object or a collection of domain
     * objects. A thrown {@link RuntimeException} is treated as an
     * application exeception; it is propagated to the caller of the
     * template.
     */
    public Object doInJcr(Session session) throws IOException, RepositoryException;
}
