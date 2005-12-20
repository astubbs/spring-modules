/**
 * Created on Nov 10, 2005
 *
 * $Id: CacheableSessionHolderProviderManager.java,v 1.1 2005/12/20 17:38:14 costin Exp $
 * $Revision: 1.1 $
 */
package org.springmodules.jcr.support;

import java.util.Map;

import org.springframework.util.CachingMapDecorator;
import org.springmodules.jcr.SessionFactory;
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
			return parentLookup((SessionFactory) key);
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
	private SessionHolderProvider parentLookup(SessionFactory sf) {
		return super.getSessionProvider(sf);
	}

	/**
	 * Overwrite the method to provide caching.
	 * 
	 * @see org.springmodules.jcr.support.AbstractSessionHolderProviderManager#getSessionProvider(org.springmodules.jcr.SessionFactory)
	 */
	public SessionHolderProvider getSessionProvider(SessionFactory sessionFactory) {
		return (SessionHolderProvider) providersCache.get(sessionFactory);
	}

}
