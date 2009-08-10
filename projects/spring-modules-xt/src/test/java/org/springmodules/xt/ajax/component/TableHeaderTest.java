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

import java.util.Arrays;
import java.util.LinkedList;
import junit.framework.*;
import org.springmodules.xt.test.xml.XMLEnhancedTestCase;

/**
 *
 * @author Sergio Bossa
 */
public class TableHeaderTest extends XMLEnhancedTestCase {
    
    public TableHeaderTest(String testName) {
        super(testName);
    }

    public void testAddRowAttribute() throws Exception {
        TableHeader tableHeader = new TableHeader(new String[]{"Header 1", "Header 2"});
        tableHeader.addRowAttribute("class", "testClass");
        
        System.out.println(tableHeader.render());
        String rendering = this.encloseIntoRoot(tableHeader.render());
        
        assertXpathEvaluatesTo("testClass", "//tr/@class", rendering);
    }

    public void testAddColumnAttribute() throws Exception {
        TableHeader tableHeader = new TableHeader(new String[]{"Header 1", "Header 2"});
        tableHeader.addColumnAttribute("class", "testClass");
        
        System.out.println(tableHeader.render());
        String rendering = this.encloseIntoRoot(tableHeader.render());
        
        assertXpathEvaluatesTo("testClass", "//tr/th[1]/@class", rendering);
        assertXpathEvaluatesTo("testClass", "//tr/th[2]/@class", rendering);
    }
    
    public void testAddTableHeaderData() throws Exception {
        TableHeader tableHeader = new TableHeader(new String[]{"Header 1", "Header 2"});
        TableHeaderData data = new TableHeaderData(new SimpleText("Header 3"));
        tableHeader.addTableHeaderData(data);
        
        System.out.println(tableHeader.render());
        String rendering = this.encloseIntoRoot(tableHeader.render());
        
        assertXpathEvaluatesTo("Header 1", "//tr/th[1]", rendering);
        assertXpathEvaluatesTo("Header 2", "//tr/th[2]", rendering);
        assertXpathEvaluatesTo("Header 3", "//tr/th[3]", rendering);
    }

    public void testRender1() throws Exception {
        TableHeader tableHeader = new TableHeader(new String[]{"Header 1", "Header 2"});
        
        System.out.println(tableHeader.render());
        String rendering = this.encloseIntoRoot(tableHeader.render());
        
        assertXpathEvaluatesTo("Header 1", "//tr/th[1]", rendering);
        assertXpathEvaluatesTo("Header 2", "//tr/th[2]", rendering);
    }   
    
    public void testRender2() throws Exception {
        TableHeader tableHeader = new TableHeader(
                new LinkedList<TableHeaderData>(Arrays.asList(new TableHeaderData(new SimpleText("Header 1")), new TableHeaderData(new SimpleText("Header 2")))));
        
        System.out.println(tableHeader.render());
        String rendering = this.encloseIntoRoot(tableHeader.render());
        
        assertXpathEvaluatesTo("Header 1", "//tr/th[1]", rendering);
        assertXpathEvaluatesTo("Header 2", "//tr/th[2]", rendering);
    }   
}
