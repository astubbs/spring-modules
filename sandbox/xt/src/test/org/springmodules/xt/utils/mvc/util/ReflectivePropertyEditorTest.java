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

import junit.framework.*;
import org.springmodules.xt.test.domain.IEmployee;
import org.springmodules.xt.test.domain.MemoryRepository;
import org.springmodules.xt.test.domain.MemoryRepositoryLoaderImpl;

/**
 *
 * @author Sergio Bossa
 */
public class ReflectivePropertyEditorTest extends TestCase {
    
    private ReflectivePropertyEditor editor;
    private IEmployee employee;
    private MemoryRepository repository;
    
    public ReflectivePropertyEditorTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
        this.repository = new MemoryRepository(new MemoryRepositoryLoaderImpl());
        this.editor = new ReflectivePropertyEditor();
        
        this.editor.setDataAccessObject(this.repository);
        this.editor.setDataAccessMethod("getEmployee");
        this.editor.setPropertyName("matriculationCode");
        
        this.employee = this.repository.getEmployee("1");
    }

    protected void tearDown() throws Exception {
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(ReflectivePropertyEditorTest.class);
        
        return suite;
    }

    /**
     * Test of getAsText method, of class org.springmodules.xt.utils.mvc.util.ReflectivePropertyEditor.
     */
    public void testGetAsText() {
        this.editor.setValue(this.employee);
        assertEquals(this.employee.getMatriculationCode(), this.editor.getAsText());
    }

    /**
     * Test of setAsText method, of class org.springmodules.xt.utils.mvc.util.ReflectivePropertyEditor.
     */
    public void testSetAsText() {
        this.editor.setAsText("1");
        assertEquals(this.employee, this.editor.getValue());
    }
}
