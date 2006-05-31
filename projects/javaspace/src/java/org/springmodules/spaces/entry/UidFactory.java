package org.springmodules.spaces.entry;

import java.io.Serializable;

/**
 * Interface describing the contract for generating uids. 
 * 
 * @author Costin Leau
 *
 */
public interface UidFactory {
	/**
	 * Generate a new uid.
	 * 
	 * @return
	 */
	public Serializable generateUid();
}
