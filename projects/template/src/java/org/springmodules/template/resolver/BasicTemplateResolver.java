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

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.LocalizedResourceHelper;
import org.springframework.util.Assert;
import org.springmodules.template.Template;
import org.springmodules.template.TemplateEngine;
import org.springmodules.template.TemplateResolver;

/**
 * A basic implementation of {@link org.springmodules.template.TemplateResolver} which uses a {@link ResourceLoader} to
 * load resource by the template name. The template resources are then passed to a configured {@link TemplateEngine}
 * which in turn creates the appropriate template. Although this class can be used as is, it would be wise to check
 * out its sub-classes, specially those derived from {@link CachingTemplateResolver}.
 *
 * @author Uri Boness
 */
public class BasicTemplateResolver implements TemplateResolver, ResourceLoaderAware, InitializingBean {

    private final static String EXTENSION_SEPARATOR = ".";

    /**
     * The default encoding that will be used (UTF-8)
     */
    protected final static String DEFAULT_ENCODING = "UTF-8";

    private TemplateEngine engine;

    private ResourceLoader resourceLoader;

    private LocalizedResourceHelper localizedResourceHelper;

    private boolean resolveLocalFromContextWhenAbsent;

    private String extension;

    /**
     * Constructs a new BasicTemplateResolver with no template engine and the {@link DefaultResourceLoader} as the
     * resource loader.
     */
    public BasicTemplateResolver() {
        this(null);
    }

    /**
     * Constructs a new BasicTemplateResolver with a given template engine and the {@link DefaultResourceLoader} as the
     * resource loader.
     *
     * @param engine The template engine that will be used to generate the appropriate templates.
     */
    public BasicTemplateResolver(TemplateEngine engine) {
        this(engine, new DefaultResourceLoader());
    }

    /**
     * Constructs a new BasicTemplateResolver with given template engine and resource loader.
     *
     * @param engine The template engine that will be used to generate the appropriate template.
     * @param resourceLoader The resource loader that will load the template resources by their names.
     */
    public BasicTemplateResolver(TemplateEngine engine, ResourceLoader resourceLoader) {
        this.engine = engine;
        this.resourceLoader = resourceLoader;
    }

    /**
     * Resolves the appropriate template based on the given template name. This method uses UTF-8 as the default
     * encoding.
     *
     * @param name The name of the template to be resolved.
     * @return The resolved template.
     */
    public final Template resolve(String name) {
        return resolve(name, DEFAULT_ENCODING);
    }

    public Template resolve(String name, Locale locale) {
        return resolve(name, DEFAULT_ENCODING, locale);
    }

    /**
     * Resolves the template based on the given template name and encoding.
     *
     * @param name The name of the template to be resolved.
     * @param encoding The encoding of the resolved template.
     * @return The resolved template.
     */
    public Template resolve(String name, String encoding) {
        return resolve(name, encoding, null);
    }

    public Template resolve(String name, String encoding, Locale locale) {
        Resource resource = loadTemplateResource(name, encoding, locale);
        return engine.createTemplate(resource, encoding);
    }

    public final void afterPropertiesSet() throws Exception {
        doAfterPropertiesSet();
        Assert.notNull(resourceLoader, "Property 'resourceLoader' is required");
        Assert.notNull(engine, "Property 'engine' is required");
        localizedResourceHelper = new LocalizedResourceHelper(resourceLoader);
    }

    /**
     * Can be overriden by sub-classes for initialization actions.
     */
    protected void doAfterPropertiesSet() {
    }

    /**
     * Loads the template resource based on the given name and encoding.
     *
     * @param name The name of the template.
     * @param encoding The encoding of the template.
     * @return The loaded template resource.
     */
    protected Resource loadTemplateResource(String name, String encoding, Locale locale) {
        String ext = extension;
        if (ext == null) {
            String[] nameAndExtension = parseNameWithExtension(name);
            name = nameAndExtension[0];
            ext = nameAndExtension[1];
        }
        if (locale == null && resolveLocalFromContextWhenAbsent) {
            locale = LocaleContextHolder.getLocale();
        }
        if (locale == null) {
            return resourceLoader.getResource(name + ext);
        }
        return localizedResourceHelper.findLocalizedResource(name, ext, locale);
    }

    protected String[] parseNameWithExtension(String name) {
        int pos = name.lastIndexOf(EXTENSION_SEPARATOR);
        return new String[] {
            name.substring(0, pos),
            name.substring(pos)
        };
    }


    //============================================== Setter/Getter =====================================================

    /**
     * Sets the template engine to be used to load and process the resolved template.
     *
     * @param engine The template engine to be used to load and process the resolved template.
     */
    public void setEngine(TemplateEngine engine) {
        this.engine = engine;
    }

    /**
     * Returns the template engine that is used to load and process the resolved tempalatel
     *
     * @return The template engine that is used to load and process the resolved tempalatel
     */
    public TemplateEngine getEngine() {
        return engine;
    }

    /**
     * Sets the resource loader that will be used to load the template resources.
     *
     * @param resourceLoader The resource loader that will be used to load the template resources.
     * @see org.springframework.context.ResourceLoaderAware#setResourceLoader(org.springframework.core.io.ResourceLoader)
     */
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    /**
     * Returns the resource loader used to load the template resources.
     *
     * @return The resource loader used to load the template resources.
     */
    protected ResourceLoader getResourceLoader() {
        return resourceLoader;
    }

    /**
     * Sets the extension of the template. The extension is requied for localization support. Basically, when
     * one of the <code>resolve(...)</code> method is called, the passed in name will be apppended with the set
     * extension (if one is set, otherwise the name will remain as is). When resolving with locale, the locale suffixes
     * will be appended to the name and only then the extesion will be appended. For example, if the resolved template
     * name is "name", the locale is "US", and the extension is ".ext", then the resolver will try resolving the follwoing
     * three names: "name_en_US.ext", "name_en.ext", and "name.ext". NOTE, if no extension is set, the resolver will try
     * to figure out the extension from the name (basically treating the substring starting at the last index of the '.'
     * in the name as the extension).
     *
     * @return The extension of the template.
     */
    public String getExtension() {
        return extension;
    }

    /**
     * Returns the extension of the template.
     *
     * @param extension The extension of the template
     */
    public void setExtension(String extension) {
        this.extension = extension;
    }

    /**
     * Returns whether this resolver should use the locale from the locale context when absent.
     *
     * @return Whether this resolver should use the locale from the locale context when absent.
     * @see #setResolveLocalFromContextWhenAbsent(boolean)
     */
    public boolean isResolveLocalFromContextWhenAbsent() {
        return resolveLocalFromContextWhenAbsent;
    }

    /**
     * Sets whether this resolver should use the locale from the {@link org.springframework.context.i18n.LocaleContext}
     * when not specified or when specified locale is <code>null</code>. By default this is set to <code>false</code>
     * meaning this resolver will try to load the template resource without considering any locale.
     *
     * @param resolveLocalFromContextWhenAbsent Determines whether this resolver should use the locale from the locale
     *        context with it is absent.
     */
    public void setResolveLocalFromContextWhenAbsent(boolean resolveLocalFromContextWhenAbsent) {
        this.resolveLocalFromContextWhenAbsent = resolveLocalFromContextWhenAbsent;
    }

}
