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

import java.io.IOException;
import java.net.URL;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hivemind.ClassResolver;
import org.apache.hivemind.Registry;
import org.apache.hivemind.Resource;
import org.apache.hivemind.impl.DefaultClassResolver;
import org.apache.hivemind.impl.RegistryBuilder;
import org.apache.hivemind.util.ClasspathResource;
import org.apache.hivemind.util.FileResource;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContextException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.Assert;

/**
 * <code>FactoryBean</code> implementation that creates a HiveMind <code>Registry</code>.
 * <p/>
 * By default, service configuration is loaded from the standard META-INF/hivemodule.xml file.
 * However, this can be overriden using the <code>setConfigLocations</code> method, and service
 * configuration can be loaded from an array of arbitrary locations.
 *
 * @author Rob Harrop
 * @author Thierry Templier
 * @see Registry
 * @see #setConfigLocations(org.springframework.core.io.Resource[]) 
 */
public class RegistryFactoryBean implements FactoryBean, InitializingBean, DisposableBean {

	/**
	 * <code>Log</code> instance used by this class.
	 */
	private static final Log log = LogFactory.getLog(RegistryFactoryBean.class);

	/**
	 * The HiveMind <code>Registry</code> instance managed by this class.
	 */
	private Registry registry;

	/**
	 * Gets the HiveMind <code>Registry</code> instance managed by this <code>FactoryBean</code>.
	 */
	public Object getObject() throws Exception {
		return this.registry;
	}

	/**
	 * Always returns <code>Registry.class</code>.
	 */
	public Class getObjectType() {
		return Registry.class;
	}

	/**
	 * Always returns <code>true</code>.
	 */
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
			return new ClasspathResourceAdapter(resolver, classpathConfigLocation);
		}
		else {
			return new DefaultResourceAdapter(configLocation);
		}
	}

	/**
	 * If no <code>Registry</code> exists constructs the default
	 * <code>Registry</code>.
	 */
	public void afterPropertiesSet() throws Exception {

		if (this.registry == null) {
			log.info("Loading default HiveMind registry.");
			this.registry = RegistryBuilder.constructDefaultRegistry();
		}

		log.info("Hivemind registry loaded");
	}

	/**
	 * Sets the locations of the Hivemind configuration files, configures a <code>RegistryBuilder</code>
	 * with these locations and creates the Hivemind <code>Registry</code>.
	 *
	 * @param configLocations locations of the Hivemind configuration files
	 */
	public void setConfigLocations(org.springframework.core.io.Resource[] configLocations) {
		Assert.notNull(configLocations, "configLocations cannot be null");

		RegistryBuilder builder = new RegistryBuilder();
		ClassResolver resolver = new DefaultClassResolver();

		log.info("Loading standard Hivemind modules");
		builder.processModules(resolver);

		for (int index = 0; index < configLocations.length; index++) {
			if (log.isInfoEnabled()) {
				log.info("Loading module : " + configLocations[index]);
			}

			builder.processModule(resolver, wrapSpringResource(resolver, configLocations[index]));
		}

		this.registry = builder.constructRegistry(Locale.getDefault());

	}

	/**
	 * Shuts down the internal HiveMind <code>Registry</code>.
	 */
	public void destroy() throws Exception {
		if (registry != null) {
			registry.shutdown();
			log.info("Hivemind registry shutdown");
		}
	}

	/**
	 * Adapter class that adapts a Spring <code>Resource</code> to a
	 * a HiveMind <code>FileResource</code>. Used for all non-classpath
	 * resources.
	 */
	private static class DefaultResourceAdapter extends FileResource {

		/**
		 * The wrapped Spring <code>ClassPathResource</code>.
		 */
		private org.springframework.core.io.Resource resource;

		/**
		 * Constructor of the wrapper.
		 *
		 * @param resource the Spring resource to wrap
		 */
		public DefaultResourceAdapter(org.springframework.core.io.Resource resource) {
			super(resource.getFilename());
			this.resource = resource;
		}

		/**
		 * @see org.apache.hivemind.Resource#getLocalization(java.util.Locale)
		 */
		public Resource getLocalization(Locale locale) {
			return super.getLocalization(locale);
		}

		/**
		 * Gets the URL of the resource from the internal Spring resource.
		 *
		 * @return the URL
		 * @see org.apache.hivemind.Resource#getResourceURL()
		 */
		public URL getResourceURL() {
			try {
				return resource.getURL();
			}
			catch (IOException ex) {
				throw new ApplicationContextException("Error during getting the url of the resource");
			}
		}

		/**
		 * Uses to create a new resource from an other one. It uses the internal
		 * Spring resource and its <code>createRelative</code> method.
		 *
		 * @return the new resource
		 * @see org.apache.hivemind.util.AbstractResource#newResource(java.lang.String)
		 */
		protected Resource newResource(String name) {
			try {
				return new DefaultResourceAdapter(this.resource.createRelative(name));
			}
			catch (IOException ex) {
				throw new ApplicationContextException("Error during creation of a new resource");
			}
		}

	}

	/**
	 * Adapter class that makes a Spring <code>ClassPathResource</code> act
	 * like a HiveMind <code>ClasspathResource</code>.
	 */
	private static class ClasspathResourceAdapter extends ClasspathResource {

		/**
		 * The Hivemind <code>ClassResolver</code>.
		 */
		private ClassResolver classResolver;

		/**
		 * The wrapped Spring <code>ClassPathResource</code>.
		 */
		private ClassPathResource resource;

		/**
		 * Constructor of the wrapper. This constructor needs to have
		 * an Hivemind <code>ClassResolver</code>.
		 *
		 * @param classResolver the Hivemind class resolver
		 * @param resource the Spring resource to wrap
		 */
		public ClasspathResourceAdapter(ClassResolver classResolver, ClassPathResource resource) {
			super(classResolver, resource.getFilename());
			this.classResolver = classResolver;
			this.resource = resource;
		}

		/**
		 * @see org.apache.hivemind.Resource#getLocalization(java.util.Locale)
		 */
		public Resource getLocalization(Locale locale) {
			return super.getLocalization(locale);
		}

		/**
		 * Gets the <code>URL</cide> of the resource from the internal Spring resource.
		 *
		 * @return the URL
		 * @see org.apache.hivemind.Resource#getResourceURL()
		 */
		public URL getResourceURL() {
			try {
				return resource.getURL();
			}
			catch (IOException ex) {
				throw new ApplicationContextException("Error getting the url of the resource ["
						+ resource.getDescription() + "]", ex);
			}
		}

		/**
		 * Uses to create a new resource from an other one. It uses the internal
		 * Spring resource and its <code>createRelative</code> method.
		 *
		 * @return the new resource
		 * @see org.apache.hivemind.util.AbstractResource#newResource(java.lang.String)
		 */
		protected Resource newResource(String name) {
			return new ClasspathResourceAdapter(classResolver,
					(ClassPathResource) this.resource.createRelative(name));
		}
	}

}
