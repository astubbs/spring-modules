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

package org.springmodules.template.engine;

import org.springmodules.template.TemplateEngine;
import org.springmodules.template.Template;
import org.springframework.core.io.Resource;

/**
 * A base class for all {@link TemplateEngine} implementations.
 *
 * @author Uri Boness
 */
public abstract class AbstractTemplateEngine implements TemplateEngine {

    private String defaultEncoding = DEFAULT_ENCODING;

    /**
     * @see TemplateEngine#createTemplate(org.springframework.core.io.Resource)
     */
    public Template createTemplate(Resource resource) {
        return createTemplate(resource, defaultEncoding);
    }


    //================================================== Setter/Getter =================================================

    /**
     * The default encoding the created temlates will be associated with.
     *
      * @return The default encoding the created templates will be associated with.
     */
    public String getDefaultEncoding() {
        return defaultEncoding;
    }

    /**
     * Sets the default encoding with which the created template will be associated.
     *
     * @param defaultEncoding The default encoding with which the created template will be associated.
     */
    public void setDefaultEncoding(String defaultEncoding) {
        this.defaultEncoding = defaultEncoding;
    }

}
