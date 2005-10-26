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

package org.springmodules.template.resolvers;

import java.util.*;

import org.springframework.core.io.*;
import org.springmodules.template.*;
import org.springmodules.template.sources.*;

/**
 * A {@link java.util.Map} based TemplateSourceResolver. The key's of the map are expected to be the names
 * of the template sources, and the values are expected to be of type {@link Resource}.
 *
 * @author Uri Boness
 */
public class ResourceMapTemplateSourceResolver implements TemplateSourceResolver {

    private Map resouceByName;

    public ResourceMapTemplateSourceResolver() {
        resouceByName = new HashMap();
    }

    public TemplateSource resolveTemplateSource(String name) {
        Resource resource = (Resource)resouceByName.get(name);
        if (resource == null) {
            return null;
        }
        return new ResourceTemplateSource(resource);
    }

    public void setResouceByName(Map resouceByName) {
        this.resouceByName = resouceByName;
    }

    public void addResource(String name, Resource resource) {
        resouceByName.put(name, resource);
    }
}
