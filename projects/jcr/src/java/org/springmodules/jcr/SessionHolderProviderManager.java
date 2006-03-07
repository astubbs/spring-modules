package org.springmodules.jcr;

import javax.jcr.Repository;


/**
 * This manager returns the approapriate sessionHolderProvider for the given repository.
 * See the implementations of the interface for more details.
 * 
 * <strong>NOTE</strong> one of the reason for this interface was to allow dynamical discovery
 * of SessionHolderProviders for specific JSR-170 implementations at runtime.
 * 
 * @author Costin Leau
 *
 */
public interface SessionHolderProviderManager {

	/**
	 * Returns the SessionHolderProvider suitable for the given Jcr Repository.
	 * @param repository
	 * @return
	 */
	public SessionHolderProvider getSessionProvider(Repository repository);

}