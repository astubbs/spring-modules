/*
 * Copyright 2002-2007 the original author or authors.
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

package org.springmodules.template;

import org.springframework.core.io.Resource;

/**
 * @author Uri Boness
 */
public interface TemplateEngine {

    public final static String DEFAULT_ENCODING = "UTF-8";

    /**
     * Creates a new template out of the given resource. The default encoding of the
     * created template is <code>UTF-8</code>.
     *
     * @param resource The resource from which the template will be created.
     * @return The newly created template
     */
    Template createTemplate(Resource resource) throws TemplateCreationException;

    /**
     * Creates a new template out of the given resource and encoding.
     *
     * @param resource The resource from which the template will be created.
     * @param encoding The encoding of the text the template will generate.
     * @return The newly created template
     */
    Template createTemplate(Resource resource, String encoding) throws TemplateCreationException;

}