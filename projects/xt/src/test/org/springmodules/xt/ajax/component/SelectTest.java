package org.springmodules.xt.ajax.component;

import java.util.LinkedList;
import java.util.List;
import org.springmodules.xt.test.xml.XMLEnhancedTestCase;

/**
 *
 * @author Sergio Bossa
 */
public class SelectTest extends XMLEnhancedTestCase {
    
    public SelectTest(String testName) {
        super(testName);
    }
    
    public void testAddAttribute() throws Exception {
        Select select = new Select("test");
       
        select.addAttribute("id", "testId");
        
        String rendering = select.render();
        
        System.out.println(rendering);
        
        assertXpathEvaluatesTo("testId", "/select/@id", rendering);
    }

    public void testAddOption() throws Exception {
        Select select = new Select("test");
        Option option1 = new Option("value1", "content1");
        Option option2 = new Option("value2", "content2");
        
        select.addOption(option1);
        select.addOption(option2);
        
        String rendering = select.render();
        
        System.out.println(rendering);
        
        assertXpathEvaluatesTo("content1", "/select/option[@value = 'value1']", rendering);
        assertXpathEvaluatesTo("content2", "/select/option[@value = 'value2']", rendering);
    }

    public void testSelectConstructor() throws Exception {
        Option option1 = new Option("value1", "content1");
        Option option2 = new Option("value2", "content2");
        List<Option> optionList = new LinkedList<Option>();
        optionList.add(option1);
        optionList.add(option2);
        
        Select select = new Select("test", optionList);
        
        String rendering = select.render();
        
        System.out.println(rendering);
        
        assertXpathEvaluatesTo("content1", "/select/option[@value = 'value1']", rendering);
        assertXpathEvaluatesTo("content2", "/select/option[@value = 'value2']", rendering);
    }
}
