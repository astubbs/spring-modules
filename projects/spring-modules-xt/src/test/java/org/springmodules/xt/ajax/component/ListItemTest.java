package org.springmodules.xt.ajax.component;

import org.springmodules.xt.test.xml.XMLEnhancedTestCase;

/**
 * @author Sergio Bossa
 */
public class ListItemTest extends XMLEnhancedTestCase {
    
    public ListItemTest(String testName) {
        super(testName);
    }

    public void testAddAttribute() throws Exception {
        ListItem item = new ListItem(new SimpleText("Test"));
        item.addAttribute("id", "test-id");
        
        String rendering = item.render();
        System.out.println(rendering);
        assertXpathEvaluatesTo("test-id", "/li/@id", rendering);
    }

    public void testRender() throws Exception {
        ListItem item = new ListItem(new SimpleText("Test"));
        
        String rendering = item.render();
        System.out.println(rendering);
        assertXpathEvaluatesTo("Test", "/li", rendering);
    }
    
}
