package org.springmodules.template;

import java.util.*;

import junit.framework.*;

/**
 * @author Uri Boness
 */
public abstract class AbstractTemplateTest extends TestCase {

    protected Map model;

    protected void setUp() throws Exception {
        model = createCommonModel();
    }

    public void testGenerate() throws Exception {
        Template template = createTemplateForCommonModel();
        String output = template.generate(model);
        assertEquals(getOutputForCommonModel(), output);
    }

    public void testMultipleGenerations() throws Exception {
        Template template = createTemplateForCommonModel();
        for (int i=0; i<5; i++) {
            try {
                String output = template.generate(model);
                assertEquals(getOutputForCommonModel(), output);
            } catch (Throwable t) {
                fail("Failed to generate output on attempt #" + i + ". " + t.getMessage());
            }
        }
    }

    protected abstract Template createTemplateForCommonModel() throws Exception;

    protected abstract String getOutputForCommonModel();

    private Map createCommonModel() {
        Map model = new HashMap();
        model.put("person", new Person("John", 40));
        return model;
    }
}
