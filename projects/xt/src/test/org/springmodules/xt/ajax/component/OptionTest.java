package org.springmodules.xt.ajax.component;

import org.springmodules.xt.test.domain.Employee;
import org.springmodules.xt.test.xml.XMLEnhancedTestCase;

/**
 *
 * @author Sergio Bossa
 */
public class OptionTest extends XMLEnhancedTestCase {
    
    public OptionTest(String testName) {
        super(testName);
    }
    
    public void testAddAttribute() throws Exception {
        Option option = new Option("value", "content");
        option.addAttribute("id", "testId");
        
        String rendering = option.render();
        System.out.println(rendering);
        
        assertXpathEvaluatesTo("testId", "/option/@id", rendering);
    }

    public void testRenderPart1() throws Exception {
        Option option = new Option("value", "content");
        
        String rendering = option.render();
        System.out.println(rendering);
        
        assertXpathEvaluatesTo("value", "/option/@value", rendering);
        assertXpathEvaluatesTo("content", "/option", rendering);
    }
    
    public void testRenderPart2() throws Exception {
        Employee emp = new Employee();
        
        emp.setMatriculationCode("123");
        emp.setSurname("Bossa");
        
        Option option = new Option(emp, "matriculationCode", "surname");
        
        String rendering = option.render();
        System.out.println(rendering);
        
        assertXpathEvaluatesTo("123", "/option/@value", rendering);
        assertXpathEvaluatesTo("Bossa", "/option", rendering);
    }
    
    public void testRenderPart3() throws Exception {
        Employee emp = new Employee();
        
        // Matriculation code and surname are null:
        Option option = new Option(emp, "matriculationCode", "surname");
        String rendering = option.render();
        
        System.out.println(rendering);
        
        assertXpathEvaluatesTo("", "/option/@value", rendering);
        assertXpathEvaluatesTo("", "/option", rendering);
    }
}
