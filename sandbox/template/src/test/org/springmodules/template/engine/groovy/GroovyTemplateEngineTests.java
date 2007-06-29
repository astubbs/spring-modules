package org.springmodules.template.engine.groovy;

import java.util.Map;
import java.util.HashMap;
import java.io.StringWriter;

import junit.framework.TestCase;
import org.springmodules.util.StringResource;
import org.springmodules.template.Template;

/**
 * @author Uri Boness
 */
public class GroovyTemplateEngineTests extends TestCase {

    private GroovyTemplateEngine engine;

    protected void setUp() throws Exception {
        engine = new GroovyTemplateEngine();
    }

    public void testCreateTemplate() throws Exception {
        String script = "Hello ${name}";
        StringResource resource = new StringResource(script);
        Template template = engine.createTemplate(resource);
        
        StringWriter writer = new StringWriter();
        Map model = new HashMap();
        model.put("name", "Lian");
        template.generate(writer, model);

        assertEquals("Hello Lian", writer.toString());
    }
}