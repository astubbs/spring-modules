/*
 * Copyright 2002-2006 the original author or authors.
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
package org.springmodules.jcr.jackrabbit;

import java.rmi.registry.Registry;

import javax.jcr.Repository;

import org.apache.jackrabbit.rmi.remote.RemoteRepository;
import org.apache.jackrabbit.rmi.server.RemoteAdapterFactory;
import org.apache.jackrabbit.rmi.server.ServerAdapterFactory;
import org.springframework.beans.factory.config.AbstractFactoryBean;

/**
 * FactoryBean for creating Jackrabbit RMI remote repository. Use Spring's 
 * RmiRegistryFactoryBean for retriving/creating a Rmi Registry used for binding
 * the repository.
 * Consider Spring's remoting capabilities when dealing with Serializable objects.
 * 
 * @see org.springframework.remoting.rmi.RmiRegistryFactoryBean
 * @author Costin Leau
 * 
 */
public class RmiServerRepositoryFactoryBean extends AbstractFactoryBean {
	private RemoteRepository remoteRepository;
	private Repository repository;
	private RemoteAdapterFactory remoteAdapterFactory;
	private Registry registry;
	private String rmiName;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.beans.factory.config.AbstractFactoryBean#createInstance()
	 */
	protected Object createInstance() throws Exception {
		if (repository == null)
			throw new IllegalArgumentException("repository property is required");
		if (remoteAdapterFactory == null)
			throw new IllegalArgumentException("remoteAdapterFactory property is required");
		if (registry == null)
			throw new IllegalArgumentException("registry property is required");
		if (rmiName == null)
			throw new IllegalArgumentException("rmiName property is required");

		remoteRepository = remoteAdapterFactory.getRemoteRepository(repository);

		registry.rebind(rmiName, remoteRepository);

		return remoteRepository;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.beans.factory.FactoryBean#getObjectType()
	 */
	public Class getObjectType() {
		return (remoteRepository == null ? RemoteRepository.class : remoteRepository.getClass());
	}

	/**
	 * @param registry The registry to set.
	 */
	public void setRegistry(Registry registry) {
		this.registry = registry;
	}

	/**
	 * @param remoteRepository The remoteRepository to set.
	 */
	public void setRemoteRepository(RemoteRepository remoteRepository) {
		this.remoteRepository = remoteRepository;
	}

	/**
	 * @param repository The repository to set.
	 */
	public void setRepository(Repository repository) {
		this.repository = repository;
	}

	/**
	 * @param rmiName The rmiName to set.
	 */
	public void setRmiName(String rmiName) {
		this.rmiName = rmiName;
	}

	/**
	 * @param remoteAdapterFactory The remoteAdapterFactory to set.
	 */
	public void setRemoteAdapterFactory(RemoteAdapterFactory remoteAdapterFactory) {
		this.remoteAdapterFactory = remoteAdapterFactory;
	}

}
