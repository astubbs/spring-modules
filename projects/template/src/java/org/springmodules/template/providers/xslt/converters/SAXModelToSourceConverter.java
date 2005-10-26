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

import java.io.*;
import java.util.*;

import javax.xml.transform.*;
import javax.xml.transform.sax.*;

import org.xml.sax.*;
import org.xml.sax.helpers.*;
import org.springmodules.template.providers.xslt.*;

/**
 * A base class for implementation of ModelToSourceConverter that use SAX events. In many cases using events to define
 * the document source is much easier and readable then creating a DOM graph.
 *
 * @author Uri Boness
 */
public abstract class SAXModelToSourceConverter implements ModelToSourceConverter {

    private final static String EMPTY_STRING = "";
    private final static Attributes NO_ATTRIBUTES = new AttributesImpl();

    /**
     * @see org.springmodules.template.providers.xslt.ModelToSourceConverter#convert(java.util.Map)
     */
    public final Source convert(Map model) throws ModelToSourceConversionException {
        return new SAXSource(new MapHolderXMLReader(), new MapHolderInputSource(model));
    }

    /**
     * Converts the given model to document source by firing events using the given content handler.
     *
     * @param model The model to convert.
     * @param contentHandler The content handle by which the events will be fired.
     * @throws SAXException Thrown if the conversion fails.
     */
    public abstract void convert(Map model, ContentHandler contentHandler) throws SAXException;


    //============================================= Helper Methods =====================================================

    /**
     * A helper method to fire a "start document" event.
     *
     * @param hander The handler to be used when firing the event.
     * @throws SAXException
     */
    protected void startDocument(ContentHandler hander) throws SAXException {
        hander.startDocument();
    }

    /**
     * A helper method to fire an "end document" event.
     *
     * @param handler The handler to be used when firing the event.
     * @throws SAXException
     */
    protected void endDocument(ContentHandler handler) throws SAXException {
        handler.endDocument();
    }

    /**
     * A helper method to fire an "start element" event. The element has no namespace and no attributes.
     *
     * @param elementName The name of the element.
     * @param handler The handler to be used when firing the event.
     * @throws SAXException
     */
    protected void startElement(String elementName, ContentHandler handler) throws SAXException {
        handler.startElement(EMPTY_STRING, elementName, elementName, NO_ATTRIBUTES);
    }

    /**
     * A helper method to fire an "start element" event. The element has no namespace but has attributes.
     *
     * @param elementName The name of the element.
     * @param attributes The attributes of the element represented as Properties.
     * @param handler The handler to be used when firing the event.
     * @throws SAXException
     */
    protected void startElement(String elementName, Properties attributes, ContentHandler handler) throws SAXException {
        AttributesImpl attrs = new AttributesImpl();
        for (Enumeration names = attributes.propertyNames(); names.hasMoreElements();) {
            String name = (String)names.nextElement();
            String value = attributes.getProperty(name);
            attrs.addAttribute(EMPTY_STRING, name, name, EMPTY_STRING, value);
        }
        handler.startElement(EMPTY_STRING, elementName, elementName, attrs);
    }

    /**
     * A helper method to fire an "start element" event. The element has no namespace but has attributes.
     *
     * @param elementName The name of the element.
     * @param attributeNames The names of the attributes
     * @param attributeValues The values of the attributes
     * @param handler The handler to be used when firing the event.
     * @throws IllegalArgumentException Thrown if the number of attribute names doesn't match the number of attribute
     *         values.
     * @throws SAXException
     */
    protected void startElement(
        String elementName,
        String[] attributeNames,
        String[] attributeValues,
        ContentHandler handler) throws SAXException {

        if (attributeNames.length != attributeValues.length) {
            throw new IllegalArgumentException("The number of attribute names must match the number of attribute values");
        }

        AttributesImpl attributes = new AttributesImpl();
        for (int i=0; i<attributeNames.length; i++) {
            String name = attributeNames[i];
            String value = attributeValues[i];
            attributes.addAttribute(EMPTY_STRING, name, name, EMPTY_STRING, value);
        }
        handler.startElement(EMPTY_STRING, elementName, elementName, attributes);
    }

