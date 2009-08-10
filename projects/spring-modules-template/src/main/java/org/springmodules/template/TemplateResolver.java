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

package org.springmodules.template;

import java.util.Locale;

/**
 * A strategy used to resolve templates based on a given name.
 *
 * @author Uri Boness
 */
public interface TemplateResolver {

    /**
     * Resolves the appropriate template based on the given name using the given encoding.
     *
     * @param name The name of the template
     * @param encoding The encoding of the template
     * @return The resolved template.
     */
    Template resolve(String name, String encoding);

    /**
     * Resolves the appropriate template based on the given name and locale.
     *
     * @param name The name of the template
     * @param locale The locale of the template
     * @return The resolved template.
     */
    Template resolve(String name, Locale locale);

    /**
     * Resolves the appropriate template based on the given name, locale, and encoding.
     *
     * @param name The name of the template
     * @param encoding The encoding of the template
     * @param locale The locale of the template
     * @return The resolved template.
     */
    Template resolve(String name, String encoding, Locale locale);

    /**
     * Resolves the appropriate template based on the given name using the default encoding.
     *
     * @param name The name of the template.
     * @return The resolved template.
     */
    Template resolve(String name);

}