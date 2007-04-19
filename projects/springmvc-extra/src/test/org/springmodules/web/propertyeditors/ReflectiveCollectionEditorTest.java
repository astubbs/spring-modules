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

import java.util.LinkedList;
import java.util.List;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;
import org.springmodules.web.test.domain.MemoryRepository;

/**
 *
 * @author Sergio Bossa
 */
public class ReflectiveCollectionEditorTest extends AbstractDependencyInjectionSpringContextTests {
    
    private MemoryRepository repository;
    
    public ReflectiveCollectionEditorTest(String testName) {
        super(testName);
    }

    public void testConversionToObject() {
        ReflectiveCollectionEditor editor = (ReflectiveCollectionEditor) this.applicationContext.getBean("employeesCollectionEditor");
        
        List strings = new LinkedList();
        strings.add("1");
        strings.add("2");
        
        editor.setValue(strings);
        assertTrue(editor.getValue() instanceof List);
        List emps = (List) editor.getValue();
        assertEquals(emps.get(0), this.repository.getEmployee("1"));
        assertEquals(emps.get(1), this.repository.getEmployee("2"));
    }
    
    public void testConversionToObjectWithStringConvertor() {
        ReflectiveCollectionEditor editor = (ReflectiveCollectionEditor) this.applicationContext.getBean("employeesByOfficeCollectionEditor");
        
        List strings = new LinkedList();
        strings.add("1");
        strings.add("2");
        
        editor.setValue(strings);
        assertTrue(editor.getValue() instanceof List);
        List allEmps = (List) editor.getValue();
        assertEquals(2, allEmps.size());
        List emps1 = (List) allEmps.get(0);
        List emps2 = (List) allEmps.get(1);
        assertEquals(2, emps1.size());
        assertEquals(2, emps2.size());
    }
    
    public void testConversionToString() {
        ReflectiveCollectionEditor editor = (ReflectiveCollectionEditor) this.applicationContext.getBean("employeesCollectionEditor");
        
        List emps = new LinkedList();
        emps.add(this.repository.getEmployee("1"));
        emps.add(this.repository.getEmployee("2"));
        
        editor.setValue(emps);
        assertTrue(editor.getValue() instanceof List);
        List strings = (List) editor.getValue();
        assertEquals(strings.get(0), this.repository.getEmployee("1").getMatriculationCode());
        assertEquals(strings.get(1), this.repository.getEmployee("2").getMatriculationCode());
    }
    
    protected String[] getConfigLocations() {
        return new String[]{"testApplicationContext.xml"};
    }

    public void setRepository(MemoryRepository repository) {
        this.repository = repository;
    }
}
