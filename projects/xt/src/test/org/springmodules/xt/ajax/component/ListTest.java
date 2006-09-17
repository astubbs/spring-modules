package org.springmodules.xt.ajax.component;

import java.util.LinkedList;
import org.springmodules.xt.test.xml.XMLEnhancedTestCase;

/**
 * @author Sergio Bossa
 */
public class ListTest extends XMLEnhancedTestCase {
    
    public ListTest(String testName) {
        super(testName);
    }

    public void testAddItem() throws Exception {
        List list = new List(List.ListType.UNORDERED);
        list.addItem(new ListItem(new SimpleText("Test")));
        
        String rendering = list.render();
        System.out.println(rendering);
        assertXpathEvaluatesTo("Test", "/ul/li", rendering);
        
        list.addItem(new ListItem(new SimpleText("Test-2")));
        
        rendering = list.render();
        System.out.println(rendering);
        assertXpathEvaluatesTo("Test-2", "/ul/li[position() = 2]", rendering);
    }

    public void testAddAttribute() throws Exception {
        List list = new List(List.ListType.UNORDERED);
        list.addAttribute("id", "test-id");
        
        String rendering = list.render();
        System.out.println(rendering);
        assertXpathEvaluatesTo("test-id", "/ul/@id", rendering);
    }

    public void testRender() throws Exception {
        List list = null;
        
        java.util.List items = new LinkedList();
        items.add(new ListItem(new SimpleText("Test")));
        
        list = new List(List.ListType.UNORDERED, items);
        
        String rendering = list.render();
        System.out.println(rendering);
        assertXpathEvaluatesTo("Test", "/ul/li", rendering);
        
        list = new List(List.ListType.ORDERED, items);
        
        rendering = list.render();
        System.out.println(rendering);
        assertXpathEvaluatesTo("Test", "/ol/li", rendering);
    }
}
