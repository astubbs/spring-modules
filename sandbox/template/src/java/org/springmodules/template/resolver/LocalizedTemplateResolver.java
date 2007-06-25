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

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.LocalizedResourceHelper;

/**
 * A {@link org.springmodules.template.TemplateResolver template resolver} implementation that resolves different
 * templates based on the name, encoding, and current locale. This template uses spring's (@link LocaleContextHolder}
 * to figure out the current locale. {@link org.springframework.core.io.support.LocalizedResourceHelper} is used
 * internally to resolve the template resources. Please see
 * {@link LocalizedResourceHelper#findLocalizedResource(String, String, java.util.Locale)}'s javadoc for a detailed
 * explaination of the resources naming convensions.
 *
 * @author Uri Boness
 */
public class LocalizedTemplateResolver extends CachingTemplateResolver {

    private LocalizedResourceHelper helper;

    private String prefix = "";
    private String extension;

    /**
     * Constructs a new LocalizedTemplateResolver.
     */
    public LocalizedTemplateResolver() {
        this.helper = new LocalizedResourceHelper(getResourceLoader());
    }

    /**
     * Loads the template resource based on the given template name and encoding. The configured prefix will be
     * prepended to the given name before delegating to {@link LocalizedResourceHelper}.
     *
     * @param name The name of the template.
     * @param encoding The encoding of the template.
     * @return The loaded template resource.
     */
    protected Resource loadTemplateResource(String name, String encoding) {
        Locale locale = LocaleContextHolder.getLocale();
        return helper.findLocalizedResource(prefix + name, extension, locale);
    }


    //============================================== Setter/Getter =====================================================

    /**
     * Sets the prefix to be prepended to the template name before resolving takes place.
     *
     * @param prefix The prefix to be prepended to the template name before resolving takes place.
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    /**
     * Returns the prefix to be prepended to the template name before resolving takes place.
     *
     * @return The prefix to be prepended to the template name before resolving takes place.
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * Sets the extension that will be appended to the name (and locale) while resolving the template resource).
     *
     * @param extension The extension that will be appended to the name (and locale) while resolving the template resource).
     */
    public void setExtension(String extension) {
        this.extension = extension;
    }

    /**
     * Returns the extension that will be appended to the name (and locale) while resolving the template resource.
     *
     * @return The extension that will be appended to the name (and locale) while resolving the template resource.
     */
    public String getExtension() {
        return extension;
    }
}
