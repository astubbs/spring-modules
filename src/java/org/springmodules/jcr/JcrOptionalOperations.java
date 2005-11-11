package org.springmodules.jcr;




/**
 * Interface used for delimiting Jcr operations based on what the underlying repository supports (in 
 * this case optional operations)..
 * Normally not used but useful for casting to restrict access in some situations. 
 * 
 * @author Costin Leau
 *
 */
public interface JcrOptionalOperations extends JcrModel2Operations {

	/**
	 * @see javax.jcr.Session#addLockToken(java.lang.String)
	 */
	public void addLockToken(String lock);

	/**
	 * @see javax.jcr.Session#getLockTokens()
	 */
	public String[] getLockTokens();

	/**
	 * @see javax.jcr.Session#removeLockToken(java.lang.String)
	 */
	public void removeLockToken(String lt);

}