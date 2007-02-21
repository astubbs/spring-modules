package org.springmodules.xt.ajax.component;

import org.springmodules.xt.test.domain.Employee;
import org.springmodules.xt.test.xml.XMLEnhancedTestCase;

/**
 *
 * @author Sergio Bossa
 */
public class TableHeaderDataTest extends XMLEnhancedTestCase {
    
    public TableHeaderDataTest(String testName) {
        super(testName);
    }
    
    public void testAddAttribute() throws Exception {
        TableHeaderData tableData = new TableHeaderData(new SimpleText("test"));
        tableData.addAttribute("id", "testId");
        
        String rendering = tableData.render();
        System.out.println(rendering);
        
        assertXpathEvaluatesTo("testId", "/th/@id", rendering);
    }

    public void testRenderPart1() throws Exception {
        TableHeaderData tableData = new TableHeaderData(new TaggedText("test", TaggedText.Tag.SPAN));
        
        String rendering = tableData.render();
        System.out.println(rendering);
        
        assertXpathEvaluatesTo("test", "/th/span", rendering);
    }
    
    public void testRenderPart2() throws Exception {
        Employee emp = new Employee();
        emp.setMatriculationCode("123");
        
        TableHeaderData tableData = new TableHeaderData(emp, "matriculationCode", null);
        
        String rendering = tableData.render();
        System.out.println(rendering);
        
        assertXpathEvaluatesTo("123", "/th", rendering);
    }
    
    public void testRenderPart3() throws Exception {
        Employee emp = new Employee();
        
        // Matriculation code is null:
        TableHeaderData tableData = new TableHeaderData(emp, "matriculationCode", null);
        String rendering = tableData.render();
        
        System.out.println(rendering);
        
        assertXpathEvaluatesTo("", "/td", rendering);
    }
}
