package org.springmodules.jcr.mapping;

import java.util.Collection;

import org.apache.portals.graffito.jcr.query.Query;
import org.springframework.dao.DataAccessException;

/**
 * Interface that specifies a basic set of JCR mapping operations. Not often used, but 
 * a useful option to enhance testability, as it can easily be mocked or stubbed.
 * 
 * <p>
 * Provides JcrMappingTemplate's data access methods that mirror various PersistenceManager
 * methods. See the required javadocs for details on those methods.
 * 
 * @author Costin Leau
 *
 */
public interface JcrMappingOperations {

    /**
     * Execute a JcrMappingCallback.
     * 
     * @param callback callback to execute
     * @return the callback result
     * @throws DataAccessException
     */
    public Object execute(JcrMappingCallback callback) throws DataAccessException;

    /**
     * @see org.apache.portals.graffito.jcr.persistence.PersistenceManager#insert(java.lang.String, java.lang.Object)
     */
    public void insert(final java.lang.String path, final java.lang.Object object);

    /**
     * @see org.apache.portals.graffito.jcr.persistence.PersistenceManager#update(java.lang.String, java.lang.Object)
     */
    public void update(final java.lang.String path, final java.lang.Object object);

    /**
     * @see org.apache.portals.graffito.jcr.persistence.PersistenceManager#remove(java.lang.String)
     */
    public void remove(final java.lang.String path);

    /**
     * @see org.apache.portals.graffito.jcr.persistence.PersistenceManager#getObject(java.lang.Class, java.lang.String) 
     */
    public Object getObject(final java.lang.Class pojoClass, final java.lang.String path);

    /**
     * @see org.apache.portals.graffito.jcr.persistence.PersistenceManager#getObjects(org.apache.portals.graffito.jcr.query.Query)
     */
    public Collection getObjects(final Query query);

}