/**
 * Created on Nov 10, 2005
 *
 * $Id: ServiceSessionHolderProviderManager.java,v 1.1 2005/12/20 17:38:14 costin Exp $
 * $Revision: 1.1 $
 */
package org.springmodules.jcr.support;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.springmodules.jcr.SessionHolderProvider;

import sun.misc.Service;
import sun.misc.ServiceConfigurationError;

/**
 * Implementation of SessionHolderProviderManager which does dynamic discovery of the providers
 * using the JDK 1.3+ <a href="http://java.sun.com/j2se/1.3/docs/guide/jar/jar.html#Service%20Provider">
 * 'Service Provider' specification</a>.
 *  
 * The class will look for org.springmodules.jcr.SessionHolderProvider property files in
 * META-INF/services directories.
 * 
 * @author Costin Leau
 *
 */
public class ServiceSessionHolderProviderManager extends CacheableSessionHolderProviderManager {

	/**
	 * Loads the service providers using the discovery mechanism.
	 * 
	 * @return the list of service providers found.
	 */
	public List getProviders() {
		Iterator i = Service.providers(SessionHolderProvider.class,
				Thread.currentThread().getContextClassLoader());
		List providers = new ArrayList();
		for (; i.hasNext();) {
			try {
				providers.add(i.next());
			}
			catch (ServiceConfigurationError sce) {
				if (!(sce.getCause() instanceof SecurityException))
					throw sce;
			}
		}
		return Collections.unmodifiableList(providers);
	}
}
