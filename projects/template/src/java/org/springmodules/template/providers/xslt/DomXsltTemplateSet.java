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

import java.util.*;

import javax.xml.transform.*;

import org.springmodules.template.*;

/**
 * An XSLT implementation of the TemplateSet interface using JDOM.
 *
 * @author Uri Boness
 */
public class DomXsltTemplateSet implements TemplateSet {

    private final static ModelToSourceConverter DEFAULT_CONVERTER = new DefaultModelToSourceConverter();

    private Map trasformersByNames;
    private Map modelToDomConverterByName;
    private ModelToSourceConverter defaultConverter;

    /**
     * Constructs a new DomXsltTemplateSet.
     *
     * @param trasformersByNames template transformers associated with template names.
     * @param modelToDomConverterByName MapToJDomConverters associated with template names.
     * @param defaultConverter A default ModelToSourceConverter to be used when other ones cannot be resolved for
     *        a specific template. That is, if a converter for a template name doesn't exist.
     */
    public DomXsltTemplateSet(Map trasformersByNames, Map modelToDomConverterByName, ModelToSourceConverter defaultConverter) {
        this.trasformersByNames = trasformersByNames;
        this.modelToDomConverterByName = modelToDomConverterByName;
        this.defaultConverter = defaultConverter;
    }

    /**
     * Returns the template associated with the given name.
     *
     * @param name The name of the requested template.
     * @return The template associated with the given name.
     */
    public Template getTemplate(String name) {
        ModelToSourceConverter converter = (ModelToSourceConverter)modelToDomConverterByName.get(name);
        if (converter == null) {
            converter = (defaultConverter != null) ? defaultConverter : DEFAULT_CONVERTER;
        }
        return new DomXsltTemplate((Transformer)trasformersByNames.get(name), converter);
    }

    /**
     * Returns the number of templates in this template set.
     * @return The number of templates in this template set.
     */
    public int getTemplateCount() {
        return trasformersByNames.size();
    }

    /**
     * Returns the names of the templates in this template set.
     * @return The names of the templates in this template set.
     */
    public String[] getTemplateNames() {
        return (String[])trasformersByNames.keySet().toArray(new String[trasformersByNames.size()]);
    }
}
