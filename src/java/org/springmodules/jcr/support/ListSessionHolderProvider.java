/**
 * Created on Nov 10, 2005
 *
 * $Id: ListSessionHolderProvider.java,v 1.1 2005/11/11 15:47:09 costin Exp $
 * $Revision: 1.1 $
 */
package org.springmodules.jcr.support;

import java.util.List;

/**
 * List based implementation of SessionHolderProvider. This class should is intended mainly
 * for testing or for declaring SessionHolderProviders in Spring context files. 
 * 
 * @author Costin Leau
 *
 */
public class ListSessionHolderProvider extends CacheableSessionHolderProviderManager {

	private List providers;
	
	/**
	 * @see org.springmodules.jcr.support.AbstractSessionHolderProviderManager#getProviders()
	 */
	public List getProviders() {
		return providers;
	}

	/**
	 * @param providers The providers to set.
	 */
	public void setProviders(List providers) {
		this.providers = providers;
	}

}
