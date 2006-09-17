package org.springmodules.xt.ajax.component;

import org.springmodules.xt.test.xml.XMLEnhancedTestCase;

/**
 * @author Sergio Bossa
 */
public class AnchorTest  extends XMLEnhancedTestCase {
    
    public AnchorTest(String testName) {
        super(testName);
    }

    public void testAddAttribute() throws Exception {
        Anchor anchor = new Anchor("www.example.org");
        anchor.addAttribute("id", "test-id");
        
        String rendering = anchor.render();
        System.out.println(rendering);
        assertXpathEvaluatesTo("test-id", "/a/@id", rendering);
    }

    public void testRender() throws Exception {
        Anchor anchor = new Anchor("www.example.org", new SimpleText("Test"));
        
        String rendering = anchor.render();
        System.out.println(rendering);
        assertXpathEvaluatesTo("www.example.org", "/a/@href", rendering);
        assertXpathEvaluatesTo("Test", "/a", rendering);
    }
}
