package org.springmodules.template.engine.freemarker;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.SimpleScalar;
import junit.framework.TestCase;
import org.easymock.MockControl;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springmodules.template.Template;
import org.springmodules.util.StringResource;

/**
 * @author Uri Boness
 */
public class FreemarkerTemplateEngineTests extends TestCase {

    private FreemarkerTemplateEngine engine;

    private ResourceLoader loader;

    private MockControl loaderControl;

    protected void setUp() throws Exception {

        loaderControl = MockControl.createControl(ResourceLoader.class);
        loader = (ResourceLoader) loaderControl.getMock();

        engine = new FreemarkerTemplateEngine(loader);
        engine.afterPropertiesSet();
    }

    public void testCreateTemplate() throws Exception {
        String encoding = "UTF-8";
        Resource resource = new StringResource("Hello ${name}");
        Template template = engine.createTemplate(resource, encoding);

        Map model = new HashMap();
        model.put("name", "Lian");
        assertEquals("Hello Lian", template.generate(model));
    }

    public void testCreateTemplate_WithInclude() throws Exception {
        String encoding = "UTF-8";

        loaderControl.expectAndReturn(loader.getResource("name_en_US"), null);
        loaderControl.expectAndReturn(loader.getResource("name_en"), null);
        loaderControl.expectAndReturn(loader.getResource("name"), new StringResource("Hello"));

        Resource resource = new StringResource("<#include \"name\">");

        loaderControl.replay();

        Template template = engine.createTemplate(resource, encoding);

        assertEquals("Hello", template.generate(new HashMap()));
        loaderControl.verify();
    }

    public void testCreateConfiguration() throws Exception {

        Properties settings = new Properties();
        settings.setProperty("tag_syntax", "auto_detect");

        Map vars = new HashMap();
        vars.put("name", "value");

        Configuration conf = FreemarkerTemplateEngine.createConfiguration(loader, settings, vars, null);

        TemplateLoader templateLoader = conf.getTemplateLoader();
        assertTrue(ResourceLoaderTemplateLoader.class.isInstance(templateLoader));
        ResourceLoaderTemplateLoader resourceLoaderTemplateLoader = (ResourceLoaderTemplateLoader) templateLoader;
        assertSame(loader, resourceLoaderTemplateLoader.getResourceLoader());

        assertEquals(6, conf.getSharedVariableNames().size());
        assertEquals("value", ((SimpleScalar) conf.getSharedVariable("name")).getAsString());

        assertEquals(Configuration.AUTO_DETECT_TAG_SYNTAX, conf.getTagSyntax());
    }

    

}