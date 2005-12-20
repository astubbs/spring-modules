package org.springmodules.jcr;


/**
 * This manager returns the approapriate sessionHolderProvider for the given sessionFactory.
 * Usually implementations will rely on the wrapped Repository name inside the sessionFactory
 * to retrieve the sessionHolderProvider.
 * 
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
	 * Returns the SessionHolderProvider suitable for the given Jcr SessionFactory.
	 * @param sessionFactory
	 * @return
	 */
	public SessionHolderProvider getSessionProvider(SessionFactory sessionFactory);

}