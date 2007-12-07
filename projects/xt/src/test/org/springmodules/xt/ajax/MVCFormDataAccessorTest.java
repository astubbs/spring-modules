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

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import junit.framework.TestCase;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.BaseCommandController;

/**
 * @author Sergio Bossa
 */
public class MVCFormDataAccessorTest extends TestCase {
    
    private MVCFormDataAccessor accessor;
    
    public MVCFormDataAccessorTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
        super.setUp();
        this.accessor = new MVCFormDataAccessor();
    }
    
    public void testGetCommandObjectWithBaseCommandController() {
        HttpServletRequest request = new MockHttpServletRequest();
        HttpServletResponse response = new MockHttpServletResponse();
        Object command = new Object();
        Map model = new HashMap();
        
        final String commandName = "commandKey";
        BaseCommandController controller = new BaseCommandController() {
            protected ModelAndView handleRequestInternal(HttpServletRequest arg0, HttpServletResponse arg1) throws Exception {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
        
        controller.setCommandName(commandName);
        model.put(commandName, command);
        
        assertEquals(command, this.accessor.getCommandObject(request, response, controller, model));
    }

    public void testGetCommandObjectWithCustomController() {
        HttpServletRequest request = new MockHttpServletRequest();
        HttpServletResponse response = new MockHttpServletResponse();
        Object command = new Object();
        Map model = new HashMap();
        
        final String commandName = "commandKey";
        Object controller = new Object() {
            public String getCommandName() {
                return commandName;
            }
        };
        
        model.put(commandName, command);
        
        assertEquals(command, this.accessor.getCommandObject(request, response, controller, model));
    }
    
    public void testGetCommandObjectFails() {
        HttpServletRequest request = new MockHttpServletRequest();
        HttpServletResponse response = new MockHttpServletResponse();
        Object command = new Object();
        Map model = new HashMap();
        
        final String commandName = "commandKey";
        Object controller = new Object();
        
        model.put(commandName, command);
        
        assertTrue(this.accessor.getCommandObject(request, response, controller, model) == null);
    }
}
