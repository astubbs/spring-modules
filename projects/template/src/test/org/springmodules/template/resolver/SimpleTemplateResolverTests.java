package org.springmodules.template.resolver;

import java.util.Locale;

import junit.framework.*;
import org.easymock.MockControl;
import org.springmodules.template.TemplateEngine;
import org.springmodules.template.Template;
import org.springmodules.util.StringResource;
import org.springframework.core.io.ResourceLoader;

/**
 * @author Uri Boness
 */
public class SimpleTemplateResolverTests extends TestCase {

    private SimpleTemplateResolver resolver;

    private TemplateEngine engine;
    private MockControl engineControl;

    private ResourceLoader loader;
    private MockControl loaderControl;

    protected void setUp() throws Exception {

        engineControl = MockControl.createControl(TemplateEngine.class);
        engine = (TemplateEngine)engineControl.getMock();

        loaderControl = MockControl.createControl(ResourceLoader.class);
        loader = (ResourceLoader)loaderControl.getMock();

        resolver = new SimpleTemplateResolver();
        resolver.setEngine(engine);
        resolver.setResourceLoader(loader);
        resolver.setPrefix("prefix/");
        resolver.setExtension(".ext");
        resolver.afterPropertiesSet();
    }

    public void testResolve() throws Exception {
        String name = "name";

        StringResource resource = new StringResource("resource");
        loaderControl.expectAndReturn(loader.getResource("prefix/name.ext"), resource);

        Template template = new DummyTemplate();
        engineControl.expectAndReturn(engine.createTemplate(resource, "UTF-8"), template);

        loaderControl.replay();
        engineControl.replay();

        Template result1 = resolver.resolve(name);
        Template result2 = resolver.resolve(name);

        assertSame(template, result1);
        assertSame(result1, result2);

        loaderControl.verify();
        engineControl.verify();
    }

    public void testResolve_WithEncoding() throws Exception {
        String name = "name";
        String encoding = "encoding";

        StringResource resource = new StringResource("resource");
        loaderControl.expectAndReturn(loader.getResource("prefix/name.ext"), resource);

        Template template = new DummyTemplate();
        engineControl.expectAndReturn(engine.createTemplate(resource, encoding), template);

        loaderControl.replay();
        engineControl.replay();

        Template result1 = resolver.resolve(name, encoding);
        Template result2 = resolver.resolve(name, encoding);

        assertSame(template, result1);
        assertSame(result1, result2);

        loaderControl.verify();
        engineControl.verify();
    }

    public void testResolve_WithEncodingAndLocale() throws Exception {
        String name = "name";
        String encoding = "encoding";

        StringResource resource = new StringResource("resource");
        loaderControl.expectAndReturn(loader.getResource("prefix/name_en_US.ext"), null);
        loaderControl.expectAndReturn(loader.getResource("prefix/name_en.ext"), null);
        loaderControl.expectAndReturn(loader.getResource("prefix/name.ext"), resource);

        Template template = new DummyTemplate();
        engineControl.expectAndReturn(engine.createTemplate(resource, encoding), template);

        loaderControl.replay();
        engineControl.replay();

        Template result1 = resolver.resolve(name, encoding, Locale.US);
        Template result2 = resolver.resolve(name, encoding, Locale.US);

        assertSame(template, result1);
        assertSame(result1, result2);

        loaderControl.verify();
        engineControl.verify();
    }
}