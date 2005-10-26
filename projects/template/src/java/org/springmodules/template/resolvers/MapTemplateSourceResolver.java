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

package org.springmodules.template.resolvers;

import java.util.*;

import org.springmodules.template.*;

/**
 * A simple {@link java.util.Map} based TemplateSourceResolver. The key's of the map are expected
 * to be the template source names, and the values the template sources themselves.
 *
 * @author Uri Boness
 */
public class MapTemplateSourceResolver implements TemplateSourceResolver {

    private Map sourceByName;

    public MapTemplateSourceResolver() {
        sourceByName = new HashMap();
    }

    public MapTemplateSourceResolver(Map sourceByName) {
        this.sourceByName = sourceByName;
    }

    public TemplateSource resolveTemplateSource(String name) {
        return (TemplateSource)sourceByName.get(name);
    }

    public void setSourceByName(Map sourceByName) {
        this.sourceByName = sourceByName;
    }

    public void addTemplateSource(String name, TemplateSource source) {
        sourceByName.put(name, source);
    }

}
