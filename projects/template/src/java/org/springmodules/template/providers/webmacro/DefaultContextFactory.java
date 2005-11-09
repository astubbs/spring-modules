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

package org.springmodules.template.providers.webmacro;

import java.util.*;

import org.webmacro.*;

/**
 * A context factory that uses the WebMacro engine to create contexts.
 *
 * @author Uri Boness
 */
class DefaultContextFactory implements WebMacroContextFactory {

    private WebMacro webMacro;

    /**
     * Construcs a new DefaultContextFactory with a given WebMacro engine.
     *
     * @param webMacro The given WebMacro egine.
     */
    public DefaultContextFactory(WebMacro webMacro) {
        this.webMacro = webMacro;
    }

    /**
     * @see WebMacroContextFactory#createContext(java.util.Map)
     */
    public Context createContext(Map model) {
        Context context = webMacro.getContext();
        context.putAll(model);
        return context;
    }
}
