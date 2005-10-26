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
 * A strategy for resolving {@link TemplateSource}'s out of given names.
 *
 * @author Uri Boness
 */
public interface TemplateSourceResolver {

    /**
     * Resolves a {@link TemplateSource} out of the given name.
     *
     * @param name The name of the required {@link TemplateSource}.
     * @return The {@link TemplateSource} associated with the given name, or <code>null</code> if non can be resolved.
     */
    public TemplateSource resolveTemplateSource(String name);

}
