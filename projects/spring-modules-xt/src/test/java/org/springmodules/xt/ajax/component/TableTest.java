package org.springmodules.xt.ajax.component;

import org.springmodules.xt.test.domain.Employee;
import org.springmodules.xt.test.xml.XMLEnhancedTestCase;

/**
 *
 * @author Sergio Bossa
 */
public class TableTest extends XMLEnhancedTestCase {
    
    public TableTest(String testName) {
        super(testName);
    }

    public void testRender() throws Exception {
        Employee emp1 = new Employee();
        Employee emp2 = new Employee();
        emp1.setMatriculationCode("123");
        emp1.setFirstname("Sergio");
        emp1.setSurname("Bossa");
        emp2.setMatriculationCode("456");
        emp2.setFirstname("George");
        emp2.setSurname("Orwell");
        
        TableHeader header = new TableHeader(new String[]{"matriculationCode", "firstname", "surname"});
        
        TableRow tableRow1 = new TableRow(emp1, new String[]{"matriculationCode", "firstname", "surname"}, null);
        TableRow tableRow2 = new TableRow(emp2, new String[]{"matriculationCode", "firstname", "surname"}, null);
        
        Table table = new Table();
        
        table.addTableAttribute("id", "table");
        table.addTableBodyAttribute("id", "tbody");
        table.addTableHeaderAttribute("id", "thead");
        
        table.setTableHeader(header);
        table.addTableRow(tableRow1);
        table.addTableRow(tableRow2);
        
        String rendering = table.render();
        System.out.println(rendering);
        
        assertXpathEvaluatesTo("matriculationCode", "/table[@id = 'table']/thead[@id = 'thead']/tr/th[position() = 1]", rendering);
        assertXpathEvaluatesTo("123", "/table[@id = 'table']/tbody[@id = 'tbody']/tr[position() = 1]/td[position() = 1]", rendering);
        assertXpathEvaluatesTo("456", "/table[@id = 'table']/tbody[@id = 'tbody']/tr[position() = 2]/td[position() = 1]", rendering);
    }
}
