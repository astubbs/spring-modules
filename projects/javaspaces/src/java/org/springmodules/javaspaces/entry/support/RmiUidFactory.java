/**
 * 
 */
package org.springmodules.javaspaces.entry.support;

import java.io.Serializable;
import java.rmi.server.UID;

import org.springmodules.javaspaces.entry.UidFactory;

/**
 * RmiUidFactory. The implementation is based on java.rmi.server.UID class in order to avoid
 * any outside dependencies. The UID are guaranteed to be unique on the same host. It is recommended
 * to review the javaspace topology and choose an approapriate uid generating strategy.
 * 
 * @see java.rmi.server.UID
 * @author Costin Leau
 *
 */
public class RmiUidFactory implements UidFactory {

	/* (non-Javadoc)
	 * @see org.springmodules.javaspaces.entry.UidFactory#generateUid()
	 */
	public Serializable generateUid() {
		return new UID();
	}

}
