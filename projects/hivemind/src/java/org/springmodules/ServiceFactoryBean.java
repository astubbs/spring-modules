/*
 * Copyright 2002-2004 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springmodules.hivemind;

import org.apache.hivemind.Registry;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContextException;
import org.springframework.util.Assert;

/**
 * <code>FactoryBean</code> implementation that acts as an adapter to a HiveMind <code>Registry</code>
 * allowing for any HiveMind service to be exposed as a Spring bean.
 * <p/>
 * Both the <code>serviceInterface</code> and <code>registry</code> properties are required. By default,
 * services are retreived from the HiveMind <code>Registry</code> using the service interface only.
 * If a value is supplied for the <code>serviceName</code> property, then both the service interface
 * and service name will be used when looking for the service in HiveMind.
 *
 * @author Rob Harrop
 * @author Thierry Templier
 * @see Registry
 * @see RegistryFactoryBean
 * @see #setServiceInterface(Class)
 * @see #setServiceName(String)
 * @see #setRegistry(org.apache.hivemind.Registry)
 */
public class ServiceFactoryBean implements FactoryBean, InitializingBean {

	/**
	 * The HiveMind <code>Registry</code> from which services are accessed.
	 */
	private Registry registry;

	/**
	 * The name of the HiveMind service to access.
	 */
	private String serviceName;

	/**
	 * The <code>Class</code> name of the of the HiveMind service.
	 */
	private Class serviceInterface;

	/**
	 * Sets the interface of the HiveMind service to be accessed.
	 */
	public void setServiceInterface(Class serviceInterface) {
		Assert.isTrue(serviceInterface.isInterface(), "Cannot use a Class for the service interface");
		this.serviceInterface = serviceInterface;
	}

	/**
	 * Sets the service name of the HiveMind service to be accessed.
	 */
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	/**
	 * Sets the HiveMind <code>Registry</code> used to load services.
	 */
	public void setRegistry(Registry registry) {
		this.registry = registry;
	}

	/**
	 * Gets the Hivemind service from the <code>Registry</code>.
	 */
	public Object getObject() throws Exception {
		return getServiceObject();
	}

	public Class getObjectType() {
		// can we use singletons and cache here?
		return getServiceObject().getClass();
	}

	public boolean isSingleton() {
		return false;
	}

	/**
	 * Checks if the registry and serviceInterface properties are specified. If no corresponding service is
	 * configured in the Hivemind <code>Registry</code>, an <code>ApplicationContextException</code> is thrown.
	 */
	public void afterPropertiesSet() throws Exception {
		if (registry == null) {
			throw new ApplicationContextException("Property [registry] of class [" + ServiceFactoryBean.class
					+ "] is required.");
		}

		if (serviceInterface == null) {
			throw new ApplicationContextException("Property [serviceInterface] of class [" + ServiceFactoryBean.class
					+ "] is required.");
		}

		// check that the service exists for fail fast behaviour
		boolean containsService = false;

		if (serviceName == null) {
			containsService = registry.containsService(serviceInterface);
		}
		else {
			containsService = registry.containsService(serviceName, serviceInterface);
		}

		if (!containsService) {
			throw new ApplicationContextException("Service with interface [" + serviceInterface.getName() +
					"] and name [" + serviceName + "] is not present in registry.");
		}
	}

	/**
	 * Get the Hivemind service from the <code>Registry</code>.
	 */
	private Object getServiceObject() {
		if (serviceName == null) {
			return registry.getService(serviceInterface);
		}
		else {
			return registry.getService(serviceName, serviceInterface);
		}
	}
}
