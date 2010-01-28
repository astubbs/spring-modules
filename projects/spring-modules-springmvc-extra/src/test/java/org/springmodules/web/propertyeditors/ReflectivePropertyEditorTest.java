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

import java.util.List;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;
import org.springmodules.web.test.domain.IEmployee;
import org.springmodules.web.test.domain.IOffice;
import org.springmodules.web.test.domain.MemoryRepository;

/**
 *
 * @author Sergio Bossa
 */
public class ReflectivePropertyEditorTest extends AbstractDependencyInjectionSpringContextTests {
    
    private MemoryRepository repository;
    
    public ReflectivePropertyEditorTest(String testName) {
        super(testName);
    }

    public void testGetAsText() {
        ReflectivePropertyEditor editor = (ReflectivePropertyEditor) this.applicationContext.getBean("employeeEditor");
        IEmployee employee = this.repository.getEmployee("1");
        editor.setValue(employee);
        assertEquals(employee.getMatriculationCode(), editor.getAsText());
    }

    public void testSetAsText() {
        ReflectivePropertyEditor editor = (ReflectivePropertyEditor) this.applicationContext.getBean("employeeEditor");
        IEmployee employee = this.repository.getEmployee("1");
        editor.setAsText("1");
        assertEquals(employee, editor.getValue());
    }
    
    public void testSetAsTextWithStringConvertor() {
        ReflectivePropertyEditor editor = (ReflectivePropertyEditor) this.applicationContext.getBean("employeesByOfficeEditor");
        IEmployee employee = this.repository.getEmployee("1");
        editor.setAsText("1");
        assertNotNull(editor.getValue());
        assertEquals(2, ((List) editor.getValue()).size());
    }
    
    protected String[] getConfigLocations() {
        return new String[]{"testApplicationContext.xml"};
    }

    public void setRepository(MemoryRepository repository) {
        this.repository = repository;
    }
}
