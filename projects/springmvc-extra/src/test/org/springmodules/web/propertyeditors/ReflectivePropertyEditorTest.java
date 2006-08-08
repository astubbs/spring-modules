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
package org.springmodules.web.propertyeditors;

import junit.framework.*;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;
import org.springmodules.web.test.domain.IEmployee;
import org.springmodules.web.test.domain.MemoryRepository;

/**
 *
 * @author Sergio Bossa
 */
public class ReflectivePropertyEditorTest extends AbstractDependencyInjectionSpringContextTests {
    
    private ReflectivePropertyEditor editor;
    private IEmployee employee;
    
    public ReflectivePropertyEditorTest(String testName) {
        super(testName);
    }

    protected void onSetUp() throws Exception {
        MemoryRepository repository = (MemoryRepository) this.applicationContext.getBean("store");
        this.editor = (ReflectivePropertyEditor) this.applicationContext.getBean("employeeEditor");
        this.employee = repository.getEmployee("1");
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(ReflectivePropertyEditorTest.class);
        
        return suite;
    }

    public void testGetAsText() {
        this.editor.setValue(this.employee);
        assertEquals(this.employee.getMatriculationCode(), this.editor.getAsText());
    }

    public void testSetAsText() {
        this.editor.setAsText("1");
        assertEquals(this.employee, this.editor.getValue());
    }
    
    protected String[] getConfigLocations() {
        return new String[]{"testApplicationContext.xml"};
    }
}
