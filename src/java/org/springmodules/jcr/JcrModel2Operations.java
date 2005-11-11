package org.springmodules.jcr;

import java.io.File;
import java.io.InputStream;

import javax.jcr.Node;


/**
 * Interface used for delimiting Jcr operations based on what the underlying repository supports (in this
 * case model 2 operations).
 * Normally not used but useful for casting to restrict access in some situations. 
 * 
 * @author Costin Leau
 *
 */
public interface JcrModel2Operations extends JcrModel1Operations {

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
	 * @see javax.jcr.Session#setNamespacePrefix(java.lang.String,
	 *      java.lang.String)
	 */
	public void setNamespacePrefix(String prefix, String uri);

	/**
	 * @see javax.jcr.Session#move(java.lang.String, java.lang.String)
	 */
	public void move(String srcAbsPath, String destAbsPath);

	/**
	 * @see javax.jcr.Session#save()
	 */
	public void save();

}