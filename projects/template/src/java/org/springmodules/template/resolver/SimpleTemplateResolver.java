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

package org.springmodules.template.resolver;

import java.util.Locale;

import org.springframework.core.io.Resource;

/**
 * A simple {@link org.springmodules.template.TemplateResolver template resolver} that may be configured with a
 * prefix and a suffix to be prepended and/or appended to the template name prior to resolving process. Note, this
 * class extends {@link org.springmodules.template.resolver.CachingTemplateResolver} so templates will be cached by
 * their names once resolved.
 *
 * @author Uri Boness
 */
public class SimpleTemplateResolver extends CachingTemplateResolver {

    private String prefix;

    /**
     * Constructs a new SimpleTemplateResolver with no prefix or suffix.
     */
    public SimpleTemplateResolver() {
        this("");
    }

    /**
     * Constructs a new SimpleTemplateResolver with given prefix and suffix.
     *
     * @param prefix A prefix to be prepended to the template name prior to resolving process.
     */
    public SimpleTemplateResolver(String prefix) {
        this.prefix = prefix;
    }

    /**
     * Prepends/Appends the configured prefix/suffix to the given template name and loads the appropriate template
     * resource.
     *
     * @param name The name of the template.
     * @param encoding The encoding of the loaded template resource.
     * @param locale The locale of the template.
     * @return The template resource.
     * @see org.springmodules.template.resolver.BasicTemplateResolver#loadTemplateResource(String, String, Locale)
     */
    protected Resource loadTemplateResource(String name, String encoding, Locale locale) {
        return super.loadTemplateResource(prefix + name, encoding, locale);
    }


    //============================================== Setter/Getter =====================================================

    /**
     * Sets the prefix to be prepended to the template name prior to resolving process.
     *
     * @param prefix The prefix to be prepended to the template name prior to resolving process.
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    /**
     * Returns the configured prefix.
     *
     * @return The configured prefix.
     */
    public String getPrefix() {
        return prefix;
    }

}
