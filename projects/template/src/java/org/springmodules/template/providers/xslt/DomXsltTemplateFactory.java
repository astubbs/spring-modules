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

import org.springmodules.template.*;

/**
 * An XSLT implementation of the org.springmodules.template.TemplateFactory.
 *
 * @author Uri Boness
 */
public class DomXsltTemplateFactory implements TemplateFactory {

    private final static ModelToSourceConverter DEFAULT_MODEL_TO_SOURCE_CONVERTER = DefaultModelToSourceConverter.INSTANCE;

    // he ModelToSourceConverter to be used when generating a template from only one source. Also serves
    // as a fallback converter for multiple templates.
    private ModelToSourceConverter modelToSourceConverter;

    // registry to hold ModelToSourceConverter's by template name.
    private Map modelToSourceConverterByName;

    // the transformer factory that will be used by default (that is, when a custom factoy is not needed).
    private TransformerFactory transformerFactory;

    /**
     * Construcsts a new DomXsltTemplateFactory
     */
    public DomXsltTemplateFactory() {
        modelToSourceConverterByName = new HashMap();
        modelToSourceConverter = DEFAULT_MODEL_TO_SOURCE_CONVERTER;
        transformerFactory = TransformerFactory.newInstance();
    }

    /**
     * @see org.springmodules.template.TemplateFactory#createTemplate(org.springmodules.template.TemplateSource)
     */
    public Template createTemplate(TemplateSource source) throws TemplateCreationException {
        Transformer transformer = createTransformer(source, transformerFactory);
        return new DomXsltTemplate(transformer, modelToSourceConverter);
    }

    /**
     * @see org.springmodules.template.TemplateFactory#createTemplate(org.springmodules.template.TemplateSourceResolver, String)
     */
    public Template createTemplate(TemplateSourceResolver templateSourceResolver, String templateName)
        throws TemplateCreationException {

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        transformerFactory.setURIResolver(new TemplateSourceResolverUriResolver(templateSourceResolver));
        Transformer transformer = createTransformer(templateName, templateSourceResolver, transformerFactory);
        return new DomXsltTemplate(transformer, modelToSourceConverter);
    }

