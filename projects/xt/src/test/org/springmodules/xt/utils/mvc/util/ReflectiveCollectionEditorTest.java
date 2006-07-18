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
package org.springmodules.xt.utils.mvc.util;

import java.util.LinkedList;
import java.util.List;
import junit.framework.*;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;
import org.springmodules.xt.test.domain.IEmployee;
import org.springmodules.xt.test.domain.MemoryRepository;

/**
 *
 * @author Sergio Bossa
 */
public class ReflectiveCollectionEditorTest extends AbstractDependencyInjectionSpringContextTests {
    
    private MemoryRepository repository;
    private ReflectiveCollectionEditor editor;
    
    public ReflectiveCollectionEditorTest(String testName) {
        super(testName);
    }

    protected void onSetUp() throws Exception {
        this.repository = (MemoryRepository) this.applicationContext.getBean("store");
        this.editor = (ReflectiveCollectionEditor) this.applicationContext.getBean("employeesCollectionEditor");
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(ReflectiveCollectionEditorTest.class);
        
        return suite;
    }

    /**
     * 
     */
    public void testConversionToObject() {
        List<String> strings = new LinkedList<String>();
        
        strings.add("1");
        strings.add("2");
        
        this.editor.setValue(strings);
        
        assertTrue(this.editor.getValue() instanceof List);
        
        List emps = (List) this.editor.getValue();
        
        assertEquals(emps.get(0), this.repository.getEmployee("1"));
        assertEquals(emps.get(1), this.repository.getEmployee("2"));
    }
    
    /**
     * 
     */
    public void testConversionToString() {
        List<IEmployee> emps = new LinkedList<IEmployee>();
        
        emps.add(this.repository.getEmployee("1"));
        emps.add(this.repository.getEmployee("2"));
        
        this.editor.setValue(emps);
        
        assertTrue(this.editor.getValue() instanceof List);
        
        List strings = (List) this.editor.getValue();
        
        assertEquals(strings.get(0), this.repository.getEmployee("1").getMatriculationCode());
        assertEquals(strings.get(1), this.repository.getEmployee("2").getMatriculationCode());
    }
    
    protected String[] getConfigLocations() {
        return new String[]{"testApplicationContext.xml"};
    }
}
