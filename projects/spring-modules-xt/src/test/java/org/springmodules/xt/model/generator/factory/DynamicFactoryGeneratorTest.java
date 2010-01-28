/*
 * Copyright 2006 - 2007 the original author or authors.
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

package org.springmodules.xt.model.generator.factory;

import junit.framework.*;
import org.springmodules.xt.test.domain.Employee;
import org.springmodules.xt.test.domain.EmployeeFactory;
import org.springmodules.xt.test.domain.IEmployee;

/**
 *
 * @author Sergio Bossa
 */
public class DynamicFactoryGeneratorTest extends TestCase {
    
    public DynamicFactoryGeneratorTest(String testName) {
        super(testName);
    }
    
    public void testSettersAndGetters() {
        DynamicFactoryGenerator<EmployeeFactory, Employee> generator = new DynamicFactoryGenerator(EmployeeFactory.class, Employee.class);
        EmployeeFactory factory = generator.generate();
        
        factory.setNickname("SB");
        factory.setMatriculationCode("111");
        factory.setFirstname("Sergio");
        factory.setSurname("Bossa");
        factory.setValue("value");
        
        assertEquals("SB", factory.getNickname());
        assertEquals("111", factory.getMatriculationCode());
        assertEquals("Sergio", factory.getFirstname());
        assertEquals("Bossa", factory.getSurname());
        assertEquals("value", factory.getValue());
    }

    public void testGenerate() {
        DynamicFactoryGenerator<EmployeeFactory, Employee> generator = new DynamicFactoryGenerator(EmployeeFactory.class, Employee.class);
        EmployeeFactory factory = generator.generate();
        
        factory.setNickname("SB");
        factory.setMatriculationCode("111");
        factory.setFirstname("Sergio");
        factory.setSurname("Bossa");
        
        IEmployee emp = factory.make();
        
        assertTrue(emp instanceof Employee);
        assertEquals("SB", emp.getNickname());
        assertEquals("111", emp.getMatriculationCode());
        assertEquals("Sergio", emp.getFirstname());
        assertEquals("Bossa", emp.getSurname());
        assertNull(emp.getPassword());
    }
    
    public void testGenerateFailsBecauseOfNoSuchConstructor() {
        DynamicFactoryGenerator<EmployeeFactory, Employee> generator = new DynamicFactoryGenerator(EmployeeFactory.class, Employee.class);
        EmployeeFactory factory = generator.generate();
        
        factory.setNickname("SB");
        factory.setFirstname("Sergio");
        factory.setSurname("Bossa");
        // We don't set matriculation code ...
        
        try {
            IEmployee emp = factory.make();
            fail("Construction should fail!");
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void testUnsupportedMethod() {
        DynamicFactoryGenerator<EmployeeFactory, Employee> generator = new DynamicFactoryGenerator(EmployeeFactory.class, Employee.class);
        EmployeeFactory factory = generator.generate();
        
        try {
            factory.setUnsupportedProperty("unsupported");
            fail("Unsupported method!");
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}
