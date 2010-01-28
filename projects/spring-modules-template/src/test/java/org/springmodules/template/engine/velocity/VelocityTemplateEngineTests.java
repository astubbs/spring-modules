package org.springmodules.template.engine.velocity;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;
import org.easymock.MockControl;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StopWatch;
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


    public void testPerformance() throws Exception {
        Map model = new HashMap();
        model.put("from", new Person("name1", "email1"));
        Person[] people = new Person[] {
            new Person("name2", "email2"),
            new Person("name3", "email3")
        };
        model.put("tos", people);

        people = new Person[] {
            new Person("name4", "email4")
        };
        model.put("ccs", people);

        people = new Person[] {
            new Person("name5", "email5")
        };
        model.put("bccs", people);
        model.put("subject", "subject1");

        String encoding = "UTF-8";
        Resource resource = new ClassPathResource("temp.vm", getClass());
        Template template = engine.createTemplate(resource, encoding);

        long sum = 0;

        for (int i=0; i<100; i++) {
            StopWatch sw = new StopWatch();
            sw.start();
            template.generate(model);
            sw.stop();
            sum += sw.getTotalTimeMillis();
        }

        System.out.println("took average of " + ((double)sum)/100 + " millis");

    }

}