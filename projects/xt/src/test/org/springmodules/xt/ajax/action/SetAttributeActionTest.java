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

package org.springmodules.xt.ajax.action;

import junit.framework.*;
import org.springmodules.xt.ajax.AjaxActionImpl;
import org.springmodules.xt.test.xml.XMLEnhancedTestCase;

/**
 *
 * @author Sergio Bossa
 */
public class SetAttributeActionTest extends XMLEnhancedTestCase {
    
    public SetAttributeActionTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(SetAttributeActionTest.class);
        
        return suite;
    }

    /**
     * Test of execute method, of class org.springmodules.xt.ajax.taconite.SetAttributeAction.
     */
    public void testExecute() throws Exception {
        AjaxActionImpl action = new SetAttributeAction("testId", "class", "testClass");
        
        String result = action.execute();
        
        System.out.println(result);
        
        assertXpathEvaluatesTo("testId", "/taconite-set-attributes/@contextNodeID", result);
        assertXpathEvaluatesTo("testClass", "/taconite-set-attributes/@class", result);
    }
}
