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

import java.io.*;
import java.util.*;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.stream.*;

import junit.framework.*;
import org.springmodules.template.samples.emailer.*;
import org.springmodules.template.providers.xslt.converters.*;
import org.w3c.dom.*;
import org.xml.sax.*;

/**
 * Tests the DomXsltTemplate  class.
 *
 * @author Uri Boness
 */
public class DomXsltTemplateTest extends TestCase {

    private Transformer transformer;

    protected void setUp() throws Exception {
        transformer = buildTransformer();
    }

    public void testGenerateWithDefaultModelConverter() throws Exception {

        // creating the template with the default converter.

        DomXsltTemplate template = new DomXsltTemplate(transformer, new DefaultModelToSourceConverter());

        Map model = buildDocumentModel();

        // generating the template output.
        StringWriter writer = new StringWriter();
        template.generate(writer, model);

        assertEquals("John,40", writer.toString());
    }

    public void testMultipleGenerationsWithDefaultModelConverter() throws Exception {

        // creating the template with the default converter.

        DomXsltTemplate template = new DomXsltTemplate(transformer, new DefaultModelToSourceConverter());

        Map model = buildDocumentModel();

        // generating the template output once.
        for (int i=0; i<5; i++) {
            StringWriter writer = new StringWriter();
            template.generate(writer, model);

            assertEquals("generating template - attempt " + i, "John,40", writer.toString());
        }
    }

    public void testGenerateWithW3cModelConverter() throws Exception {

        DomXsltTemplate template = new DomXsltTemplate(transformer, new W3cModelToSourceConverterImpl());

        Map model = buildBeanModel();

        StringWriter writer = new StringWriter();
        template.generate(writer, model);

        assertEquals("John,40", writer.toString());

    }

    public void testGenerateWithJDomModelConverter() throws Exception {

        DomXsltTemplate template = new DomXsltTemplate(transformer, new JDomModelToSourceConverterImpl());

        Map model = buildBeanModel();

        StringWriter writer = new StringWriter();
        template.generate(writer, model);

        assertEquals("John,40", writer.toString());
    }

    public void testGenerateWithSAXModelConverter() throws Exception {

        DomXsltTemplate template = new DomXsltTemplate(transformer, new SAXModelToSourceConverterImpl());

        Map model = buildBeanModel();

        StringWriter writer = new StringWriter();
        template.generate(writer, model);

        assertEquals("John,40", writer.toString());
    }

    //=========================================== Helper Methods ==================================================

    private Transformer buildTransformer() throws Exception {

        StringBuffer xsl = new StringBuffer();

        xsl.append("<?xml version='1.0'?>");
        xsl.append("<xsl:stylesheet version='1.0' xmlns:xsl='http://www.w3.org/1999/XSL/Transform' >");
        xsl.append("    <xsl:output method='text' />");
        xsl.append("    <xsl:template match='/'>");
        xsl.append("        <xsl:value-of select='person/name'/>,<xsl:value-of select='person/age' />");
        xsl.append("    </xsl:template>");
        xsl.append("</xsl:stylesheet>");

        TransformerFactory tf = TransformerFactory.newInstance();
        return tf.newTransformer(new StreamSource(new StringReader(xsl.toString())));
    }

    private Map buildBeanModel() {
        Person person = new Person("John", 40);
        Map model = new HashMap();
        model.put("person", person);
        return model;
    }

    private Map buildDocumentModel() throws Exception {

        StringBuffer xml = new StringBuffer();

        xml.append("<?xml version='1.0' encoding='ISO-8859-1'?>");
        xml.append("<person>");
        xml.append("    <name>John</name>");
        xml.append("    <age>40</age>");
        xml.append("</person>");

        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(
            new InputSource(new StringReader(xml.toString())));
        Map model = new HashMap();
        model.put("model", doc); // the name of the document in the model can be anything.
        return model;
    }


    //============================================== Inner Classes =====================================================

    private static class W3cModelToSourceConverterImpl extends W3cModelToSourceConverter {

        protected Document convertToDocument(Map model) throws ModelToSourceConversionException {
            try {

                Person person = (Person)model.get("person");

                Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
                Element root = document.createElement("person");
                Element name = document.createElement("name");
                name.appendChild(document.createTextNode(person.getName()));
                root.appendChild(name);
                Element age = document.createElement("age");
                age.appendChild(document.createTextNode(String.valueOf(person.getAge())));
                root.appendChild(age);
                document.appendChild(root);

                return document;

            } catch (ParserConfigurationException pce) {
                throw new ModelToSourceConversionException("Could not covert model", pce);
            }

        }

    }

    private static class JDomModelToSourceConverterImpl extends JDomModelToSourceConverter {

        public org.jdom.Document convertToDocument(Map model) throws ModelToSourceConversionException {

            Person person = (Person)model.get("person");
            org.jdom.Element name = new org.jdom.Element("name");
            name.setText(person.getName());
            org.jdom.Element age = new org.jdom.Element("age");
            age.setText(String.valueOf(person.getAge()));
            org.jdom.Element root = new org.jdom.Element("person");
            root.addContent(name);
            root.addContent(age);
            return new org.jdom.Document(root);
        }
    }

    private static class SAXModelToSourceConverterImpl extends SAXModelToSourceConverter {

        public void convert(Map model, ContentHandler contentHandler) throws SAXException {
            Person person = (Person)model.get("person");

            startDocument(contentHandler);
            startElement("person", contentHandler);
            startElement("name", contentHandler);
            text(person.getName(), contentHandler);
            endElement("name", contentHandler);
            startElement("age", contentHandler);
            text(String.valueOf(person.getAge()), contentHandler);
            endElement("age", contentHandler);
            endElement("person", contentHandler);
            endDocument(contentHandler);
        }

    }
}