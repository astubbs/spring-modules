package org.springmodules.xt.ajax.component;

import java.util.ArrayList;
import java.util.Arrays;
import org.springmodules.xt.test.xml.XMLEnhancedTestCase;

/**
 * @author Sergio Bossa
 */
public class ListTest extends XMLEnhancedTestCase {
    
    public ListTest(String testName) {
        super(testName);
    }
    
    public void testUnorderedListConstructor() throws Exception {
        List list = new List(List.ListType.UNORDERED, 
                new ArrayList<ListItem>(Arrays.asList(new ListItem(new SimpleText("Test1")), new ListItem(new SimpleText("Test2")))));
        
        String rendering = list.render();
        System.out.println(rendering);
        
        assertXpathEvaluatesTo("Test1", "/ul/li[position() = 1]", rendering);
        assertXpathEvaluatesTo("Test2", "/ul/li[position() = 2]", rendering);
    }
    
    public void testOrderedListConstructor() throws Exception {
        List list = new List(List.ListType.ORDERED, 
                new ArrayList<ListItem>(Arrays.asList(new ListItem(new SimpleText("Test1")), new ListItem(new SimpleText("Test2")))));
        
        String rendering = list.render();
        System.out.println(rendering);
        
        assertXpathEvaluatesTo("Test1", "/ol/li[position() = 1]", rendering);
        assertXpathEvaluatesTo("Test2", "/ol/li[position() = 2]", rendering);
    }

    public void testAddItemToUnorderedList() throws Exception {
        List list = new List(List.ListType.UNORDERED);
        
        list.addItem(new ListItem(new SimpleText("Test1")));
        String rendering = list.render();
        System.out.println(rendering);
        assertXpathEvaluatesTo("Test1", "/ul/li[position() = 1]", rendering);
        
        list.addItem(new ListItem(new SimpleText("Test2")));
        rendering = list.render();
        System.out.println(rendering);
        assertXpathEvaluatesTo("Test2", "/ul/li[position() = 2]", rendering);
    }
    
    public void testAddItemToOrderedList() throws Exception {
        List list = new List(List.ListType.ORDERED);
        
        list.addItem(new ListItem(new SimpleText("Test1")));
        String rendering = list.render();
        System.out.println(rendering);
        assertXpathEvaluatesTo("Test1", "/ol/li[position() = 1]", rendering);
        
        list.addItem(new ListItem(new SimpleText("Test2")));
        rendering = list.render();
        System.out.println(rendering);
        assertXpathEvaluatesTo("Test2", "/ol/li[position() = 2]", rendering);
    }

    public void testAddAttribute() throws Exception {
        List list = new List(List.ListType.UNORDERED);
        list.addAttribute("id", "test-id");
        
        String rendering = list.render();
        System.out.println(rendering);
        assertXpathEvaluatesTo("test-id", "/ul/@id", rendering);
    }
}
