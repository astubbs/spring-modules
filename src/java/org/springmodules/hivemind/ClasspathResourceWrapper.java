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

import org.apache.hivemind.ClassResolver;
import org.apache.hivemind.Resource;
import org.apache.hivemind.util.ClasspathResource;

import org.springframework.context.ApplicationContextException;
import org.springframework.core.io.ClassPathResource;

/**
 * Default wrapper on a Spring resource. It extends the Hivemind
 * <code>ClasspathResource</code> to delegate the resource management
 * to Spring in the case of classpath resource.
 * 
 * @author Thierry Templier
 */
public class ClasspathResourceWrapper extends ClasspathResource {

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
	public ClasspathResourceWrapper(ClassResolver classResolver, ClassPathResource resource) {
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
	 * Gets the URL of the resource from the internal Spring resource. 
	 * @see org.apache.hivemind.Resource#getResourceURL()
	 * 
	 * @return the URL
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
	 * @see org.apache.hivemind.util.AbstractResource#newResource(java.lang.String)
	 * 
	 * @return the new resource
	 */
	protected Resource newResource(String name) {
		return new ClasspathResourceWrapper(classResolver,
				(ClassPathResource) this.resource.createRelative(name));
	}
}
