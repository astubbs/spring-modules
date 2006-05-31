/**
 * 
 */
package org.springmodules.asynch;

import org.springmodules.spaces.entry.MethodResultEntry;

/**
 * @author Costin Leau
 * 
 */
public interface AsynchResponse {
	/**
	 * Return the result.
	 * 
	 * @return
	 */
	public MethodResultEntry getResult() throws Throwable;

	/**
	 * Is the response complete.
	 * @return
	 */
	public boolean isComplete();

}
