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

import java.util.HashMap;
import java.util.Map;
import org.springmodules.xt.ajax.AjaxAction;
import org.springmodules.xt.test.xml.XMLEnhancedTestCase;

/**
 *
 * @author Sergio Bossa
 */
public class RedirectActionTest extends XMLEnhancedTestCase {
    
    public RedirectActionTest(String testName) {
        super(testName);
    }
    
    public void testRender() throws Exception {
        Map model = new HashMap();
        
        model.put("ex", "ex");
        
        AjaxAction action = new RedirectAction("/ajax/redirect.action", model);
        
        String result = action.render();
        
        System.out.println(result);
        
        assertXpathEvaluatesTo("/ajax/redirect.action?ex=ex", "/redirect/content/target/@url", result);
    }
}
