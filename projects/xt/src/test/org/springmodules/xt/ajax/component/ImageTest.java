package org.springmodules.xt.ajax.component;

import org.springmodules.xt.test.xml.XMLEnhancedTestCase;

/**
 * @author Sergio Bossa
 */
public class ImageTest extends XMLEnhancedTestCase {
    
    public ImageTest(String testName) {
        super(testName);
    }

    public void testAddAttribute() throws Exception {
        Image img = new Image("test.png", "Test Image");
        img.addAttribute("id", "test-id");
        
        String rendering = img.render();
        System.out.println(rendering);
        assertXpathEvaluatesTo("test-id", "/img/@id", rendering);
    }

    public void testRender() throws Exception {
        Image img = new Image("test.png", "Test Image");
        
        String rendering = img.render();
        System.out.println(rendering);
        assertXpathEvaluatesTo("test.png", "/img/@src", rendering);
        assertXpathEvaluatesTo("Test Image", "/img/@alt", rendering);
    }
}
