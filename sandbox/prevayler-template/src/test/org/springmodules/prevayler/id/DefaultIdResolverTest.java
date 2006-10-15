package org.springmodules.prevayler.id;

import org.springmodules.prevayler.test.domain.Employee;
import org.springmodules.prevayler.test.domain.EmployeeImpl;
import org.springmodules.prevayler.test.domain.Manager;
import org.springmodules.prevayler.test.domain.ManagerImpl;
import junit.framework.TestCase;

/**
 * @author Sergio Bossa
 */
public class DefaultIdResolverTest extends TestCase {

    private DefaultIdResolver resolver;
    
    public DefaultIdResolverTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
        this.resolver = new DefaultIdResolver();
    }

    public void testResolveId() {
        Employee emp = new EmployeeImpl("a1");
        assertNotNull(this.resolver.resolveId(emp));
        
        Manager man = new ManagerImpl("a1");
        assertNotNull(this.resolver.resolveId(man));
    }
}
