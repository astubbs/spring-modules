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

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
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
public class IntroductorSetTest extends TestCase {
    
    private Set target;
    
    public IntroductorSetTest(String testName) {
        super(testName);
    }
    
    protected void setUp() throws Exception {
        target = new HashSet();
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
        IntroductorSet introductor = new IntroductorSet(target, new Class[]{EmployeeView.class}, new DynamicBeanIntroductor());
        
        this.internalTestAddAndRemove(introductor);
    }
    
    /**
     * Test of adding and removing a new object.
     */
    public void testAddAndRemoveObjectPartTwo() {
        IntroductorSet introductor = new IntroductorSet(target, new Class[]{EmployeeView.class}, new Class[]{IEmployee.class}, new DynamicBeanIntroductor());
        
        this.internalTestAddAndRemove(introductor);
    }
    
    /**
     * Test of iterating and removing objects.
     */
    public void testIterateAndRemovePartOne() {
        IntroductorSet introductor = new IntroductorSet(target, new Class[]{EmployeeView.class}, new DynamicBeanIntroductor());
        
        this.internalTestIterateAndRemove(introductor);
    }
    
    /**
     * Test of iterating and removing objects.
     */
    public void testIterateAndRemovePartTwo() {
        IntroductorSet introductor = new IntroductorSet(target, new Class[]{EmployeeView.class}, new Class[]{IEmployee.class}, new DynamicBeanIntroductor());
        
        this.internalTestIterateAndRemove(introductor);
    }
    
    /**
     * Test of modifying and iterating objects.
     */
    public void testModifyAndIteratePartOne() {
        IntroductorSet introductor = new IntroductorSet(target, new Class[]{EmployeeView.class}, new DynamicBeanIntroductor());
        
        this.internalTestModifyAndIterate(introductor);
    }
    
    /**
     * Test of modifying and iterating objects.
     */
    public void testModifyAndIteratePartTwo() {
        IntroductorSet introductor = new IntroductorSet(target, new Class[]{EmployeeView.class}, new Class[]{IEmployee.class}, new DynamicBeanIntroductor());
        
        this.internalTestModifyAndIterate(introductor);
    }
    
    private void internalTestAddAndRemove(IntroductorSet introductor) {
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
    
    private void internalTestIterateAndRemove(IntroductorSet introductor) {
        for (Iterator it = introductor.iterator(); it.hasNext();) {
            Object o = it.next();
            assertTrue(o instanceof  IEmployee);
            assertTrue(o instanceof EmployeeView);
            it.remove();
        }
        assertEquals(0, target.size());
        assertEquals(0, introductor.size());
    }
    
    private void internalTestModifyAndIterate(IntroductorSet introductor) {
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
