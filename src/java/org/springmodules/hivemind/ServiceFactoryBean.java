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

/**
 * @author Rob Harrop
 */
public class ServiceFactoryBean implements FactoryBean, InitializingBean {

	private Registry registry;

	private String serviceName;

	private Class serviceInterface;

	public void setServiceInterface(Class serviceInterface) {
		this.serviceInterface = serviceInterface;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public void setRegistry(Registry registry) {
		this.registry = registry;
	}


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

	private Object getServiceObject() {
		if (serviceName == null) {
			return registry.getService(serviceInterface);
		}
		else {
			return registry.getService(serviceName, serviceInterface);
		}
	}
}
