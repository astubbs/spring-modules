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

package org.springmodules.xt.ajax.taconite;

import junit.framework.*;
import org.springmodules.xt.ajax.component.SimpleText;
import org.springmodules.xt.test.xml.XMLEnhancedTestCase;

/**
 *
 * @author Sergio Bossa
 */
public class TaconiteResponseTest extends XMLEnhancedTestCase {
    
    public TaconiteResponseTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(TaconiteResponseTest.class);
        
        return suite;
    }

    /**
     * Test of getResponse method, of class org.springmodules.xt.ajax.taconite.TaconiteResponse.
     */
    public void testGetResponse() throws Exception {
        TaconiteAppendContentAction action1 = new TaconiteAppendContentAction("action1", new SimpleText("Test Text"));
        TaconiteReplaceContentAction action2 = new TaconiteReplaceContentAction("action2", new SimpleText("Test Text"));
        TaconiteResponse response = new TaconiteResponse();
        
        response.addAction(action1);
        response.addAction(action2);
        
        String result = response.getResponse();
        
        System.out.println(result);
        
        assertXpathEvaluatesTo("action1", "/taconite-root/taconite-append-as-children/@contextNodeID", result);
        assertXpathEvaluatesTo("action2", "/taconite-root/taconite-replace-children/@contextNodeID", result);
    }
}