    /**
     * A helper method to fire an "start element" event. The element has no namespace but has attributes.
     * The same as {@link #startElement(String, String[], String[], org.xml.sax.ContentHandler)} but with an Object array
     * to represent the attribute values instead of a String array.
     *
     * @param elementName The name of the element.
     * @param attributeNames The names of the attributes
     * @param attributeValues The values of the attributes
     * @param handler The handler to be used when firing the event.
     * @throws IllegalArgumentException Thrown if the number of attribute names doesn't match the number of attribute
     *         values.
     * @throws SAXException
     */
    protected void startElement(
        String elementName,
        String[] attributeNames,
        Object[] attributeValues,
        ContentHandler handler) throws SAXException {

        if (attributeNames.length != attributeValues.length) {
            throw new IllegalArgumentException("The number of attribute names must match the number of attribute values");
        }

        AttributesImpl attributes = new AttributesImpl();
        for (int i=0; i<attributeNames.length; i++) {
            String name = attributeNames[i];
            Object value = attributeValues[i];
            attributes.addAttribute(EMPTY_STRING, name, name, EMPTY_STRING, String.valueOf(value));
        }
        handler.startElement(EMPTY_STRING, elementName, elementName, attributes);
    }

    /**
     * A helper method to fire an "end element" event.
     *
     * @param elementName The name of the "closed" element.
     * @param handler The handler to be used when firing the event.
     * @throws SAXException
     */
    protected void endElement(String elementName, ContentHandler handler) throws SAXException {
        handler.endElement(EMPTY_STRING, elementName, elementName);
    }

    /**
     * A helper method to fire an "text" event.
     *
     * @param text The text to be fired.
     * @param handler The handler to be used when firing the event.
     * @throws SAXException
     */
    protected void text(String text, ContentHandler handler) throws SAXException {
        handler.characters(text.toCharArray(), 0, text.length());
    }

    //============================================== Inner Classes =====================================================

    // an input source that holds the Map model as the source for the xml.
    private class MapHolderInputSource extends InputSource {
        private Map model;

        public MapHolderInputSource(Map model) {
            this.model = model;
        }

        public Map getModel() {
            return model;
        }
    }

    // an XMLReader that knows how to "read" from MapHolderInputSource
    private class MapHolderXMLReader implements XMLReader {

        private ContentHandler contentHandler;
        private DTDHandler dtdHandler;
        private ErrorHandler errorHandler;
        private EntityResolver entityResolver;
        private Map features;
        private Map properties;

        public MapHolderXMLReader() {
            features = new HashMap();
            properties = new HashMap();
        }

        public void parse(String systemId) throws IOException, SAXException {
            // do nothing...
        }

        public boolean getFeature(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
            Boolean value = (Boolean)features.get(name);
            return (value != null) && value.booleanValue();
        }

        public void setFeature(String name, boolean value) throws SAXNotRecognizedException, SAXNotSupportedException {
            features.put(name, (value)? Boolean.TRUE : Boolean.FALSE);
        }

        public ContentHandler getContentHandler() {
            return contentHandler;
        }

        public void setContentHandler(ContentHandler handler) {
            this.contentHandler = handler;
        }

        public DTDHandler getDTDHandler() {
            return dtdHandler;
        }

        public void setDTDHandler(DTDHandler handler) {
            this.dtdHandler = handler;
        }

        public EntityResolver getEntityResolver() {
            return entityResolver;
        }

        public void setEntityResolver(EntityResolver resolver) {
            this.entityResolver = resolver;
        }

        public ErrorHandler getErrorHandler() {
            return errorHandler;
        }

        public void setErrorHandler(ErrorHandler handler) {
            this.errorHandler = handler;
        }

        public void parse(InputSource input) throws IOException, SAXException {
            if (!(input instanceof MapHolderInputSource)) {
                throw new SAXException("Excpecting a MapHolderInputSource, but received " + input);
            }
            if (contentHandler == null) {
                return;
            }
            Map model = ((MapHolderInputSource)input).getModel();
            convert(model, contentHandler);
        }

        public Object getProperty(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
            return properties.get(name);
        }

        public void setProperty(String name, Object value) throws SAXNotRecognizedException, SAXNotSupportedException {
            properties.put(name, value);
        }

    }

}
