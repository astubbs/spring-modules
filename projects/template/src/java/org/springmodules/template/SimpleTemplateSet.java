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

import java.util.*;

/**
 * A simple implementation of the org.springmodules.template.TemplateSet interface.
 *
 * @author Uri Boness
 */
public class SimpleTemplateSet implements TemplateSet {

    // holds templates by their names.
    private Map templatesByNames;

    /**
     * Constructs a new empty SimpleTemplateSet.
     */
    public SimpleTemplateSet() {
        templatesByNames = new HashMap();
    }

    /**
     * @see TemplateSet#getTemplate(String)
     */
    public Template getTemplate(String name) {
        return (Template)templatesByNames.get(name);
    }

    /**
     * Returns the number of templates in this template set.
     *
     * @return The number of templates in this template set.
     */
    public int getTemplateCount() {
        return templatesByNames.size();
    }

    /**
     * Returns a list of all the names of the templates in this template set.
     *
     * @return A list of all the names of the templates in this template set.
     */
    public String[] getTemplateNames() {
        return (String[])templatesByNames.keySet().toArray(new String[templatesByNames.size()]);
    }

    /**
     * Adds a template to this template set and associates it with the given name.
     *
     * @param name The name of the given template.
     * @param template The template to be added to this template set.
     */
    public void addTemplate(String name, Template template) {
        templatesByNames.put(name, template);
    }
}
