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

package org.springmodules.xt.ajax.component;

import junit.framework.*;
import org.springmodules.xt.test.domain.Employee;
import org.springmodules.xt.test.xml.XMLEnhancedTestCase;

/**
 *
 * @author Sergio Bossa
 */
@Deprecated
public class RowListTest extends XMLEnhancedTestCase {
    
    private RowList rowList;
    
    public RowListTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
        Employee e1 = new Employee();
        Employee e2 = new Employee();
        
        e1.setFirstname("Firstname1");
        e1.setSurname("Surname1");
        
        e2.setFirstname("Firstname2");
        e2.setSurname("Surname2");
        
        this.rowList = new RowList(new Employee[]{e1, e2}, new String[]{"firstname", "surname"});
    }

    protected void tearDown() throws Exception {
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(RowListTest.class);
        
        return suite;
    }

    /**
     * Test of addRowAttribute method, of class org.springmodules.xt.ajax.component.RowList.
     */
    public void testAddRowAttribute() throws Exception {
        this.rowList.addRowAttribute("class", "testClass");
        
        System.out.println(this.rowList.render());
        
        String rendering = this.encloseIntoRoot(this.rowList.render());
        
        assertXpathEvaluatesTo("testClass", "/root/tr[1]/@class", rendering);
        assertXpathEvaluatesTo("testClass", "/root/tr[2]/@class", rendering);
    }

    /**
     * Test of addColumnAttribute method, of class org.springmodules.xt.ajax.component.RowList.
     */
    public void testAddColumnAttribute() throws Exception {
        this.rowList.addColumnAttribute("class", "testClass");
        
        System.out.println(this.rowList.render());
        
        String rendering = this.encloseIntoRoot(this.rowList.render());
        
        assertXpathEvaluatesTo("testClass", "/root/tr[1]/td[1]/@class", rendering);
        assertXpathEvaluatesTo("testClass", "/root/tr[1]/td[2]/@class", rendering);
        assertXpathEvaluatesTo("testClass", "/root/tr[2]/td[1]/@class", rendering);
        assertXpathEvaluatesTo("testClass", "/root/tr[2]/td[2]/@class", rendering);
    }

    /**
     * Test of appendComponentsToRow method, of class org.springmodules.xt.ajax.component.RowList.
     */
    public void testAppendComponentsToRow() throws Exception {
        this.rowList.appendComponentsToRow(new Component[]{new SimpleText("Column 1")}, 0);
        this.rowList.appendComponentsToRow(new Component[]{new SimpleText("Column 2")}, 1);
        
        System.out.println(this.rowList.render());
        
        String rendering = this.encloseIntoRoot(this.rowList.render());
        
        assertXpathEvaluatesTo("Column 1", "/root/tr[1]/td[3]", rendering);
        assertXpathEvaluatesTo("Column 2", "/root/tr[2]/td[3]", rendering);
    }

    /**
     * Test of render method, of class org.springmodules.xt.ajax.component.RowList.
     */
    public void testRender() throws Exception {
        System.out.println(this.rowList.render());
        
        String rendering = this.encloseIntoRoot(this.rowList.render());
        
        assertXpathEvaluatesTo("Firstname1", "/root/tr[1]/td[1]", rendering);
        assertXpathEvaluatesTo("Surname1", "/root/tr[1]/td[2]", rendering);
        assertXpathEvaluatesTo("Firstname2", "/root/tr[2]/td[1]", rendering);
        assertXpathEvaluatesTo("Surname2", "/root/tr[2]/td[2]", rendering);
    }
}
