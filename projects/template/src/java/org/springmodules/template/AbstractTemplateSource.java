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
 * A base class for all template source implementations.
 *
 * @author Uri Boness
 */
public abstract class AbstractTemplateSource implements TemplateSource {

    // the name of the template source.
    protected String name;

    /**
     * A default empty constructor.
     */
    public AbstractTemplateSource() {
    }

    /**
     * Constructs a template source with a a given name;
     *
     * @param name The name of this template source.
     */
    public AbstractTemplateSource(String name) {
        this.name = name;
    }

    /**
     * A template source may have a name associated with it.
     *
     * @return The name of this template source, <code>null</code> if no name is associated with this source.
     * @see org.springmodules.template.TemplateSource#getName()
     */
    public String getName() {
        return name;
    }

    /**
     * By default this functionality is not supported.
     *
     * @see org.springmodules.template.TemplateSource#getAsString()
     */
    public String getAsString() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

}
