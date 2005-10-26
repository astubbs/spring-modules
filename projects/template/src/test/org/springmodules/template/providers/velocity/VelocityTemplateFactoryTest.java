package org.springmodules.template.providers.velocity;

import java.util.*;

import junit.framework.*;
import org.springmodules.template.*;
import org.springmodules.template.sources.*;
import org.springmodules.template.resolvers.*;

/**
 * @author Uri Boness
 */
public class VelocityTemplateFactoryTest extends TestCase {

    private Map model;

    private VelocityTemplateFactory factory;

    protected void setUp() throws Exception {

        factory = new VelocityTemplateFactory();
        factory.init();

        model = new HashMap();
        model.put("greeting", "Hello");
    }

    public void testCreateTemplate() throws Exception {
        StringTemplateSource source = new StringTemplateSource("${greeting} World");
        Template template = factory.createTemplate(source);
        String output = template.generate(model);
        assertEquals("Hello World", output);
    }

    public void testCreateTemplateSetWithSourcesArray() throws Exception {

        TemplateSource[] sources = new TemplateSource[] {
            new StringTemplateSource("greeting", "${greeting}"),
            new StringTemplateSource("main", "#parse('greeting') World")
        };

        TemplateSet templates = factory.createTemplateSet(sources);
        Template template = templates.getTemplate("main");

        String output = template.generate(model);

        assertEquals("Hello World", output);

    }

    public void testCreateTemplateSetWithSourceResolver() throws Exception {

        String greeting = "${greeting}";
        String main = "#parse('greeting') World";

        MapTemplateSourceResolver resolver = new MapTemplateSourceResolver();
        resolver.addTemplateSource("greeting", new StringTemplateSource(greeting));
        resolver.addTemplateSource("main", new StringTemplateSource(main));

        TemplateSet templates = factory.createTemplateSet(resolver);
        Template template = templates.getTemplate("main");

        String output = template.generate(model);

        assertEquals("Hello World", output);

    }

}
