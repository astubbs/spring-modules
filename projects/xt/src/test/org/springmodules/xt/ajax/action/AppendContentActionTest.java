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

import java.util.Arrays;
import org.springmodules.xt.ajax.AjaxAction;
import org.springmodules.xt.ajax.ElementMatcher;
import org.springmodules.xt.ajax.action.matcher.ListMatcher;
import org.springmodules.xt.ajax.action.matcher.SelectorMatcher;
import org.springmodules.xt.ajax.action.matcher.WildcardMatcher;
import org.springmodules.xt.ajax.component.SimpleText;
import org.springmodules.xt.test.xml.XMLEnhancedTestCase;

/**
 *
 * @author Sergio Bossa
 */
public class AppendContentActionTest extends XMLEnhancedTestCase {
    
    public AppendContentActionTest(String testName) {
        super(testName);
    }
    
    public void testRender() throws Exception {
        AjaxAction action = new AppendContentAction("testId", new SimpleText("Test Component"));
        
        String result = action.render();
        
        System.out.println(result);
        
        assertXpathEvaluatesTo("Test Component", "/append-as-children/content", result);
        assertXpathEvaluatesTo("testId", "/append-as-children/context/matcher/@contextNodeID", result);
    }
    
    public void testRenderWithWildcardMatcher() throws Exception {
        ElementMatcher matcher = new WildcardMatcher("testId");
        AjaxAction action = new AppendContentAction(matcher, new SimpleText("Test Component"));
        
        String result = action.render();
        
        System.out.println(result);
        
        assertXpathEvaluatesTo("Test Component", "/append-as-children/content", result);
        assertXpathEvaluatesTo("wildcard", "/append-as-children/context/matcher/@matchMode", result);
        assertXpathEvaluatesTo("testId", "/append-as-children/context/matcher/@contextNodeID", result);
    }
    
    public void testRenderWithListMatcher() throws Exception {
        ElementMatcher matcher = new ListMatcher(Arrays.asList("testId1", "testId2"));
        AjaxAction action = new AppendContentAction(matcher, new SimpleText("Test Component"));
        
        String result = action.render();
        
        System.out.println(result);
        
        assertXpathEvaluatesTo("Test Component", "/append-as-children/content", result);
        assertXpathEvaluatesTo("plain", "/append-as-children/context/matcher/@matchMode", result);
        assertXpathEvaluatesTo("testId1,testId2", "/append-as-children/context/matcher/@contextNodeID", result);
    }
    
    public void testRenderWithSelectorMatcher() throws Exception {
        ElementMatcher matcher = new SelectorMatcher(Arrays.asList("#testId1", "#testId2"));
        AjaxAction action = new AppendContentAction(matcher, new SimpleText("Test Component"));
        
        String result = action.render();
        
        System.out.println(result);
        
        assertXpathEvaluatesTo("Test Component", "/append-as-children/content", result);
        assertXpathEvaluatesTo("selector", "/append-as-children/context/matcher/@matchMode", result);
        assertXpathEvaluatesTo("#testId1,#testId2", "/append-as-children/context/matcher/@contextNodeSelector", result);
    }
}
