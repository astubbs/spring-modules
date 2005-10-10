/**
 * Created on Oct 3, 2005
 *
 * $Id: JcrMappingCallback.java,v 1.1 2005/10/10 09:27:26 costin Exp $
 * $Revision: 1.1 $
 */
package org.springmodules.jcr.mapping;

import org.apache.portals.graffito.jcr.exception.JcrMappingException;
import org.apache.portals.graffito.jcr.persistence.PersistenceManager;

/**
 * Callback interface for Jcr mapping code. To be used with JcrMappingTemplate's execute method, 
 * assumably often as anonymous classes within a method implementation. The typical 
 * implementation will call PersistenceManager.get/insert/remove/update to perform some operations on 
 * the repository.
 * 
 * @author Costin Leau
 *
 */
public interface JcrMappingCallback {

    /**
     * Called by {@link JcrMappingTemplate#execute} within an active PersistenceManager
     * {@link org.apache.graffito.jcr.mapper.persistence.PersistenceManager}. 
     * It is not responsible for logging out of the <code>Session</code> or handling transactions.
     *
     * Allows for returning a result object created within the
     * callback, i.e. a domain object or a collection of domain
     * objects. A thrown {@link RuntimeException} is treated as an
     * application exeception; it is propagated to the caller of the
     * template.
     */
    public Object doInJcrMapping(PersistenceManager manager) throws JcrMappingException;
}
