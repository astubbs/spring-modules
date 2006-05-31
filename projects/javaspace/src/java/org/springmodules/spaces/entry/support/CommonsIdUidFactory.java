/**
 * 
 */
package org.springmodules.spaces.entry.support;

import java.io.Serializable;

import org.apache.commons.id.IdentifierGenerator;
import org.apache.commons.id.IdentifierUtils;
import org.springmodules.spaces.entry.UidFactory;

/**
 * UidFactory implementation based on Apache commons-id (which is still part of
 * the sandbox).
 * 
 * @author Costin Leau
 * 
 */
public class CommonsIdUidFactory implements UidFactory {
	private IdentifierGenerator generator = IdentifierUtils.UUID_VERSION_FOUR_GENERATOR;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springmodules.spaces.entry.UidFactory#generateUid()
	 */
	public Serializable generateUid() {
		return (Serializable) generator.nextIdentifier();
	}

}
