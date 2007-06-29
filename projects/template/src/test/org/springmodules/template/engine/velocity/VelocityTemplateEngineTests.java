package org.springmodules.template.engine.velocity;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;
import org.easymock.MockControl;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springmodules.template.Template;
import org.springmodules.util.StringResource;

/**
 * @author Uri Boness
 */
public class VelocityTemplateEngineTests extends TestCase {

    private VelocityTemplateEngine engine;

    private ResourceLoader loader;

    private MockControl loaderControl;

    protected void setUp() throws Exception {
        loaderControl = MockControl.createControl(ResourceLoader.class);
        loader = (ResourceLoader) loaderControl.getMock();

        engine = new VelocityTemplateEngine(loader);
        engine.afterPropertiesSet();
    }

    public void testCreateTemplate() throws Exception {
        String encoding = "UTF-8";
        Resource resource = new StringResource("Hello $name");
        Template template = engine.createTemplate(resource, encoding);

        Map model = new HashMap();
        model.put("name", "Daan");
        assertEquals("Hello Daan", template.generate(model));
    }

    public void testCreateTemplate_WithInclude() throws Exception {
        String encoding = "UTF-8";

        // first, velocity calls the loader to load the resource
        loaderControl.expectAndReturn(loader.getResource("name"), new StringResource("Hello"));

        // second, velocity calls the loader when trying to get the last modified date of the resource.
        loaderControl.expectAndReturn(loader.getResource("name"), null);

        Resource resource = new StringResource("#include( \"name\" )");

        loaderControl.replay();

        Template template = engine.createTemplate(resource, encoding);

        assertEquals("Hello", template.generate(new HashMap()));
        loaderControl.verify();
    }


}