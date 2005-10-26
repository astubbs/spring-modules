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

import org.jdom.*;
import org.jdom.transform.*;
import org.springmodules.template.providers.xslt.*;

/**
 * A base class for the ModelToSourceConverter interface using JDOM. Subclasses of this
 * class will only have to convert the given map model to a JDOM document.
 *
 * @author Uri Boness
 */
public abstract class JDomModelToSourceConverter implements ModelToSourceConverter {

    /**
     * @see org.springmodules.template.providers.xslt.ModelToSourceConverter#convert(java.util.Map)
     */
    public final Source convert(Map model) throws ModelToSourceConversionException {
        Document document = convertToDocument(model);
        return new JDOMSource(document);
    }

    public abstract Document convertToDocument(Map model) throws ModelToSourceConversionException;

}
