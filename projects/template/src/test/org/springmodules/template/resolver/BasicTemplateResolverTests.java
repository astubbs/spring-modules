package org.springmodules.template.resolver;

import java.util.Locale;

import junit.framework.TestCase;
import org.easymock.MockControl;
import org.springframework.core.io.ResourceLoader;
import org.springmodules.template.Template;
import org.springmodules.template.TemplateEngine;
import org.springmodules.util.StringResource;

/**
 * @author Uri Boness
 */
public class BasicTemplateResolverTests extends TestCase {

    private BasicTemplateResolver resolver;

    private TemplateEngine engine;
    private MockControl engineControl;

    private ResourceLoader loader;
    private MockControl loaderControl;

    protected void setUp() throws Exception {

        engineControl = MockControl.createControl(TemplateEngine.class);
        engine = (TemplateEngine)engineControl.getMock();

        loaderControl = MockControl.createControl(ResourceLoader.class);
        loader = (ResourceLoader)loaderControl.getMock();

        resolver = new BasicTemplateResolver();
        resolver.setEngine(engine);
        resolver.setResourceLoader(loader);
        resolver.setExtension(".ext");
        resolver.afterPropertiesSet();
    }

    public void testResolve() throws Exception {
        String name = "name";

        StringResource resource = new StringResource("resource");
        loaderControl.expectAndReturn(loader.getResource("name.ext"), resource);

        Template template = new DummyTemplate();
        engineControl.expectAndReturn(engine.createTemplate(resource, "UTF-8"), template);

        loaderControl.replay();
        engineControl.replay();

        Template result = resolver.resolve(name);

        assertSame(template, result);

        loaderControl.verify();
        engineControl.verify();
    }

    public void testResolve_WithEncoding() throws Exception {
        String name = "name";
        String encoding = "encoding";

        StringResource resource = new StringResource("resource");
        loaderControl.expectAndReturn(loader.getResource("name.ext"), resource);

        Template template = new DummyTemplate();
        engineControl.expectAndReturn(engine.createTemplate(resource, encoding), template);

        loaderControl.replay();
        engineControl.replay();

        Template result = resolver.resolve(name, encoding);

        assertSame(template, result);

        loaderControl.verify();
        engineControl.verify();
    }

    public void testResolve_WithEncodingAndLocale() throws Exception {
        String name = "name";
        String encoding = "encoding";

        StringResource resource = new StringResource("resource");
        loaderControl.expectAndReturn(loader.getResource("name_en_US.ext"), null);
        loaderControl.expectAndReturn(loader.getResource("name_en.ext"), null);
        loaderControl.expectAndReturn(loader.getResource("name.ext"), resource);


        Template template = new DummyTemplate();
        engineControl.expectAndReturn(engine.createTemplate(resource, encoding), template);

        loaderControl.replay();
        engineControl.replay();

        Template result = resolver.resolve(name, encoding, Locale.US);

        assertSame(template, result);

        loaderControl.verify();
        engineControl.verify();
    }

}