package org.springmodules.prevayler.id;

import junit.framework.TestCase;
import org.springmodules.prevayler.test.domain.Employee;
import org.springmodules.prevayler.test.domain.EmployeeImpl;
import org.springmodules.prevayler.test.domain.ManagerImpl;
import org.springmodules.prevayler.test.domain.Office;
import org.springmodules.prevayler.test.domain.OfficeImpl;
import org.springmodules.prevayler.system.PrevalenceInfo;

/**
 * @author Sergio Bossa
 */
public class DefaultIdMergerTest extends TestCase {
    
    private DefaultIdMerger merger;
    
    public DefaultIdMergerTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
        PrevalenceInfo info = new PrevalenceInfo();
        info.setPrevalentClasses(new Class[]{Employee.class, Office.class});
        info.setIdGenerationStrategy(new DefaultLongIdGenerator());
        info.setIdResolutionStrategy(new DefaultIdResolver());
        this.merger = new DefaultIdMerger(info);
    }

    public void testMerge() throws Exception {
        OfficeImpl o1 = new OfficeImpl("o1", "Office 1");
        ManagerImpl m1 = new ManagerImpl("m1");
        EmployeeImpl e1 = new EmployeeImpl("e1");
        o1.setId(new Long(1));
        m1.setId(new Long(1));
        e1.setId(new Long(2));
        e1.setOffice(o1);
        m1.addManagedEmployee(e1);
        
        OfficeImpl o2 = new OfficeImpl("o1", "Office 1");
        ManagerImpl m2 = new ManagerImpl("m1");
        EmployeeImpl e2 = new EmployeeImpl("e1");
        e2.setOffice(o2);
        m2.addManagedEmployee(e2);
        
        assertNull(o2.getId());
        assertNull(m2.getId());
        assertNull(e2.getId());
        
        this.merger.merge(m1, m2);
        
        assertNotNull(o2.getId());
        assertNotNull(m2.getId());
        assertNotNull(e2.getId());
    }
}
