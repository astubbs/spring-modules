/**
 * 
 */
package org.springmodules.javaspaces.entry.support;

import java.io.Serializable;

import org.safehaus.uuid.UUIDGenerator;
import org.springmodules.javaspaces.entry.UidFactory;

/**
 * UidFactory based on Jug library (http://jug.safehaus.org). 
 * 
 * @author Costin Leau
 *
 */
public class JugUidFactory implements UidFactory {

	private UUIDGenerator generator = UUIDGenerator.getInstance();
	
	/* (non-Javadoc)
	 * @see org.springmodules.javaspaces.entry.UidFactory#generateUid()
	 */
	public Serializable generateUid() {
		return generator.generateTimeBasedUUID();
	}
	

}
