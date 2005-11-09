package org.springmodules.template.providers.webmacro;

import java.util.*;

import org.springmodules.template.*;
import org.springmodules.template.resolvers.*;
import org.springmodules.template.sources.*;
import junit.framework.*;

/**
 * @author Uri Boness
 */
public class WebMacroTemplateIntegrationTests extends TestCase {

    public void testTemplateSetWithSourcesArray() throws Exception {

        String main = "#parse as template \"other\"";
        String other = "Hello $name";

        WebMacroTemplateFactory factory = new WebMacroTemplateFactory();

        TemplateSet set = factory.createTemplateSet(new TemplateSource[] {
            new StringTemplateSource("main", main),
            new StringTemplateSource("other", other)
        });

        Map model = new HashMap();
        model.put("name", "John");
        Template template = set.getTemplate("main");
        String output = template.generate(model);

        assertEquals("Hello John", output);
    }

    public void testTemplateSetWithSourceResolver() throws Exception {

        String main = "#parse as template \"other\"";
        String other = "Hello $name";

        MapTemplateSourceResolver resolver = new MapTemplateSourceResolver();
        resolver.addTemplateSource("main", new StringTemplateSource(main));
        resolver.addTemplateSource("other", new StringTemplateSource(other));

        WebMacroTemplateFactory factory = new WebMacroTemplateFactory();
        TemplateSet set = factory.createTemplateSet(resolver);

        Map model = new HashMap();
        model.put("name", "John");
        Template template = set.getTemplate("main");
        String output = template.generate(model);

        assertEquals("Hello John", output);
    }

}
