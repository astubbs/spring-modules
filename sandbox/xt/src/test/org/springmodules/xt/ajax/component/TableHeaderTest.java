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
import org.springmodules.xt.test.xml.XMLEnhancedTestCase;

/**
 *
 * @author Sergio Bossa
 */
public class TableHeaderTest extends XMLEnhancedTestCase {
    
    private TableHeader tableHeader;
    
    public TableHeaderTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
        this.tableHeader = new TableHeader(new String[]{"Header 1", "Header 2"});
    }

    protected void tearDown() throws Exception {
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(TableHeaderTest.class);
        
        return suite;
    }

    /**
     * Test of addRowAttribute method, of class org.springmodules.xt.ajax.component.TableHeader.
     */
    public void testAddRowAttribute() throws Exception {
        this.tableHeader.addRowAttribute("class", "testClass");
        
        System.out.println(this.tableHeader.render());
        
        String rendering = this.encloseIntoRoot(this.tableHeader.render());
        
        assertXpathEvaluatesTo("testClass", "/root/tr/@class", rendering);
    }

    /**
     * Test of addColumnAttribute method, of class org.springmodules.xt.ajax.component.TableHeader.
     */
    public void testAddColumnAttribute() throws Exception {
        this.tableHeader.addColumnAttribute("class", "testClass");
        
        System.out.println(this.tableHeader.render());
        
        String rendering = this.encloseIntoRoot(this.tableHeader.render());
        
        assertXpathEvaluatesTo("testClass", "/root/tr/th[1]/@class", rendering);
        assertXpathEvaluatesTo("testClass", "/root/tr/th[2]/@class", rendering);
    }

    /**
     * Test of render method, of class org.springmodules.xt.ajax.component.TableHeader.
     */
    public void testRender() throws Exception {
        System.out.println(this.tableHeader.render());
        
        String rendering = this.encloseIntoRoot(this.tableHeader.render());
        
        assertXpathEvaluatesTo("Header 1", "/root/tr/th[1]", rendering);
        assertXpathEvaluatesTo("Header 2", "/root/tr/th[2]", rendering);
    }   
}
