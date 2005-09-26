package org.springmodules.jcr;

import java.io.File;
import java.io.InputStream;

import javax.jcr.Item;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.ValueFactory;

import org.springframework.dao.DataAccessException;
import org.xml.sax.ContentHandler;

/**
 * Interface that specifies a basic set of JCR operations.
 * Implemented by JcrTemplate. Not often used, but a useful option
 * to enhance testability, as it can easily be mocked or stubbed.
 *
 * <p>Provides JcrTemplate's data access methods that mirror various
 * Session methods. See the JCR Session javadocs
 * for details on those methods.
 *
 * @author Costin Leau
 *
 */
public interface JcrOperations {

    /**
     * Execute the action specified by the given action object within a Session.
     * 
     * @param action
     *            callback object that specifies the Jcr action
     * @param exposeNativeSession
     *            whether to expose the native Jcr Session to callback code
     * @return a result object returned by the action, or null
     * @throws org.springframework.dao.DataAccessException
     *             in case of Hibernate errors
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
     * Imports a File in the current workspace on the given node. If the parentNode is null the root
     * node will be used.
     * 
     * <strong>Note</strong> this method has been mainly inspired from the
     * contrib/examples package inside JackRabbit repository.
     * 
     * @param parentNode
     *            Parent Repository Node
     * @param file
     *            File to be imported
     * 
     * @return the child node to which the file belongs to
     */
    public Node importFile(final Node parentNode, final File file);

    /**
     * Import a Folder using the current session on the given node. If the parentNode is null the root
     * node will be used.
     * 
     * <strong>Note</strong> this method has been mainly inspired from the
     * contrib/examples package inside JackRabbit repository.
     * 
     * @param parentnode
     *            Parent Repository Node
     * @param directory
     *            Directory to be traversed
     * @param includeStartDir
     *            true if the given directory should be included or just it's entries
     */

    public Node importFolder(final Node parentnode, final File directory, final boolean includeStartDirectory);

    /**
     * @see javax.jcr.Session#addLockToken(java.lang.String)
     */
    public void addLockToken(final String lock);

    /**
     * @see javax.jcr.Session#getAttribute(java.lang.String)
     */
    public Object getAttribute(final String name);

    /**
     * @see javax.jcr.Session#getAttributeNames()
     */
    public String[] getAttributeNames();

    /**
     * @see javax.jcr.Session#getImportContentHandler(java.lang.String, int)
     */
    public ContentHandler getImportContentHandler(final String parentAbsPath, final int uuidBehavior);

    /**
     * @see javax.jcr.Session#getItem(java.lang.String)
     */
    public Item getItem(final String absPath);

    /**
     * @see javax.jcr.Session#getLockTokens()
     */
    public String[] getLockTokens();

    /**
     * @see javax.jcr.Session#getNamespacePrefix(java.lang.String)
     */
    public String getNamespacePrefix(final String uri);

    /**
     * @see javax.jcr.Session#getNamespacePrefixes()
     */
    public String[] getNamespacePrefixes();

    /**
     * @see javax.jcr.Session#getNamespaceURI(java.lang.String)
     */
    public String getNamespaceURI(final String prefix);

    /**
     * @see javax.jcr.Session#getNodeByUUID(java.lang.String)
     */
    public Node getNodeByUUID(final String uuid);

    /**
     * @see javax.jcr.Session#getUserID()
     */
    public String getUserID();

    /**
     * @see javax.jcr.Session#getValueFactory()
     */
    public ValueFactory getValueFactory();

    /**
     * @see javax.jcr.Session#hasPendingChanges()
     */
    public boolean hasPendingChanges();

    /**
     * @see javax.jcr.Session#importXML(java.lang.String, java.io.InputStream,
     *      int)
     */
    public void importXML(final String parentAbsPath, final InputStream in, final int uuidBehavior);

    /**
     * @see javax.jcr.Session#refresh(boolean)
     */
    public void refresh(final boolean keepChanges);

    /**
     * @see javax.jcr.Session#removeLockToken(java.lang.String)
     */
    public void removeLockToken(final String lt);

    /**
     * @see javax.jcr.Session#setNamespacePrefix(java.lang.String,
     *      java.lang.String)
     */
    public void setNamespacePrefix(final String prefix, final String uri);

    /**
     * @see javax.jcr.Session#isLive()
     */
    public boolean isLive();

    /**
     * @see javax.jcr.Session#itemExists(java.lang.String)
     */
    public boolean itemExists(final String absPath);

    /**
     * @see javax.jcr.Session#move(java.lang.String, java.lang.String)
     */
    public void move(final String srcAbsPath, final String destAbsPath);

    /**
     * @see javax.jcr.Session#save()
     */
    public void save();

    /**
     * Dumps the contents of the given node to standard output.
     * 
     * @param node
     *            the node to be dumped (null is equivalent to the root node)
     * @throws RepositoryException
     *             on repository errors
     */
    public String dump(final Node node);

}