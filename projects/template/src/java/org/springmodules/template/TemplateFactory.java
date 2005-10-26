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

/**
 * A factory for template creations.
 *
 * @author Uri Boness
 */
public interface TemplateFactory {

    /**
     * Creates a template out of the given template source.
     *
     * @param source The source of the created template.
     * @return The created template.
     * @throws TemplateCreationException Thrown if an error occured during template creation.
     */
    public Template createTemplate(TemplateSource source) throws TemplateCreationException ;

    /**
     * Creates a template using the given template source resolver and the name of the requested template.
     * This can be used to create a template with dependencies of other templates (using include and import directives
     * for example). In this scenario, The template source resolver will also be used to resolve the dependencies.
     *
     * @param templateSourceResolver The template source resolver to be used.
     * @param templateName The name of the requested template.
     * @return The created template.
     * @throws TemplateCreationException Thrown if an error occured during template creation.
     */
    public Template createTemplate(TemplateSourceResolver templateSourceResolver, String templateName)
        throws TemplateCreationException;

    /**
     * A template can be created out of multiple sources. This may be handy when a template source
     * has a link/reference to or includes other sources.
     *
     * @param sources The sources of the created template.
     * @return The created template set.
     * @throws TemplateCreationException Thrown if an error occured during template creation.
     */
    public TemplateSet createTemplateSet(TemplateSource[] sources) throws TemplateCreationException;

    /**
     * Creates a template set using the given template source resolver.
     *
     * @param templateSourceResolver The template source resolver to use.
     * @return The created template set.
     * @throws TemplateCreationException Thrown if an error occured during template creation.
     */
    public TemplateSet createTemplateSet(TemplateSourceResolver templateSourceResolver) throws TemplateCreationException;

}
