package org.springmodules.xt.ajax.component;

import java.util.LinkedList;
import java.util.List;
import org.springmodules.xt.test.domain.Employee;
import org.springmodules.xt.test.xml.XMLEnhancedTestCase;

/**
 *
 * @author Sergio Bossa
 */
public class TableRowTest extends XMLEnhancedTestCase {
    
    public TableRowTest(String testName) {
        super(testName);
    }
    
    public void testAddAttribute() throws Exception {
        TableRow tableRow = new TableRow();
        tableRow.addAttribute("id", "testId");
        
        String rendering = tableRow.render();
        System.out.println(rendering);
        
        assertXpathEvaluatesTo("testId", "/tr/@id", rendering);
    }

    public void testAddTableData() throws Exception {
        TableRow tableRow = new TableRow();
        TableData tableData1 = new TableData(new TaggedText("data1", TaggedText.Tag.SPAN));
        TableData tableData2 = new TableData(new TaggedText("data2", TaggedText.Tag.SPAN));

        tableRow.addTableData(tableData1);
        tableRow.addTableData(tableData2);
        
        String rendering = tableRow.render();
        
        System.out.println(rendering);
        
        assertXpathEvaluatesTo("data1", "/tr/td[position() = 1]/span", rendering);
        assertXpathEvaluatesTo("data2", "/tr/td[position() = 2]/span", rendering);
    }

    public void testTableRowConstructor1() throws Exception {
        TableData tableData1 = new TableData(new TaggedText("data1", TaggedText.Tag.SPAN));
        TableData tableData2 = new TableData(new TaggedText("data2", TaggedText.Tag.SPAN));
        List<TableData> tdList = new LinkedList<TableData>();
        tdList.add(tableData1);
        tdList.add(tableData2);
        
        TableRow tableRow = new TableRow(tdList);
        
        String rendering = tableRow.render();
        System.out.println(rendering);
        
        assertXpathEvaluatesTo("data1", "/tr/td[position() = 1]/span", rendering);
        assertXpathEvaluatesTo("data2", "/tr/td[position() = 2]/span", rendering);
    }
    
    public void testTableRowConstructor2() throws Exception {
        Employee emp = new Employee();
        emp.setMatriculationCode("123");
        emp.setFirstname("Sergio");
        emp.setSurname("Bossa");
        
        TableRow tableRow = new TableRow(emp, new String[]{"matriculationCode", "firstname", "surname"}, null);
        
        String rendering = tableRow.render();
        System.out.println(rendering);
        
        assertXpathEvaluatesTo("123", "/tr/td[position() = 1]", rendering);
        assertXpathEvaluatesTo("Sergio", "/tr/td[position() = 2]", rendering);
        assertXpathEvaluatesTo("Bossa", "/tr/td[position() = 3]", rendering);
    }
}
