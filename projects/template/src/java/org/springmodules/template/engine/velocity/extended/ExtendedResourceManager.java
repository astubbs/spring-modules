/*
 * Copyright 2002-2005 the original author or authors.
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

package org.springmodules.template.engine.velocity.extended;

import org.apache.velocity.exception.*;
import org.apache.velocity.runtime.resource.*;
import org.apache.velocity.runtime.resource.loader.*;

/**
 * An extention to Velocity's ResourceManagerImpl class that enable the
 * configuration of a single resource loader.
 *
 * @author Uri Boness
 */
public class ExtendedResourceManager extends ResourceManagerImpl {

    private ResourceLoader singleLoader;

    public ExtendedResourceManager() {
    }

    public ExtendedResourceManager(ResourceLoader singleLoader) {
        super();
        this.singleLoader = singleLoader;
    }

    protected Resource loadResource(String resourceName, int resourceType, String encoding) throws ResourceNotFoundException, ParseErrorException, Exception {

        if (singleLoader == null) {
            return super.loadResource(resourceName, resourceType, encoding);
        }

        Resource resource = ResourceFactory.getResource(resourceName, resourceType);

        resource.setRuntimeServices(rsvc);

        resource.setName( resourceName );
        resource.setEncoding( encoding );

        resource.setResourceLoader(singleLoader);

        if (!resource.process()) {
             return null;
        }

        long howOldItWas = singleLoader.getLastModified(resource);
        resource.setLastModified(howOldItWas);
        resource.setModificationCheckInterval(singleLoader.getModificationCheckInterval());

        resource.touch();

        return resource;
    }


}
