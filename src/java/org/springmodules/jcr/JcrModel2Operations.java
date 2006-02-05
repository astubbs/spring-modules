package org.springmodules.jcr;

import java.io.InputStream;


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