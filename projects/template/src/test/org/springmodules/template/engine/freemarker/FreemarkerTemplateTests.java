package org.springmodules.template.engine.freemarker;

import java.io.StringReader;
import java.util.Map;
import java.util.HashMap;

import junit.framework.*;
import freemarker.template.Template;
import freemarker.template.Configuration;

/**
 * @author Uri Boness
 */
public class FreemarkerTemplateTests extends TestCase {

    public void testGenerate() throws Exception {
        Configuration conf = new Configuration();
        Template template = new Template("name", new StringReader("${name}"), conf);
        FreemarkerTemplate ft = new FreemarkerTemplate(template);

        Map model = new HashMap();
        model.put("name", "Daan");
        String name = ft.generate(model);

        assertEquals("Daan", name);
    }

    
}