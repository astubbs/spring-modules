package org.springmodules.template.providers.xslt;

import java.util.*;
import java.io.*;

import javax.xml.parsers.*;

import junit.framework.*;
import org.springmodules.template.*;
import org.springmodules.template.sources.*;
import org.springmodules.template.resolvers.*;
import org.w3c.dom.*;
import org.xml.sax.*;

/**
 * @author Uri Boness
 */
public class DomXsltTemplateFactoryTest extends TestCase {


    public void testCreateTemplateSetUsingTemplateSourcesWithImports() throws Exception {
        DomXsltTemplateFactory factory = new DomXsltTemplateFactory();
        TemplateSet templateSet = factory.createTemplateSet(createTemplateSources());

        Map model = buildDocumentModel();
        Template template = templateSet.getTemplate("main");

        StringWriter writer = new StringWriter();
        template.generate(writer, model);

        assertEquals("John,40", writer.toString());
    }

    public void testCreateTemplateSetUsingTemplateSourceResolverWithImports() throws Exception {
        DomXsltTemplateFactory factory = new DomXsltTemplateFactory();
        TemplateSet templateSet = factory.createTemplateSet(createTemplateSourceResolver());

        Map model = buildDocumentModel();
        Template template = templateSet.getTemplate("main");

        StringWriter writer = new StringWriter();
        template.generate(writer, model);

        assertEquals("John,40", writer.toString());
    }

    private TemplateSource[] createTemplateSources() throws Exception {

        StringBuffer xsl = new StringBuffer();

        xsl.append("<?xml version='1.0'?>");
        xsl.append("<xsl:stylesheet version='1.0' xmlns:xsl='http://www.w3.org/1999/XSL/Transform' >");
        xsl.append("    <xsl:template match='/'>");
        xsl.append("        <xsl:value-of select='person/name'/>,<xsl:value-of select='person/age' />");
        xsl.append("    </xsl:template>");
        xsl.append("</xsl:stylesheet>");

        TemplateSource helperSource = new StringTemplateSource("helper", xsl.toString());

        xsl = new StringBuffer();

        xsl.append("<?xml version='1.0'?>");
        xsl.append("<xsl:stylesheet version='1.0' xmlns:xsl='http://www.w3.org/1999/XSL/Transform' >");
        xsl.append("    <xsl:import href='helper'/>");
        xsl.append("    <xsl:output method='text' />");
        xsl.append("    <xsl:template match='/'>");
        xsl.append("        <xsl:apply-imports/>");
        xsl.append("    </xsl:template>");
        xsl.append("</xsl:stylesheet>");

        TemplateSource mainSource = new StringTemplateSource("main", xsl.toString());

        return new TemplateSource[] { helperSource, mainSource };
    }

    private TemplateSourceResolver createTemplateSourceResolver() throws Exception {

        StringBuffer xsl = new StringBuffer();

        xsl.append("<?xml version='1.0'?>");
        xsl.append("<xsl:stylesheet version='1.0' xmlns:xsl='http://www.w3.org/1999/XSL/Transform' >");
        xsl.append("    <xsl:template match='/'>");
        xsl.append("        <xsl:value-of select='person/name'/>,<xsl:value-of select='person/age' />");
        xsl.append("    </xsl:template>");
        xsl.append("</xsl:stylesheet>");

        TemplateSource helperSource = new StringTemplateSource("helper", xsl.toString());

        xsl = new StringBuffer();

        xsl.append("<?xml version='1.0'?>");
        xsl.append("<xsl:stylesheet version='1.0' xmlns:xsl='http://www.w3.org/1999/XSL/Transform' >");
        xsl.append("    <xsl:import href='helper'/>");
        xsl.append("    <xsl:output method='text' />");
        xsl.append("    <xsl:template match='/'>");
        xsl.append("        <xsl:apply-imports/>");
        xsl.append("    </xsl:template>");
        xsl.append("</xsl:stylesheet>");

        TemplateSource mainSource = new StringTemplateSource("main", xsl.toString());

        MapTemplateSourceResolver resolver = new MapTemplateSourceResolver();
        resolver.addTemplateSource("helper", helperSource);
        resolver.addTemplateSource("main", mainSource);

        return resolver;
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

}
