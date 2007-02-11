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
package org.springmodules.xt.ajax.support;

import junit.framework.*;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springmodules.xt.ajax.AjaxResponse;
import org.springmodules.xt.test.xml.XMLEnhancedTestCase;

/**
 *
 * @author Sergio Bossa
 */
public class RedirectExceptionResolverTest extends XMLEnhancedTestCase {
    
    public RedirectExceptionResolverTest(String testName) {
        super(testName);
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(RedirectExceptionResolverTest.class);
        
        return suite;
    }

    public void testResolve() throws Exception {
        MockHttpServletRequest httpRequest = new MockHttpServletRequest("GET", "/ajax/exception.action");
        Exception ex = new Exception("exception");
        
        RedirectExceptionResolver resolver = new RedirectExceptionResolver();
        resolver.setRedirectUrl("/ajax/exception.action");
        
        AjaxResponse response = resolver.resolve(httpRequest, ex);
        assertNotNull(response);
        assertXpathExists("//taconite-redirect/@targetUrl", response.getResponse());
    }
}
