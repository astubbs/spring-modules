package org.springmodules.jcr;


import javax.jcr.Node;

import org.springframework.dao.DataAccessException;

/**
 * Interface that specifies a basic set of JCR operations. Not often used, 
 * but a useful option to enhance testability, as it can easily be mocked or stubbed.
 * 
 * <p>
 * Provides JcrTemplate's data access methods that mirror various Session
 * methods. See the JCR Session javadocs for details on those methods.
 * 
 * @author Costin Leau
 * 
 */
public interface JcrOperations extends JcrOptionalOperations {

    /**
     * Execute the action specified by the given action object within a Session.
     * 
     * @param action
     *            callback object that specifies the Jcr action
     * @param exposeNativeSession
     *            whether to expose the native Jcr Session to callback code
     * @return a result object returned by the action, or null
     * @throws org.springframework.dao.DataAccessException
     *             in case of Jcr errors
     */
    public Object execute(JcrCallback action, boolean exposeNativeSession) throws DataAccessException;

    /**
     * Execute the action specified by the given action object within a
     * {@link javax.jcr.Session}. Application exceptions thrown by the action
     * object get propagated to the caller (can only be unchecked). JCR
     * exceptions are transformed into appropriate DAO ones. Allows for
     * returning a result object, i.e. a domain object or a collection of domain
     * objects.
     * 
     * Note: Callback code does not need to explicitly log out of the
     * <code>Session</code>; this method will handle that itself.
     * 
     * The workspace logged into will be that named by the
     * <code>workspaceName</code> property; if that property is
     * <code>null</code>, the repository's default workspace will be used.
     * 
     * @param callback
     *            the <code>JCRCallback</code> that executes the client
     *            operation
     */
    public Object execute(JcrCallback callback) throws DataAccessException;

    /**
     * Dump the contents of the given node in a String. This method parses the whole
     * tree under the node and can generate a huge String.
     * 
     * @param node
     *            node to be dumped (null is equivalent to the root node)
     *            
     * @return node tree in a string representation.
     *             
     */
    public String dump(Node node);
    
    /**
     * Renames a node (with the given name) 
     * 
     * @param node node to rename
     * @param newName new name for the node
     * 
     */
    public void rename(Node node, String newName);

}