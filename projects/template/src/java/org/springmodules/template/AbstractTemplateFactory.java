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

package org.springmodules.template;

import org.apache.commons.logging.*;

/**
 * A base class for TemplateFactory implementations.
 *
 * @author Uri Boness
 */
public abstract class AbstractTemplateFactory implements TemplateFactory {

    protected final Log log = LogFactory.getLog(getClass());

    /**
     * @see TemplateFactory#createTemplate(TemplateSourceResolver, String)
     */
    public Template createTemplate(TemplateSourceResolver templateSourceResolver, String templateName) throws TemplateCreationException {
        TemplateSource source = templateSourceResolver.resolveTemplateSource(templateName);
        if (source == null) {
            throw new TemplateCreationException("Could not resolve template source '" + templateName + "'");
        }
        return createTemplate(source);
    }

    /**
     * @see TemplateFactory#createTemplateSet(TemplateSource[])
     */
    public TemplateSet createTemplateSet(TemplateSource[] sources) throws TemplateCreationException {
        SimpleTemplateSet templates = new SimpleTemplateSet();
        for (int i=0; i<sources.length; i++) {
            String name = sources[i].getName();
            if (name == null) {
                if (log.isWarnEnabled()) {
                    log.warn("Template source \"" + sources.toString() + "\" has no name. TemplateSet can " +
                        "contain only templates that have names");
                }
                continue;
            }

            Template template = createTemplate(sources[i]);
            templates.addTemplate(sources[i].getName(), template);
        }
        return templates;
    }

    /**
     * @see TemplateFactory#createTemplateSet(TemplateSourceResolver)
     */
    public TemplateSet createTemplateSet(TemplateSourceResolver templateSourceResolver) throws TemplateCreationException {
        return new TemplateSourceResolverTemplateSet(templateSourceResolver, this);
    }


}
