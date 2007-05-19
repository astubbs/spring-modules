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

package org.springmodules.xt.model.introductor.collections;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.springmodules.xt.test.domain.Employee;
import org.springmodules.xt.test.domain.IEmployee;
import org.springmodules.xt.test.domain.Office;
import org.springmodules.xt.test.domain.EmployeeView;
import org.springmodules.xt.model.introductor.bean.DynamicBeanIntroductor;

/**
 *
 * @author Sergio Bossa
 */
public class IntroductorListTest extends TestCase {
    
    private List target;
    
    public IntroductorListTest(String testName) {
        super(testName);
    }
    
    protected void setUp() throws Exception {
        target = new LinkedList();
        Employee emp1 = new Employee();
        Employee emp2 = new Employee();
        
        emp1.setMatriculationCode("1");
        emp2.setMatriculationCode("2");
        
        target.add(emp1);
        target.add(emp2);
    }
    
    protected void tearDown() throws Exception {
    }
    
    /**
     * Test of adding and removing a new object.
     */
    public void testAddAndRemoveObjectPartOne() {
        IntroductorList introductor = new IntroductorList(target, new Class[]{EmployeeView.class}, new DynamicBeanIntroductor());
        
        this.internalTestAddAndRemove(introductor);
    }
    
    /**
     * Test of adding and removing a new object.
     */
    public void testAddAndRemoveObjectPartTwo() {
        IntroductorList introductor = new IntroductorList(target, new Class[]{EmployeeView.class}, new Class[]{IEmployee.class}, new DynamicBeanIntroductor());
        
        this.internalTestAddAndRemove(introductor);
    }
    
    /**
     * Test of iterating and removing objects.
     */
    public void testIterateAndRemovePartOne() {
        IntroductorList introductor = new IntroductorList(target, new Class[]{EmployeeView.class}, new DynamicBeanIntroductor());
        
        this.internalTestIterateAndRemove(introductor);
    }
    
    /**
     * Test of iterating and removing objects.
     */
    public void testIterateAndRemovePartTwo() {
        IntroductorList introductor = introductor = new IntroductorList(target, new Class[]{EmployeeView.class}, new Class[]{IEmployee.class}, new DynamicBeanIntroductor());
        
        this.internalTestIterateAndRemove(introductor);
    }
    
    /**
     * Test of adding, removing and getting objects.
     */
    public void testAddRemoveAndGetPartOne() {
        IntroductorList introductor = new IntroductorList(target, new Class[]{EmployeeView.class}, new DynamicBeanIntroductor());
        
        this.internalTestAddRemoveAndGet(introductor);
    }
    
    /**
     * Test of adding, removing and getting objects.
     */
    public void testAddRemoveAndGetPartTwo() {
        IntroductorList introductor = introductor = new IntroductorList(target, new Class[]{EmployeeView.class}, new Class[]{IEmployee.class}, new DynamicBeanIntroductor());
        
        this.internalTestAddRemoveAndGet(introductor);
    }
    
    /**
     * Test of modifying and iterating objects.
     */
    public void testModifyAndIteratePartOne() {
        IntroductorCollection introductor = introductor = new IntroductorCollection(target, new Class[]{EmployeeView.class}, new DynamicBeanIntroductor());
        
        this.internalTestModifyAndIterate(introductor);
    }
    
    /**
     * Test of modifying and iterating objects.
     */
    public void testModifyAndIteratePartTwo() {
        IntroductorCollection introductor = new IntroductorCollection(target, new Class[]{EmployeeView.class}, new Class[]{IEmployee.class}, new DynamicBeanIntroductor());
        
        this.internalTestModifyAndIterate(introductor);
    }
    
    private void internalTestAddAndRemove(IntroductorList introductor) {
        assertEquals(2, target.size());
        assertEquals(2, introductor.size());
        
        Employee emp3 = new Employee();
        emp3.setMatriculationCode("3");
        
        // Add and remove from introductor ....
        
        introductor.add(emp3);
        
        assertEquals(3, target.size());
        assertEquals(3, introductor.size());
        assertTrue(target.contains(emp3));
        assertTrue(introductor.contains(emp3));
        
        introductor.remove(emp3);
        
        assertEquals(2, target.size());
        assertEquals(2, introductor.size());
        
        // Add from introductor and remove from target ....
        
        introductor.add(emp3);
        
        assertEquals(3, target.size());
        assertEquals(3, introductor.size());
        assertTrue(target.contains(emp3));
        assertTrue(introductor.contains(emp3));
        
        target.remove(emp3);
        
        assertEquals(2, target.size());
        assertEquals(2, introductor.size());
    }
    
    private void internalTestIterateAndRemove(IntroductorList introductor) {
        int index = 0;
        for (ListIterator it = introductor.listIterator(); it.hasNext();) {
            assertEquals(it.nextIndex(), index++);
            Object o = it.next();
            assertTrue(o instanceof  IEmployee);
            assertTrue(o instanceof EmployeeView);
        }
        
        index = 1;
        for (ListIterator it = introductor.listIterator(2); it.hasPrevious();) {
            assertEquals(it.previousIndex(), index--);
            Object o = it.previous();
            assertTrue(o instanceof  IEmployee);
            assertTrue(o instanceof EmployeeView);
        }
        
        for (ListIterator it = introductor.listIterator(); it.hasNext();) {
            it.next();
            it.remove();
        }
        
        assertEquals(0, target.size());
        assertEquals(0, introductor.size());
    }
    
    private void internalTestAddRemoveAndGet(IntroductorList introductor) {
        // Add a new employee:
        
        Employee emp3 = new Employee();
        emp3.setMatriculationCode("3");
        
        introductor.add(emp3);
        
        assertEquals(3, introductor.size());
        // Test that the new employee is equal to the added employee using matriculation codes ...
        assertTrue(emp3.getMatriculationCode().equals(((EmployeeView) introductor.get(2)).getMatriculationCode()));
        // Because testing using equals() would fail, due to how Spring AOP works when intercepting the equals() method:
        assertFalse(introductor.get(2).equals(emp3));
        // However, PLEASE NOTE that the two objects are actually equals!
        
        // Remove the employee previously added:
        
        introductor.remove(2);
        
        assertEquals(introductor.size(), 2);
        
        // Add again a new employee:
        
        Employee emp33 = new Employee();
        emp33.setMatriculationCode("33");
        
        introductor.add(emp33);
        
        assertEquals(3, introductor.size());
        // Test that the new employee is equal to the added employee using matriculation codes ...
        assertTrue(emp33.getMatriculationCode().equals(((EmployeeView) introductor.get(2)).getMatriculationCode()));
        // Because testing using equals() would fail, due to how Spring AOP works when intercepting the equals() method:
        assertFalse(introductor.get(2).equals(emp3));
        // However, PLEASE NOTE that the two objects are actually equals!
    }
    
    private void internalTestModifyAndIterate(IntroductorCollection introductor) {
        int n = 0;
        for (Iterator it = introductor.iterator(); it.hasNext();) {
            EmployeeView v = (EmployeeView) it.next();
            Office o = new Office();
            o.setOfficeId("" + ++n);
            v.setOffice(o);
        }
        
        n = 0;
        for (Iterator it = introductor.iterator(); it.hasNext();) {
            EmployeeView v = (EmployeeView) it.next();
            assertEquals("" + ++n, v.getOffice().getOfficeId());
        }
    }
}
