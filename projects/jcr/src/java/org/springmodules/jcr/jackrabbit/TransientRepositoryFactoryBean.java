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

import java.lang.reflect.Field;

import javax.jcr.Repository;

import org.apache.jackrabbit.api.JackrabbitRepository;
import org.apache.jackrabbit.core.TransientRepository;
import org.apache.jackrabbit.core.TransientRepository.RepositoryFactory;

/**
 * FactoryBean for creating Jackrabbit's TransientRepository (i.e. repository are initialized for the
 * first session and closed once the last session is closed.
 * 
 * @author Costin Leau
 * @since 0.5
 */
public class TransientRepositoryFactoryBean extends RepositoryFactoryBean {
	private RepositoryFactory repositoryFactory;

	/**
	 * @return Returns the repositoryFactory.
	 */
	public RepositoryFactory getRepositoryFactory() {
		return repositoryFactory;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springmodules.jcr.jackrabbit.RepositoryFactoryBean#createRepository()
	 */
	protected Repository createRepository() throws Exception {
		// use given factory
		if (repositoryFactory != null)
			return new TransientRepository(repositoryFactory);

		// fallback to discovered repository configuration
		return new TransientRepository(getRepositoryConfig());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springmodules.jcr.jackrabbit.RepositoryFactoryBean#destroy()
	 */
	public void destroy() throws Exception {
		// use introspection to call shutdown on the repo
		Field repositoryField = TransientRepository.class.getDeclaredField("repository");
		repositoryField.setAccessible(true);
		JackrabbitRepository repo = (JackrabbitRepository) repositoryField.get(repository);
		repo.shutdown();
	}

}
