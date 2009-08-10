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

package org.springmodules.xt.model.introductor.implementor;

import org.jmock.Expectations;
import org.jmock.integration.junit3.MockObjectTestCase;
import org.springmodules.xt.test.domain.IEmployee;
import org.springmodules.xt.test.domain.IManager;
import org.springmodules.xt.test.domain.IOffice;

/**
 *
 * @author Sergio Bossa
 */
public class DynamicImplementorIntroductorTest extends MockObjectTestCase {
    
    public DynamicImplementorIntroductorTest(String testName) {
        super(testName);
    }
    
    public void testIntroduceInterfacesPart1() {
        IEmployee implementor = mock(IEmployee.class);
        IManager target = mock(IManager.class);
        DynamicImplementorIntroductor introductor  = new DynamicImplementorIntroductor(implementor);
        Object introduced = introductor.introduceInterfaces(target, new Class[]{IEmployee.class});
        
        assertTrue(introduced instanceof  IEmployee);
        assertTrue(introduced instanceof IManager);
        
        final IManager manager = (IManager) introduced;
        manager.getManagedEmployees();
        super.checking(new Expectations(){{
        	oneOf(manager).getManagedEmployees();
        }});
        
        final IEmployee employee = (IEmployee) introduced;
        super.checking(new Expectations(){{
        	oneOf(employee).getFirstname();
        }});
        employee.getFirstname();
        super.verify();
    }
    /*
    public void testIntroduceInterfacesPart2() {
        Mock implementor = mock(IEmployee.class);
        Mock target = mock(IManager.class);
        DynamicImplementorIntroductor introductor  = new DynamicImplementorIntroductor(implementor.proxy());
        Object introduced = introductor.introduceInterfaces(target.proxy(), new Class[]{IEmployee.class}, new Class[]{IManager.class});
        
        assertTrue(introduced instanceof  IEmployee);
        assertTrue(introduced instanceof IManager);
        
        IManager manager = (IManager) introduced;
        target.expects(once()).method("getManagedEmployees");
        manager.getManagedEmployees();
        
        IEmployee employee = (IEmployee) introduced;
        implementor.expects(once()).method("getFirstname");
        employee.getFirstname();
    }
    
    public void testAlwaysOverrideTarget() {
        Mock implementor = mock(IEmployee.class);
        Mock target = mock(IManager.class);
        DynamicImplementorIntroductor introductor  = new DynamicImplementorIntroductor(implementor.proxy());
        Object introduced = introductor.introduceInterfaces(target.proxy(), new Class[]{IEmployee.class});
        
        assertTrue(introduced instanceof  IEmployee);
        assertTrue(introduced instanceof IManager);
        
        target.expects(once()).method("getMatriculationCode");
        ((IManager) target.proxy()).getMatriculationCode();
        
        IEmployee employee = (IEmployee) introduced;
        implementor.expects(once()).method("getMatriculationCode");
        employee.getMatriculationCode();
    }
    
    public void testIntroduceNotImplementedInterface() {
        Mock implementor = mock(IEmployee.class);
        Mock target = mock(IManager.class);
        DynamicImplementorIntroductor introductor  = new DynamicImplementorIntroductor(implementor.proxy());
        try {
            Object introduced = introductor.introduceInterfaces(target.proxy(), new Class[]{IOffice.class});
            fail();
        } catch(Exception ex) {
        }
    }*/
}
