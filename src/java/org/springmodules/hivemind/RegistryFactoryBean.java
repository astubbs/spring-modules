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

import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hivemind.ClassResolver;
import org.apache.hivemind.Registry;
import org.apache.hivemind.Resource;
import org.apache.hivemind.impl.DefaultClassResolver;
import org.apache.hivemind.impl.RegistryBuilder;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.ClassPathResource;

/**
 * @author Rob Harrop
 * @author Thierry Templier
 */
public class RegistryFactoryBean implements FactoryBean, InitializingBean {

	private static final Log log = LogFactory.getLog(RegistryFactoryBean.class);

	private Registry registry;

	private org.springframework.core.io.Resource[] configLocations;

	public Object getObject() throws Exception {
		return this.registry;
	}

	public Class getObjectType() {
		return Registry.class;
	}

	public boolean isSingleton() {
		return true;
	}

	/**
	 * Wrap the spring resource into an hivemind resource.
	 *
	 * @param resolver the hivemind class resolver
	 * @param configLocation the configuration location resource
	 * @return the wrapped resource
	 */
	private Resource wrapSpringResource(ClassResolver resolver, org.springframework.core.io.Resource configLocation) {
		if (configLocation instanceof ClassPathResource) {
			ClassPathResource classpathConfigLocation = (ClassPathResource) configLocation;
			return new ClasspathResourceWrapper(resolver, classpathConfigLocation);
		}
		else {
			return new DefaultResourceWrapper(configLocation);
		}
	}

	public void afterPropertiesSet() throws Exception {
		if (configLocations != null) {
			RegistryBuilder builder = new RegistryBuilder();
			ClassResolver resolver = new DefaultClassResolver();

			log.info("Loading standard Hivemind modules");
			builder.processModules(resolver);
			for (int index = 0; index < configLocations.length; index++) {
				builder.processModule(resolver, wrapSpringResource(resolver, configLocations[index]));
			}

			this.registry = builder.constructRegistry(Locale.getDefault());
		}
		else {
			this.registry = RegistryBuilder.constructDefaultRegistry();
		}
		log.info("Hivemind registry loaded");
	}

	public void setConfigLocations(org.springframework.core.io.Resource[] configLocations) {
		this.configLocations = configLocations;
	}

}
