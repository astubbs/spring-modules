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

package org.springmodules.template.sources;

import java.io.*;

import org.springframework.core.io.*;
import org.springmodules.template.*;

/**
 * A Spring Resource based template source.
 *
 * @author Uri Boness
 */
public class ResourceTemplateSource extends AbstractTemplateSource {

    private Resource resource;

    /**
     * Constructs a template source with the given resource.
     *
     * @param resource The resource to be used by this template source.
     */
    public ResourceTemplateSource(Resource resource) {
        super(resource.getFilename());
        this.resource = resource;
    }

    /**
     * Constructs a template source with the given resource and associates it with the given name.
     *
     * @param name The name of this template source.
     * @param resource The resource to be used by this template source.
     */
    public ResourceTemplateSource(String name, Resource resource) {
        super(name);
        this.resource = resource;
    }

    /**
     * Returns a reader for this source.
     *
     * @return A reader for this source.
     */
    public Reader getReader() throws IOException {
        return new InputStreamReader(resource.getInputStream());
    }

    public InputStream getInputStream() throws IOException {
        return resource.getInputStream();
    }

}
