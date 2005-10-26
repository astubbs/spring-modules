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

package org.springmodules.template.providers.xslt.converters;

import java.util.*;

import javax.xml.transform.*;
import javax.xml.transform.dom.*;

import org.w3c.dom.*;
import org.springmodules.template.providers.xslt.*;

/**
 * A base class for w3c implementations of the ModelToSourceConverter interface. Sub-classes of this class will only
 * have to convert the Map model to a org.w3c.dom.Document.
 *
 * @author Uri Boness
 */
public abstract class W3cModelToSourceConverter implements ModelToSourceConverter {

    /**
     * @see ModelToSourceConverter#convert(java.util.Map)
     */
    public final Source convert(Map model) throws ModelToSourceConversionException {
        return new DOMSource(convertToDocument(model));
    }

    /**
     * Converts the given Map model to a org.w3c.dom.Document
     * @param model The model to convert.
     * @return The org.w3c.dom.Document that was generated out of the given model.
     * @throws ModelToSourceConversionException Thrown when the conversion fails.
     */
    protected abstract Document convertToDocument(Map model) throws ModelToSourceConversionException;

}
