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

package org.springmodules.template.providers.xslt;

import java.util.*;

import javax.xml.transform.*;
import javax.xml.transform.dom.*;

import org.w3c.dom.*;

/**
 * This converter checks if the model contains a value that happens to be of a Source or a java.w3c.dom.Document type.
 * If so, it uses this value as the restuned source. If no such value exists, a TemplateModelToDomConversionException
 * will be thrown.
 *
 * @author Uri Boness
 */
public class DefaultModelToSourceConverter implements ModelToSourceConverter {

    public final static ModelToSourceConverter INSTANCE = new DefaultModelToSourceConverter();

    /**
     * @see ModelToSourceConverter#convert(java.util.Map)
     */
    public Source convert(Map model) throws ModelToSourceConversionException {
        for (Iterator values = model.values().iterator(); values.hasNext();) {
            Object value = values.next();
            if (value instanceof Source) {
                return (Source)value;
            }
            if (value instanceof Document) {
                return new DOMSource((Document)value);
            }
        }
        throw new ModelToSourceConversionException("Could not convert model to JDom Document");
    }

}
