package org.springmodules.jcr;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.jcr.Item;
import javax.jcr.Node;
import javax.jcr.ValueFactory;
import javax.jcr.query.QueryResult;

import org.springframework.dao.DataAccessException;
import org.xml.sax.ContentHandler;

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
     * Import a File in the current workspace on the given node. If the
     * parentNode is null the root node will be used.
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
    public Node importFile(Node parentNode, File file);

    /**
     * Import a Folder using the current session on the given node. If the
     * parentNode is null the root node will be used.
     * 
     * <strong>Note</strong> this method has been mainly inspired from the
     * contrib/examples package inside JackRabbit repository.
     * 
     * @param parentnode
     *            Parent Repository Node
     * @param directory
     *            Directory to be traversed
     * @param includeStartDir
     *            true if the given directory should be included or just it's
     *            entries
     */

    public Node importFolder(Node parentnode, File directory, boolean includeStartDirectory);

    /**
     * @see javax.jcr.Session#addLockToken(java.lang.String)
     */
    public void addLockToken(String lock);

    /**
     * @see javax.jcr.Session#getAttribute(java.lang.String)
     */
    public Object getAttribute(String name);

    /**
     * @see javax.jcr.Session#getAttributeNames()
     */
    public String[] getAttributeNames();

    /**
     * @see javax.jcr.Session#getImportContentHandler(java.lang.String, int)
     */
    public ContentHandler getImportContentHandler(String parentAbsPath, int uuidBehavior);

    /**
     * @see javax.jcr.Session#getItem(java.lang.String)
     */
    public Item getItem(String absPath);

    /**
     * @see javax.jcr.Session#getLockTokens()
     */
    public String[] getLockTokens();

    /**
     * @see javax.jcr.Session#getNamespacePrefix(java.lang.String)
     */
    public String getNamespacePrefix(String uri);

    /**
     * @see javax.jcr.Session#getNamespacePrefixes()
     */
    public String[] getNamespacePrefixes();

    /**
     * @see javax.jcr.Session#getNamespaceURI(java.lang.String)
     */
    public String getNamespaceURI(String prefix);

    /**
     * @see javax.jcr.Session#getNodeByUUID(java.lang.String)
     */
    public Node getNodeByUUID(String uuid);

    /**
     * @see javax.jcr.Session#getRootNode(); 
     */
    public Node getRootNode();
    
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
    public void importXML(String parentAbsPath, InputStream in, int uuidBehavior);

    /**
     * @see javax.jcr.Session#refresh(boolean)
     */
    public void refresh(boolean keepChanges);

    /**
     * @see javax.jcr.Session#removeLockToken(java.lang.String)
     */
    public void removeLockToken(String lt);

    /**
     * @see javax.jcr.Session#setNamespacePrefix(java.lang.String,
     *      java.lang.String)
     */
    public void setNamespacePrefix(String prefix, String uri);

    /**
     * @see javax.jcr.Session#isLive()
     */
    public boolean isLive();

    /**
     * @see javax.jcr.Session#itemExists(java.lang.String)
     */
    public boolean itemExists(String absPath);

    /**
     * @see javax.jcr.Session#move(java.lang.String, java.lang.String)
     */
    public void move(String srcAbsPath, String destAbsPath);

    /**
     * @see javax.jcr.Session#save()
     */
    public void save();

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
     * Execute a persistent query from the given node.
     * 
     * @see javax.jcr.query.QueryManager#getQuery(javax.jcr.Node)
     * @param node node to be dumped
     * @return query result
     */
    public QueryResult query(Node node);

    /**
     * Execute a query with the given strings with XPATH as default language.
     * It's the same as #query(java.lang.String, java.lang.String)
     * 
     * @see javax.jcr.query.QueryManager#createQuery(java.lang.String, java.lang.String)
     * @param statement query statement
     * @return query result
     */
    public QueryResult query(String statement);

    /**
     * Execute a query with the given strings.
     * 
     * @see javax.jcr.query.QueryManager#createQuery(java.lang.String, java.lang.String)
     * @param statement query statement
     * @param language language statement
     * @return query result
     */
    public QueryResult query(String statement, String language);
    
    /**
     * Default method for doing multiple queries. It assumes the language is
     * XPATH and that errors will not be ignored.
     * 
     * @param list a list of queries that will be executed against the
     *            repository
     * @return a map containing the queries as keys and results as values
     */
    public Map query(final List list);
    
    /**
     * Utility method for executing a list of queries against the repository.
     * Reads the queries given and returns the results in a map.
     * 
     * <p/> If possible the map will be a LinkedHashSet on JDK 1.4+, otherwise
     * LinkedHashSet from Commons collections 3.1 if the package is found. If
     * the above fails a HashMap will be returned.
     * 
     * @see org.springframework.core.CollectionFactory
     * 
     * @param list list of queries
     * @param language language of the queries. If null XPATH is assumed.
     * @param ignoreErrors if true it will populate unfound nodes with null
     * @return a map containing the queries as keys and results as values
     */
    public Map query(final List list, final String language, final boolean ignoreErrors);

}