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

package org.springmodules.template.providers.freemarker;

import java.io.*;

import freemarker.core.*;
import freemarker.template.*;
import org.springmodules.template.Template;
import org.springmodules.template.*;

/**
 * A freemarker implementation of the {@link TemplateSet} interface.
 * This implemenation uses freemarker configuration to retrieve the templates by names.
 *
 * @author Uri Boness
 */
public class FreemarkerTemplateSet implements TemplateSet {

    // the freemarker configuration to be used.
    private Configuration configuration;

    /**
     * Creates a new FreemarkerTemplateSet with given freemarker configuration.
     *
     * @param configuration The freemarker configuration to be used.
     */
    public FreemarkerTemplateSet(Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     * @see TemplateSet#getTemplate(String)
     */
    public Template getTemplate(String name) {

        freemarker.template.Template template;
        try {
             template = configuration.getTemplate(name);
        } catch (FileNotFoundException fnfe) {
            return null; /** returning null according to the contract of this method see {@link TemplateSet#getTemplate(String)} */
        } catch (ParseException pe) {
            throw new TemplateCreationException("Could not parse freemarker template. Bad syntax", pe);
        } catch (IOException ioe) {
            throw new TemplateCreationException("Could not read template source", ioe);
        }

        return new FreemarkerTemplate(template);
    }
}
