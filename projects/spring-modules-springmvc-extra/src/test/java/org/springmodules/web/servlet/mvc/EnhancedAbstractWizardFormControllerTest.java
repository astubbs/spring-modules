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
package org.springmodules.web.servlet.mvc;

import junit.framework.*;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springmodules.web.test.domain.Employee;
import org.springmodules.web.test.domain.IEmployee;
import org.springmodules.web.test.domain.Office;

/**
 *
 * @author Sergio Bossa
 */
public class EnhancedAbstractWizardFormControllerTest extends AbstractDependencyInjectionSpringContextTests {
    
    public EnhancedAbstractWizardFormControllerTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(EnhancedSimpleFormControllerTest.class);
        
        return suite;
    }

    public void testSetCustomEditorsWithDefaultPrefix() 
    throws Exception {
        IEmployee emp = new Employee();
        EnhancedAbstractWizardFormController controller = (EnhancedAbstractWizardFormController) this.applicationContext.getBean("testCustomEditorsWizardControllerOne");
        ServletRequestDataBinder binder = new ServletRequestDataBinder(emp);
        
        controller.initBinder(new MockHttpServletRequest(), binder);
        
        assertNotNull(binder.findCustomEditor(Office.class, null));
    }
    
    public void testSetCustomEditorsWithClassPrefix() 
    throws Exception {
        IEmployee emp = new Employee();
        EnhancedAbstractWizardFormController controller = (EnhancedAbstractWizardFormController) this.applicationContext.getBean("testCustomEditorsWizardControllerTwo");
        ServletRequestDataBinder binder = new ServletRequestDataBinder(emp);
        
        controller.initBinder(new MockHttpServletRequest(), binder);
        
        assertNotNull(binder.findCustomEditor(Office.class, null));
    }
    
    public void testSetCustomEditorsWithPropertyPrefix() 
    throws Exception {
        IEmployee emp = new Employee();
        EnhancedAbstractWizardFormController controller = (EnhancedAbstractWizardFormController) this.applicationContext.getBean("testCustomEditorsWizardControllerThree");
        ServletRequestDataBinder binder = new ServletRequestDataBinder(emp);
        
        controller.initBinder(new MockHttpServletRequest(), binder);
        
        assertNotNull(binder.findCustomEditor(null, "office"));
    }
    
    public void testSetCustomEditorsWithFailure() 
    throws Exception {
        IEmployee emp = new Employee();
        EnhancedAbstractWizardFormController controller = (EnhancedAbstractWizardFormController) this.applicationContext.getBean("testCustomEditorsWizardControllerFour");
        ServletRequestDataBinder binder = new ServletRequestDataBinder(emp);
        
        try {
            controller.initBinder(new MockHttpServletRequest(), binder);
            this.fail("initBinder should raise an exception!");
        }
        catch(Exception ex) {}
    }


    protected String[] getConfigLocations() {
        return new String[]{"testApplicationContext.xml"};
    }
}