    /**
     * @see org.springmodules.template.TemplateFactory#createTemplateSet(org.springmodules.template.TemplateSource[])
     */
    public TemplateSet createTemplateSet(TemplateSource[] sources) throws TemplateCreationException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        transformerFactory.setURIResolver(new TemplateSourcesUriResolver(sources));
        Map transformersByNames = createTrasformers(sources, transformerFactory);
        return new DomXsltTemplateSet(transformersByNames, modelToSourceConverterByName, modelToSourceConverter);
    }

    /**
     * @see org.springmodules.template.TemplateFactory#createTemplateSet(org.springmodules.template.TemplateSourceResolver)
     */
    public TemplateSet createTemplateSet(TemplateSourceResolver templateSourceResolver) throws TemplateCreationException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        transformerFactory.setURIResolver(new TemplateSourceResolverUriResolver(templateSourceResolver));
        return new LazyDomXsltTemplateSet(
            transformerFactory,
            templateSourceResolver,
            modelToSourceConverterByName,
            modelToSourceConverter
        );
    }

    //============================================ Setter/Getter ==================================================

    /**
     * Sets a single ModelToSourceConverter to be used by this factory. This converter will be used when <i>createTemplate()</i>
     * is used, and also as a default (a backup) converter when <i>createTemplateSet()</i> is used.
     *
     * @param modelToSourceConverter the single ModelToSourceConverter to be used by this factory.
     */
    public void setModelToDomConverter(ModelToSourceConverter modelToSourceConverter) {
        this.modelToSourceConverter = modelToSourceConverter;
    }

    /**
     * Sets a mapping between ModelToDomConverters and template names. This mapping is used when using the
     * <i>createTemplateSet()</i>.
     *
     * @param modelToDomConverterByName The mapping between ModelToDomConverters and template naems.
     */
    public void setModelToDomConverterByName(Map modelToDomConverterByName) {
        this.modelToSourceConverterByName = modelToDomConverterByName;
    }


    //=========================================== Helper Methods ==================================================

    /**
     * Creates an xslt transformer from the given template source using the given transformer factory.
     *
     * @param source The TemplateSource from which the transformer will be created.
     * @param transformerFactory the transformer factory that will create the transformer.
     * @return The newly created Transformer.
     * @throws TemplateCreationException Thrown when the Transformer could not be created.
     */
    protected Transformer createTransformer(TemplateSource source, TransformerFactory transformerFactory)
        throws TemplateCreationException {

        try {
            return transformerFactory.newTransformer(new StreamSource(source.getReader()));
        } catch (TransformerConfigurationException tce) {
            throw new TemplateCreationException("Could not create XSLT template", tce);
        } catch (IOException ioe) {
            throw new TemplateCreationException("Could not read xsl template source", ioe);
        }
    }

    /**
     * Creats a new transformer from a TemplateSource using the given transformer factory.
     *
     * @param name The name of the template (source).
     * @param resolver The TemplateSourceResolver that will be used to resolve the TemplateSource from the given name.
     * @param transformerFactory The TransformerFactory that will be used to create the transformer.
     * @return The newly create Transformer.
     * @throws TemplateCreationException Thrown when the Transformer could not be created.
     */
    protected Transformer createTransformer(
        String name,
        TemplateSourceResolver resolver,
        TransformerFactory transformerFactory) throws TemplateCreationException {

        TemplateSource templateSource = resolver.resolveTemplateSource(name);

        try {
            return transformerFactory.newTransformer(new StreamSource(templateSource.getReader()));
        } catch (TransformerConfigurationException tce) {
            throw new TemplateCreationException("Could not create XSLT template '" + name + "'", tce);
        } catch (IOException ioe) {
            throw new TemplateCreationException("Could not read XSLT template from source '" + name + "'", ioe);
        }
    }

    /**
     * Creates a map of transformers from the given template sources, where the keys are the names
     * of the templates (sources).
     *
     * @param sources The templates sources from which the transformers will be created.
     * @param transformerFactory The TransformerFactory that will be used to create the transformers.
     * @return The Map of the created transformers.
     * @throws TemplateCreationException Thrown when one of the transformer could not be created.
     */
    protected Map createTrasformers(TemplateSource[] sources, TransformerFactory transformerFactory)
        throws TemplateCreationException {

        Map trasformersByName = new HashMap();
        for (int i=0; i<sources.length; i++) {
            Transformer transformer = createTransformer(sources[i], transformerFactory);
            trasformersByName.put(sources[i].getName(), transformer);
        }
        return trasformersByName;
    }


    //============================================= Inner Classes ==================================================

    // This is an implementation of URIResolver that uses a list of template sources.
    // In this case, the URI will have to be one of the template sources's names. A template source with a name
    // that equals the given URI will be searched for. When found, a Source object will be reated from it.
    private static class TemplateSourcesUriResolver implements URIResolver {

        private Map  templateSourcesByNames;

        public TemplateSourcesUriResolver(TemplateSource[] templateSources) {
            templateSourcesByNames = new HashMap();
            for (int i=0; i<templateSources.length; i++) {
                templateSourcesByNames.put(templateSources[i].getName(), templateSources[i]);
            }
        }

        public Source resolve(String href, String base) throws TransformerException {
            TemplateSource templateSource = (TemplateSource)templateSourcesByNames.get(href);
            if (templateSource == null) {
                return null; // according to the contract of this method.
            }
            try {
                return new StreamSource(templateSource.getReader());
            } catch (IOException ioe) {
                throw new TransformerException("Could not read template source '" + href + "'", ioe);
            }
        }
    }

    // this is a URIResolver that uses TemplateSourceResolver to get the template source.
    // In this case, the URI will be passed to the TemplateSourceResolver to get a TemlateSource from which
    // a Source object will be created.
    private static class TemplateSourceResolverUriResolver implements URIResolver {

        private TemplateSourceResolver templateSourceResolver;

        public TemplateSourceResolverUriResolver(TemplateSourceResolver templateSourceResolver) {
            this.templateSourceResolver = templateSourceResolver;
        }

        public Source resolve(String href, String base) throws TransformerException {
            TemplateSource templateSource = templateSourceResolver.resolveTemplateSource(href);
            if (templateSource == null) {
                return null;
            }
            try {
                return new StreamSource(templateSource.getReader());
            } catch (IOException ioe) {
                throw new TransformerException("Could not read template source '" + href + "'", ioe);
            }
        }

    }

}
