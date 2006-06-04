/**
 * Created on Mar 13, 2006
 *
 * $Id$
 * $Revision$
 */
package org.springmodules.jini;

import java.io.IOException;
import java.rmi.RemoteException;

import net.jini.core.entry.Entry;
import net.jini.core.lookup.ServiceRegistrar;
import net.jini.core.lookup.ServiceTemplate;
import net.jini.discovery.DiscoveryEvent;
import net.jini.discovery.DiscoveryListener;
import net.jini.discovery.LookupDiscovery;
import net.jini.lookup.entry.Name;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.core.NestedRuntimeException;
import org.springframework.remoting.RemoteAccessException;
import org.springframework.remoting.RemoteLookupFailureException;

/**
 * JiniServiceFactoryBean for JINI environments. The class is made up from various samples found on jini.org 
 * customized in a Spring specific way. The search will be executed using the provided ServiceTemplate
 * or, if it is null, one will be created using the serviceClass and serviceName. If the lookup operation
 * times out (30 seconds by default), a null service will be returned.
 * For most cases the serviceClass and serviceNames are enought and hide the jini details from the client. 
 * <p/>
 * The factoryBean can be configured to do a lookup each time before returning the object type by setting
 * the "singleton" property to false. 
 *   
 * @author Costin Leau
 *
 */
public class JiniServiceFactoryBean extends AbstractFactoryBean {
	private static final Log log = LogFactory.getLog(JiniServiceFactoryBean.class);

	private ServiceTemplate template;
	// utility properties
	private Class serviceClass;
	private String serviceName;

	private String[] groups = LookupDiscovery.ALL_GROUPS;
	// 30 secs
	private long timeout = 30 * 1000;
	// used to pass out information from inner classes
	private Object proxy;

	/**
	 * @see org.springmodules.beans.factory.FactoryBean#getObjectType()
	 */
	public Class getObjectType() {
		// try to discover the class type if possible to make it work with autowiring
		if (proxy == null) {
			// no template - look at serviceClass
			if (template == null) {
				return (serviceClass == null ? null : serviceClass);
			}

			// look at the template and
			// return the first class from the template (if there is one)
			if (template.serviceTypes != null && template.serviceTypes.length > 0)
				return template.serviceTypes[0];

		}
		return proxy.getClass();
	}

	/**
	 * @see org.springmodules.beans.factory.config.AbstractFactoryBean#createInstance()
	 */
	protected Object createInstance() throws Exception {
		ServiceTemplate templ;

		if (template == null) {
			Class[] types = (serviceClass == null ? null : new Class[] { serviceClass });
			Entry[] entry = (serviceName == null ? null : new Entry[] { new Name(serviceName) });

			templ = new ServiceTemplate(null, types, entry);
		}
		else
			templ = template;

		final ServiceTemplate finalTemplate = templ;

		LookupDiscovery lookupDiscovery = null;

		try {
			lookupDiscovery = new LookupDiscovery(groups);

			// hook listener for finding the service
			lookupDiscovery.addDiscoveryListener(new DiscoveryListener() {
				public void discovered(DiscoveryEvent ev) {
					log.debug("received discovery event " + ev);
					ServiceRegistrar[] reg = ev.getRegistrars();
					// once the proxy if found, bail out
					for (int i = 0; i < reg.length && proxy == null; i++) {
						findService(finalTemplate, reg[i]);
					}
				}

				public void discarded(DiscoveryEvent ev) {
				}
			});

			if (log.isDebugEnabled())
				log.debug("Awaiting discovery event...");

			if (proxy == null) {
				synchronized (this) {
					this.wait(timeout);
				}
			}

			if (log.isDebugEnabled())
				log.debug("Terminating discovery service");

		}
		catch (IOException e) {
			throw new RemoteLookupFailureException("cannot create lookup discovery", e);
		}
		catch (InterruptedException e) {
			throw new NestedRuntimeException("lookup interrupted", e) {
			};
		}
		finally {
			// make sure to close the lookup threads
			if (lookupDiscovery != null)
				lookupDiscovery.terminate();
		}

		return proxy;
	}

	/**
	 * Find the service and notify once it is found.
	 *
	 * @param templ
	 * @param lus
	 */
	private void findService(ServiceTemplate templ, ServiceRegistrar lus) {
		try {
			synchronized (this) {

				proxy = lus.lookup(templ);

				//System.out.println(lus.lookup(new ServiceTemplate(null,
				//		new Class[] { TransactionManager.class }, null)));

				if (proxy != null) {
					if (log.isDebugEnabled())
						log.debug("Discovered proxy " + proxy.getClass());
					this.notify();
				}
				else {
					if (log.isDebugEnabled())
						log.debug("template " + templ + " not found in registrar " + lus);
				}
			}
		}
		catch (RemoteException re) {
			throw new RemoteAccessException("can not find service", re);
		}
	}

	/**
	 * @return Returns the groups.
	 */
	public String[] getGroups() {
		return groups;
	}

	/**
	 * @param groups The groups to set.
	 */
	public void setGroups(String[] groups) {
		this.groups = groups;
	}

	/**
	 * @return Returns the serviceClass.
	 */
	public Class getServiceClass() {
		return serviceClass;
	}

	/**
	 * @param serviceClass The serviceClass to set.
	 */
	public void setServiceClass(Class serviceClass) {
		this.serviceClass = serviceClass;
	}

	/**
	 * @return Returns the serviceName.
	 */
	public String getServiceName() {
		return serviceName;
	}

	/**
	 * @param serviceName The serviceName to set.
	 */
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	/**
	 * @return Returns the template.
	 */
	public ServiceTemplate getTemplate() {
		return template;
	}

	/**
	 * @param template The template to set.
	 */
	public void setTemplate(ServiceTemplate template) {
		this.template = template;
	}

	/**
	 * @return Returns the timeout.
	 */
	public long getTimeout() {
		return timeout;
	}

	/**
	 * @param timeout The timeout to set.
	 */
	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

}
