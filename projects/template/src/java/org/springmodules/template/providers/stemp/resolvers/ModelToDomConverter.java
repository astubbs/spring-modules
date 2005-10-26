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

package org.springmodules.template.providers.stemp.resolvers;

import java.util.*;

import org.w3c.dom.*;

/**
 * Converts a Map model to a org.w3c.dom.Document
 *
 * @see XPathExpressionResolver
 * @author Uri Boness
 */
public interface ModelToDomConverter {

    /**
     * Converts the given Map to a org.w3c.dom.Document
     *
     * @param model The model to convert.
     * @return The document that was generated from the given map.
     * @throws ModelConversionException Thrown if the conversion fails.
     */
    public Document convert(Map model) throws ModelConversionException;

}
