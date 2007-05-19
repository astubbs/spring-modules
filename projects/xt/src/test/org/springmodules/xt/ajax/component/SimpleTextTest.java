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

/**
 *
 * @author Sergio Bossa
 */
public class SimpleTextTest extends TestCase {
    
    public SimpleTextTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    /**
     * Test of render method, of class org.springmodules.xt.ajax.component.SimpleText.
     */
    public void testRender() {
        SimpleText text = new SimpleText("Test Text");
        
        System.out.println(text.render());
        
        assertEquals("Test Text", text.render());
    } 
}
