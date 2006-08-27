package org.springmodules.javaspaces.entry;

import java.io.Serializable;

/**
 * Interface describing the contract for generating uids.
 * The UID are mainly used for the RMI remoting support.
 *
 * @author Costin Leau
 *
 */
public interface UidFactory {
	/**
	 * Generate a new uid.
	 *
	 * @return the id
	 */
	public Serializable generateUid();
}
