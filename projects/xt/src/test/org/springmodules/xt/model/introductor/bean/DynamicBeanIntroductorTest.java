/*
 * Copyright 2006 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springmodules.xt.model.introductor.bean;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.springmodules.xt.test.domain.Employee;
import org.springmodules.xt.test.domain.IEmployee;
import org.springmodules.xt.test.domain.Office;
import org.springmodules.xt.test.domain.EmployeeView;

/**
 *
 * @author Sergio Bossa
 */
public class DynamicBeanIntroductorTest extends TestCase {
    
    private DynamicBeanIntroductor introductor;
    
    public DynamicBeanIntroductorTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
        this.introductor  = new DynamicBeanIntroductor();
    }

    protected void tearDown() throws Exception {
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(DynamicBeanIntroductorTest.class);
        
        return suite;
    }

    /**
     * Test of introduceInterfaces method, of class org.springmodules.xt.model.introductor.bean.DynamicBeanIntroductor.
     */
    public void testIntroduceInterfaces() {
        Employee target = new Employee();
        target.setFirstname("Sergio");
        target.setSurname("Bossa");
        
        Object introduced = this.introductor.introduceInterfaces(target, new Class[]{EmployeeView.class});
        
        assertTrue(introduced instanceof Employee);
        assertTrue(introduced instanceof  IEmployee);
        assertTrue(introduced instanceof EmployeeView);
        
        EmployeeView view = (EmployeeView) introduced;
        
        assertEquals(view.getFirstname(), "Sergio");
        assertEquals(view.getSurname(), "Bossa");
        
        view.setMatriculationCode("123");
        view.setOffice(new Office());
        
        assertEquals(view.getMatriculationCode(), "123");
        assertNotNull(view.getOffice());
        
        Object introduced2 = this.introductor.introduceInterfaces(target, new Class[]{EmployeeView.class}, new Class[]{IEmployee.class});
        
        assertFalse(introduced2 instanceof Employee);
        assertTrue(introduced2 instanceof  IEmployee);
        assertTrue(introduced2 instanceof EmployeeView);
    }
    
    /**
     * Test of getTraget method, of class org.springmodules.xt.model.introductor.bean.DynamicBeanIntroductor.
     */
    public void testGetTarget() {
        Employee target = new Employee();
        target.setFirstname("Sergio");
        target.setSurname("Bossa");
        
        Object introduced = this.introductor.introduceInterfaces(target, new Class[]{EmployeeView.class}, new Class[]{IEmployee.class});
        
        assertFalse(introduced instanceof Employee);
        assertTrue(introduced instanceof  IEmployee);
        assertTrue(introduced instanceof EmployeeView);
        
        Object source = this.introductor.getTarget(introduced);
        
        assertTrue(source instanceof Employee);
        assertTrue(source instanceof IEmployee);
        assertFalse(source instanceof EmployeeView);
    }
}
