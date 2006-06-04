/**
 * 
 */
package org.springmodules.javaspaces.entry.support;

import java.io.Serializable;

import org.springmodules.javaspaces.entry.UidFactory;

import com.activescript.GUID.GUIDException;
import com.activescript.GUID.GUIDGenerator;

/**
 * ActiveScript jguid generator.
 * 
 * @author Costin Leau
 *
 */
public class JGUidFactory implements UidFactory {

	private GUIDGenerator generator;
	
	/* (non-Javadoc)
	 * @see org.springmodules.javaspaces.entry.UidFactory#generateUid()
	 */
	public Serializable generateUid() {
		return generator.getUUID();
	}
	
	public JGUidFactory()
	{
		try {
			generator = new GUIDGenerator();
		}
		catch (GUIDException e) {
			throw new RuntimeException(e);
		}
	}

}
