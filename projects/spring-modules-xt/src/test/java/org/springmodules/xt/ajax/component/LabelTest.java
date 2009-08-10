package org.springmodules.xt.ajax.component;

import org.springmodules.xt.test.xml.XMLEnhancedTestCase;

/**
 * @author Sergio Bossa
 */
public class LabelTest extends XMLEnhancedTestCase {
    
    public LabelTest(String testName) {
        super(testName);
    }

    public void testRender() throws Exception {
        Label label = new Label("element", new TaggedText("test", TaggedText.Tag.SPAN));
        
        String rendering = label.render();
        System.out.println(rendering);
        
        assertXpathEvaluatesTo("element", "/label/@for", rendering);
        assertXpathEvaluatesTo("test", "/label/span", rendering);
    }
}
