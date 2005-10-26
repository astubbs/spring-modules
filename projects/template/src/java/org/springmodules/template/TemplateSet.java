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
 * Represents a set of templates that are associated with names.
 *
 * @author Uri Boness
 */
public interface TemplateSet {

    /**
     * Returns the template associated with the given name.
     *
     * @param name The name of the requested template.
     * @return The template associated with the given name, or <code>null</code> if there's not such template.
     */
    public Template getTemplate(String name);


}
