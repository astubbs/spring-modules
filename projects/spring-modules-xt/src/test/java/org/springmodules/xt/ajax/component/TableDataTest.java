package org.springmodules.xt.ajax.component;

import org.springmodules.xt.test.domain.Employee;
import org.springmodules.xt.test.xml.XMLEnhancedTestCase;

/**
 *
 * @author Sergio Bossa
 */
public class TableDataTest extends XMLEnhancedTestCase {
    
    public TableDataTest(String testName) {
        super(testName);
    }
    
    public void testAddAttribute() throws Exception {
        TableData tableData = new TableData(new SimpleText("test"));
        tableData.addAttribute("id", "testId");
        
        String rendering = tableData.render();
        System.out.println(rendering);
        
        assertXpathEvaluatesTo("testId", "/td/@id", rendering);
    }

    public void testRenderPart1() throws Exception {
        TableData tableData = new TableData(new TaggedText("test", TaggedText.Tag.SPAN));
        
        String rendering = tableData.render();
        System.out.println(rendering);
        
        assertXpathEvaluatesTo("test", "/td/span", rendering);
    }
    
    public void testRenderPart2() throws Exception {
        Employee emp = new Employee();
        emp.setMatriculationCode("123");
        
        TableData tableData = new TableData(emp, "matriculationCode", null);
        
        String rendering = tableData.render();
        System.out.println(rendering);
        
        assertXpathEvaluatesTo("123", "/td", rendering);
    }
    
    public void testRenderPart3() throws Exception {
        Employee emp = new Employee();
        
        // Matriculation code is null:
        TableData tableData = new TableData(emp, "matriculationCode", null);
        String rendering = tableData.render();
        
        System.out.println(rendering);
        
        assertXpathEvaluatesTo("", "/td", rendering);
    }
}
