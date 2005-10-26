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

package org.springmodules.template.providers.xslt;

import java.io.*;
import java.util.*;

import javax.xml.transform.*;
import javax.xml.transform.stream.*;

import org.apache.commons.logging.*;
import org.springmodules.template.*;

/**
 * A TemplateSet implementation that uses a TemplateSourceResolver to resolve the template sources from
 * which the templates will be created. A template will only be created once and then cached for later
 * retrievals.
 *
 * @author Uri Boness
 */
public class LazyDomXsltTemplateSet implements TemplateSet {

    // the default ModelToSoruceConverter that will be used to convert the given Map model to a Dom source.
    private final static ModelToSourceConverter DEFAULT_CONVERTER = DefaultModelToSourceConverter.INSTANCE;

    private final static Log log = LogFactory.getLog(LazyDomXsltTemplateSet.class);

    private TransformerFactory transformerFactory;
    private TemplateSourceResolver templateSourceResolver;
    private Map modelToSourceConverterByName;
    private ModelToSourceConverter defaultModelToSourceConverter;

    // caches the created templates by their names.
    private Map templateCache;

    /**
     * Creates a new LazyDomXsltTemplateSet with a given template source resolver, models converters,
     * and transformer factory.
     *
     * @param transformerFactory The transformer factory that will be used to create transformers from template sources.
     * @param templateSourceResolver The template source resolver that will be used to resolve a template source from a
     *        template name.
     * @param modelToSourceConverterByName ModelToSourceConverters associated with the names of the templates to
     *        which they apply.
     * @param defaultModelToSourceConverter A default (fallback) ModelToSourceConverter. Used when no specific converter
     *        was defined for a requested template.
     */
    public LazyDomXsltTemplateSet(
        TransformerFactory transformerFactory,
        TemplateSourceResolver templateSourceResolver,
        Map modelToSourceConverterByName,
        ModelToSourceConverter defaultModelToSourceConverter) {

        templateCache = new HashMap();
        this.transformerFactory = transformerFactory;
        this.templateSourceResolver = templateSourceResolver;
        this.modelToSourceConverterByName = modelToSourceConverterByName;
        this.defaultModelToSourceConverter = defaultModelToSourceConverter;
    }

    /**
     * @see TemplateSet#getTemplate(String)
     */
    public Template getTemplate(String name) {
        Template template = getCachedTemplate(name);
        if (template != null) {
            return template;
        }
        template = createTemplate(name);
        if (template != null) {
            cacheTemplate(name, template);
        }
        return template;
    }

    //============================================== Helper Methods ====================================================

    /**
     * Creates a new Template for the given template name.
     *
     * @param templateName The name of the template to be created.
     * @return The newly created template.
     */
    protected Template createTemplate(String templateName) {

        ModelToSourceConverter converter = (ModelToSourceConverter)modelToSourceConverterByName.get(templateName);
        if (converter == null) {
            converter = (defaultModelToSourceConverter != null)? defaultModelToSourceConverter : DEFAULT_CONVERTER;
        }

        Transformer transformer = createTransformer(templateName);
        if (transformer == null) {
            return null;
        }

        return new DomXsltTemplate(transformer, converter);
    }

    /**
     * Creates a new Tranformer for the given template name.
     *
     * @param templateName The given template name.
     * @return A new Tranformer for the given template name.
     */
    protected Transformer createTransformer(String templateName) {
        try {
            TemplateSource templateSource = templateSourceResolver.resolveTemplateSource(templateName);
            return transformerFactory.newTransformer(new StreamSource(templateSource.getReader()));
        } catch (TransformerConfigurationException tce) {
            log.error("Could not create xslt template from source '" + templateName + "'", tce);
            return null;
        } catch (IOException ioe) {
            log.error("Could not read xslt template from source '" + templateName + "'", ioe);
            return null;
        }
    }

    /**
     * Caches the given template using the given name as the key.
     *
     * @param name The key for the cached template.
     * @param template The template to cache.
     */
    protected void cacheTemplate(String name, Template template) {
        templateCache.put(name, template);
    }

    /**
     * Returns the cached template associated with the given name (as the key).
     *
     * @param name The given name (key).
     * @return The cached template associated with the given name, or <code>null</code> if no template was found.
     */
    protected Template getCachedTemplate(String name) {
        return (Template)templateCache.get(name);
    }
}
