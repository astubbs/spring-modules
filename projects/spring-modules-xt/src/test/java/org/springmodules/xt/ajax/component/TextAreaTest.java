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

import org.springmodules.xt.test.xml.XMLEnhancedTestCase;

/**
 * @author Peter Bona
 * @author Sergio Bossa
 */
public class TextAreaTest extends XMLEnhancedTestCase {

	public TextAreaTest(String testName) {
        super(testName);
    }

    public void testAddAttribute() throws Exception {
        TextArea textArea = new TextArea(4, 5);
        textArea.addAttribute("id", "test-id");
        
        String rendering = textArea.render();
        System.out.println(rendering);
        
        assertXpathEvaluatesTo("test-id", "/textarea/@id", rendering);
    }
    
    public void testRender() throws Exception {
    	TextArea textArea = new TextArea(4, 5, "Test");
        
        String rendering = textArea.render();
        System.out.println(rendering);
        
        assertXpathEvaluatesTo("4", "/textarea/@rows", rendering);
        assertXpathEvaluatesTo("5", "/textarea/@cols", rendering);
        assertXpathEvaluatesTo("Test", "/textarea", rendering);
    }
}
