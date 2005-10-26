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

import org.apache.commons.logging.*;
import org.dom4j.*;
import org.dom4j.Node;
import org.dom4j.io.*;
import org.w3c.dom.Document;
import org.springmodules.template.providers.stemp.*;

/**
 * An XPath expression resolver. This implementation uses dom4j api
 *
 * @author Uri Boness
 */
public class XPathExpressionResolver implements ExpressionResolver {

    private final static Log log = LogFactory.getLog(XPathExpressionResolver.class);

    private ModelToDomConverter modelToDomConverter;

    /**
     * Constructs a new XPathExpressionResolver.
     */
    public XPathExpressionResolver() {
    }

    /**
     * Constructs a new XPathExpressionResolver.
     *
     * @param modelToDomConverter The converter this resolver will use.
     */
    public XPathExpressionResolver(ModelToDomConverter modelToDomConverter) {
        this();
        this.modelToDomConverter = modelToDomConverter;
    }

    /**
     * @see ExpressionResolver#resolve(String, java.util.Map)
     */
    public String resolve(String expression, Map model) {
        Document document = extractDom(model);

        if (document == null) {
            log.error("Could not find/convert Document in model");
            return null;
        }

        try {
            DOMReader reader = new DOMReader();
            org.dom4j.Document dom4jDocument = reader.read(document);
            Object result = dom4jDocument.selectObject(expression);
            return String.valueOf(getTextContent(result));
        } catch (InvalidXPathException ixpe) {
            log.error("Given expression is not a valid xpath \"" + expression + "\"", ixpe);
            return null;
        } catch (Exception epe) {
            log.error("Cannot resolve xpath on given model", epe);
            return null;
        }
    }

    /**
     * Extracts the org.w3c.dom.Document from the given java.util.Map model. This method first uses the
     * ModelToSourceConverter (if one is set) to extract the document. If no converter is set, the it looks for
     * a document object in the map.
     * It is also possible to override this method instead of using a converter.
     *
     * @param model The map model from which the document will be extracted.
     * @return The extrated document.
     */
    protected Document extractDom(Map model) {
        if (modelToDomConverter != null) {
            return modelToDomConverter.convert(model);
        }
        for (Iterator values = model.values().iterator(); values.hasNext();) {
            Object value = values.next();
            if (value instanceof Document) {
                return (Document)value;
            }
        }
        return null;
    }

    protected String getTextContent(Object xpathResult) {
        if (xpathResult instanceof Attribute) {
            return ((Attribute)xpathResult).getValue();
        }

        if (xpathResult instanceof Node) {
            return ((Node)xpathResult).getText();
        }

        if (xpathResult instanceof List) {
            List list = (List)xpathResult;
            StringBuffer buffer = new StringBuffer();
            boolean first = true;
            for (Iterator i = list.iterator(); i.hasNext();) {
                String value = getTextContent(i.next());
                if (!first) {
                    buffer.append(",");
                }
                buffer.append(value);
            }
        }

        return String.valueOf(xpathResult);
    }
}
