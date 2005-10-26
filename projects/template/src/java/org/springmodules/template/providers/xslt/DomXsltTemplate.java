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
 * An XSLT implementation of the org.springmodules.template.Template using JDOM.
 *
 * @author Uri Boness
 */
public class DomXsltTemplate extends AbstractTemplate {

    private Transformer transformer;
    private ModelToSourceConverter modelToSourceConverter;

    /**
     * Constructs a new DomXsltTemplate.
     *
     * @param transformer The XSL trasformer of this template.
     * @param modelToSourceConverter The converter to be used to convert the given model to a JDOM document as an
     *        input for the trasformation.
     */
    public DomXsltTemplate(Transformer transformer, ModelToSourceConverter modelToSourceConverter) {
        this.transformer = transformer;
        this.modelToSourceConverter = modelToSourceConverter;
    }

    /**
     * @see Template#generate(java.io.Writer, java.util.Map)
     */
    public void generate(Writer writer, Map model) throws TemplateGenerationException {
        try {

            Source source = modelToSourceConverter.convert(model);
            StreamResult result = new StreamResult(writer);
            transformer.transform(source, result);

        } catch (ModelToSourceConversionException mtdce) {
            throw new TemplateGenerationException("Template generation failed. Could not convert given model to JDOM document", mtdce);
        } catch (TransformerException te) {
            throw new TemplateGenerationException("Template generation failed. Could not transform model using xsl", te);
        }
    }

}
