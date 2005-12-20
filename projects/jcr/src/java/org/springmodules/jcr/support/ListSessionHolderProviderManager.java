/**
 * Created on Nov 10, 2005
 *
 * $Id: ListSessionHolderProviderManager.java,v 1.1 2005/12/20 17:38:16 costin Exp $
 * $Revision: 1.1 $
 */
package org.springmodules.jcr.support;

import java.util.Collections;
import java.util.List;

/**
 * List based implementation of SessionHolderProviderManager. This class should is intended mainly
 * for testing or for declaring SessionHolderProviders in Spring context files. 
 * 
 * @author Costin Leau
 *
 */
public class ListSessionHolderProviderManager extends CacheableSessionHolderProviderManager {

	private List providers = Collections.EMPTY_LIST;
	
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
