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
public class OptionListTest extends XMLEnhancedTestCase {
    
    private OptionList optionList;
    
    public OptionListTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
        Employee e1 = new Employee();
        Employee e2 = new Employee();
        
        e1.setFirstname("Firstname1");
        e1.setSurname("Surname1");
        
        e2.setFirstname("Firstname2");
        e2.setSurname("Surname2");
        
        this.optionList = new OptionList(new Employee[]{e1, e2}, null, "firstname", "surname");
    }

    protected void tearDown() throws Exception {
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(OptionListTest.class);
        
        return suite;
    }

    /**
     * Test of addAttribute method, of class org.springmodules.xt.ajax.component.OptionList.
     */
    public void testAddAttribute() throws Exception {
        this.optionList.addAttribute("class", "testClass");
        
        System.out.println(this.optionList.render());
        
        String rendering = this.encloseIntoRoot(this.optionList.render());
       
        assertXpathEvaluatesTo("testClass", "/root/option[1]/@class", rendering);
        assertXpathEvaluatesTo("testClass", "/root/option[2]/@class", rendering);
    }

    /**
     * Test of setFirstTextOption method, of class org.springmodules.xt.ajax.component.OptionList.
     */
    public void testSetFirstTextOption() throws Exception {
        this.optionList.setFirstTextOption("Dummy", "Dummy ...");
        
        System.out.println(this.optionList.render());
        
        String rendering = this.encloseIntoRoot(this.optionList.render());
        
        assertXpathEvaluatesTo("Dummy", "/root/option[1]/@value", rendering);
        assertXpathEvaluatesTo("Dummy ...", "/root/option[1]", rendering);
    }

    /**
     * Test of render method, of class org.springmodules.xt.ajax.component.OptionList.
     */
    public void testRender() throws Exception {
        System.out.println(this.optionList.render());
        
        String rendering = this.encloseIntoRoot(this.optionList.render());
        
        assertXpathEvaluatesTo("Surname1", "/root/option[1]", rendering);
        assertXpathEvaluatesTo("Firstname1", "/root/option[1]/@value", rendering);
        assertXpathEvaluatesTo("Surname2", "/root/option[2]", rendering);
        assertXpathEvaluatesTo("Firstname2", "/root/option[2]/@value", rendering);
    }
}
