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
public class ButtonTest extends XMLEnhancedTestCase {
    
    private Button button;
    
    public ButtonTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
        this.button = new Button("testName", "testValue", Button.ButtonType.BUTTON, new SimpleText("Click"));
    }

    protected void tearDown() throws Exception {
    }

    public void testAddAttribute() throws Exception {
        button.addAttribute("class", "testClass");
        
        System.out.println(this.button.render());
        
        String rendering = this.button.render();
        
        assertXpathEvaluatesTo("testClass", "/button/@class", rendering);
    }

    public void testRender() throws Exception {
        String rendering = this.button.render();
        
        System.out.println(this.button.render());
        
        assertXpathEvaluatesTo("testName", "/button/@name", rendering);
        assertXpathEvaluatesTo("testValue", "/button/@value", rendering);
        assertXpathEvaluatesTo("Click", "/button", rendering);
    }
    
}
