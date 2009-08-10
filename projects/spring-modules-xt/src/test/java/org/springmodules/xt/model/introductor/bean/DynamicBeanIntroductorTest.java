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

import org.jmock.integration.junit3.MockObjectTestCase;

/**
 *
 * @author Sergio Bossa
 */
public abstract class DynamicBeanIntroductorTest extends MockObjectTestCase {
    /*
    private DynamicBeanIntroductor introductor;
    
    public DynamicBeanIntroductorTest(String testName) {
        super(testName);
    }
    
    protected void setUp() throws Exception {
        this.introductor  = new DynamicBeanIntroductor();
    }
    
    public void testIntroduceInterfacesPart1() {
        Employee target = new Employee();
        target.setFirstname("Sergio");
        target.setSurname("Bossa");
        
        Object introduced = this.introductor.introduceInterfaces(target, new Class[]{EmployeeView.class});
        
        assertTrue(introduced instanceof Employee);
        assertTrue(introduced instanceof  IEmployee);
        assertTrue(introduced instanceof EmployeeView);
        
        EmployeeView view = (EmployeeView) introduced;
        
        assertEquals("Sergio", view.getFirstname());
        assertEquals("Bossa", view.getSurname());
        
        view.setMatriculationCode("123");
        view.setOffice(new Office());
        
        assertEquals("123", view.getMatriculationCode());
        assertNotNull(view.getOffice());
    }
    
    public void testIntroduceInterfacesPart2() {
        Employee target = new Employee();
        target.setFirstname("Sergio");
        target.setSurname("Bossa");
        
        Object introduced = this.introductor.introduceInterfaces(target, new Class[]{EmployeeView.class}, new Class[]{IEmployee.class});
        
        assertFalse(introduced instanceof Employee);
        assertTrue(introduced instanceof  IEmployee);
        assertTrue(introduced instanceof EmployeeView);
        
        EmployeeView view = (EmployeeView) introduced;
        
        assertEquals("Sergio", view.getFirstname());
        assertEquals("Bossa", view.getSurname());
        
        view.setMatriculationCode("123");
        view.setOffice(new Office());
        
        assertEquals("123", view.getMatriculationCode());
        assertNotNull(view.getOffice());
    }
    
    public void testIntroduceInterfacesWithInheritedInterface() {
        Employee target = new Employee();
        
        Object introduced = this.introductor.introduceInterfaces(target, new Class[]{EmployeeView2.class});
        
        assertTrue(introduced instanceof Employee);
        assertTrue(introduced instanceof IEmployee);
        assertTrue(introduced instanceof EmployeeView);
        assertTrue(introduced instanceof EmployeeView2);
        
        EmployeeView2 view = (EmployeeView2) introduced;
        // Call to setOffice() and getOffice(), belonging to the inherited EmployeeView interface:
        view.setOffice(new Office());
        assertNotNull(view.getOffice());
        
        Object introduced2 = this.introductor.introduceInterfaces(target, new Class[]{EmployeeView2.class}, new Class[]{IEmployee.class});
        
        assertFalse(introduced2 instanceof Employee);
        assertTrue(introduced2 instanceof IEmployee);
        assertTrue(introduced2 instanceof EmployeeView);
        assertTrue(introduced2 instanceof EmployeeView2);
        
        view = (EmployeeView2) introduced2;
        // Call to setOffice() and getOffice(), belonging to the inherited EmployeeView interface:
        view.setOffice(new Office());
        assertNotNull(view.getOffice());
    }
    
    public void testCallsDelegatedToTarget() {
        Mock targetMock = mock(Employee.class);
        
        Object introduced = this.introductor.introduceInterfaces(targetMock.proxy(), new Class[]{EmployeeView2.class});
        EmployeeView2 view = (EmployeeView2) introduced;
        // Expects call to target:
        targetMock.expects(once()).method("setNickname").with(eq("sb"));
        view.setNickname("sb");
        
        targetMock.reset();
        
        introduced = this.introductor.introduceInterfaces(targetMock.proxy(), new Class[]{EmployeeView2.class}, new Class[]{IEmployee.class});
        view = (EmployeeView2) introduced;
        // Expects call to target:
        targetMock.expects(once()).method("setNickname").with(eq("sb"));
        view.setNickname("sb");
    }
    
    public void testOverrideTarget() {
        Mock targetMock = mock(Employee.class);
        
        Object introduced = this.introductor.introduceInterfaces(targetMock.proxy(), new Class[]{EmployeeView3.class});
        EmployeeView3 view = (EmployeeView3) introduced;
        // Expects no call to target:
        targetMock.expects(never()).method("setNickname").with(eq("sb"));
        targetMock.expects(never()).method("getNickname");
        view.setNickname("sb");
        assertEquals("sb", view.getNickname());
        
        targetMock.reset();
        
        introduced = this.introductor.introduceInterfaces(targetMock.proxy(), new Class[]{EmployeeView3.class}, new Class[]{IEmployee.class});
        view = (EmployeeView3) introduced;
        // Expects no call to target:
        targetMock.expects(never()).method("setNickname").with(eq("sb"));
        targetMock.expects(never()).method("getNickname");
        view.setNickname("sb");
        assertEquals("sb", view.getNickname());
    }
    
    public void testMapToTargetField() {
        IEmployee employee = new Employee();
        Object introduced = this.introductor.introduceInterfaces(employee, new Class[]{EmployeeView4.class});
        EmployeeView4 view = (EmployeeView4) introduced;
        view.setNickname("sb");
        assertEquals("sb", view.getNickname());
        
        employee = new Employee();
        introduced = this.introductor.introduceInterfaces(employee, new Class[]{EmployeeView4.class}, new Class[]{IEmployee.class});
        view = (EmployeeView4) introduced;
        view.setNickname("sb");
        assertEquals("sb", view.getNickname());
    }
    
    public void testBooleanProperties() {
        Office office = new Office();
        OfficeView introducedOffice = (OfficeView) this.introductor.introduceInterfaces(office, new Class[]{OfficeView.class}, new Class[]{IOffice.class});
    
        introducedOffice.setSelected(true);
        assertTrue(introducedOffice.isSelected());
    }
    
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
    }*/
}
