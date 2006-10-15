package org.springmodules.prevayler;

import org.springmodules.prevayler.test.domain.Employee;
import org.springmodules.prevayler.test.domain.EmployeeImpl;
import org.springmodules.prevayler.system.PrevalentSystem;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Sergio Bossa
 */
public class SimpleSearchAndUpdatePrevaylerCallback implements PrevaylerCallback {
    
    public Object doInTransaction(PrevalentSystem system) {
        Iterator emps = system.get(Employee.class).iterator();
        List result = new LinkedList();
        while (emps.hasNext()) {
            EmployeeImpl e = (EmployeeImpl) emps.next();
            if (e.getMatriculationCode().equals("a1")) {
                e.setFirstname("Sergio");
                e.setSurname("Bossa");
                
                system.update(e);
                result.add(e);
            }
        }
        return result;
    }
}
