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

package org.springmodules.xt.ajax;

import org.springmodules.xt.ajax.action.AppendContentAction;
import org.springmodules.xt.ajax.action.ReplaceContentAction;
import org.springmodules.xt.ajax.component.SimpleText;
import org.springmodules.xt.test.xml.XMLEnhancedTestCase;

/**
 *
 * @author Sergio Bossa
 */
public class AjaxResponseImplTest extends XMLEnhancedTestCase {
    
    public AjaxResponseImplTest(String testName) {
        super(testName);
    }

    public void testRender() throws Exception {
        AppendContentAction action1 = new AppendContentAction("action1", new SimpleText("Test Text"));
        ReplaceContentAction action2 = new ReplaceContentAction("action2", new SimpleText("Test Text"));
        AjaxResponseImpl response = new AjaxResponseImpl();
        
        response.addAction(action1);
        response.addAction(action2);
        
        String result = response.render();
        
        System.out.println(result);
        
        assertXpathEvaluatesTo("action1", "/ajax-response/append-as-children/context/matcher/@contextNodeID", result);
        assertXpathEvaluatesTo("action2", "/ajax-response/replace-children/context/matcher/@contextNodeID", result);
    }
}
