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

import org.springmodules.template.*;

/**
 * A BeanFactory that creates {@link DomXsltTemplate}s.
 *
 * @author Uri Boness
 */
public class XslTemplateFactoryBean extends AbstractTemplateFactoryBean {

    private ModelToSourceConverter modelToSourceConverter;

    private Map modelToSourceConverterByName;

    /**
     * Empty contrustor (JavaBean support).
     */
    public XslTemplateFactoryBean() {
        modelToSourceConverter = new DefaultModelToSourceConverter();
    }

    /**
     * Sets the model-to-source converter that will be used to convert {@link java.util.Map} model
     * to {@link javax.xml.transform.Source}.
     *
     * @param modelToSourceConverter The model-to-source converter.
     */
    public void setModelToSourceConverter(ModelToSourceConverter modelToSourceConverter) {
        this.modelToSourceConverter = modelToSourceConverter;
    }

    /**
     * Sets the model-to-source converters for specific template names.
     *
     * @param modelToSourceConverterByName The model-to-source converters for specific template names.
     * @see DomXsltTemplateFactory#setModelToDomConverterByName(java.util.Map)
     */
    public void setModelToSourceConverterByName(Map modelToSourceConverterByName) {
        this.modelToSourceConverterByName = modelToSourceConverterByName;
    }

    /**
     * Creates a new DomXsltTemplateFactory.
     *
     * @see org.springmodules.template.AbstractTemplateFactoryBean#createTemplateFactory()
     */
    protected TemplateFactory createTemplateFactory() throws Exception {
        DomXsltTemplateFactory factory = new DomXsltTemplateFactory();
        factory.setModelToDomConverter(modelToSourceConverter);
        if (modelToSourceConverterByName != null) {
            factory.setModelToDomConverterByName(modelToSourceConverterByName);
        }
        return factory;
    }

}
