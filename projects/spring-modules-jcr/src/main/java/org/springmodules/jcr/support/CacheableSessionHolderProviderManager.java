/**
 * Created on Nov 10, 2005
 *
 * $Id: CacheableSessionHolderProviderManager.java,v 1.3 2006/03/07 13:41:07 costin Exp $
 * $Revision: 1.3 $
 */
package org.springmodules.jcr.support;

import java.util.Map;

import javax.jcr.Repository;

import org.springframework.util.CachingMapDecorator;
import org.springmodules.jcr.SessionHolderProvider;

/**
 * Manager which caches providers in order to avoid lookups.
 * 
 * @author Costin Leau
 *
 */
public abstract class CacheableSessionHolderProviderManager extends AbstractSessionHolderProviderManager {

	/**
	 * Caching class based on CachingMapDecorator from main Spring distribution.
	 * 
	 * @author Costin Leau
	 *
	 */
	protected class ProvidersCache extends CachingMapDecorator {
		private ProvidersCache() {
			super(true);
		}

		/**
		 * @see org.springframework.util.CachingMapDecorator#create(java.lang.Object)
		 */
		protected Object create(Object key) {
			return parentLookup((Repository) key);
		}

	}

	/**
	 * Providers cache.
	 */
	private final Map providersCache = new ProvidersCache();

	/**
	 * Method for retrieving the parent functionality.
	 * 
	 * @param sf
	 * @return
	 */
	private SessionHolderProvider parentLookup(Repository repository) {
		return super.getSessionProvider(repository);
	}

	/**
	 * Overwrite the method to provide caching.
	 * 
	 * @see org.springmodules.jcr.support.AbstractSessionHolderProviderManager#getSessionProvider(Repository)
	 */
	public SessionHolderProvider getSessionProvider(Repository repository) {
		return (SessionHolderProvider) providersCache.get(repository);
	}

}
