/*
 * Copyright 2004-2005 the original author or authors.
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

package org.springmodules.validation.bean.conf.xml;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import junit.framework.TestCase;
import org.easymock.MockControl;
import org.springmodules.validation.bean.conf.xml.handler.DateInFutureRuleElementHandler;
import org.springmodules.validation.bean.conf.xml.handler.DateInPastRuleElementHandler;
import org.springmodules.validation.bean.conf.xml.handler.EmailRuleElementHandler;
import org.springmodules.validation.bean.conf.xml.handler.LengthRuleElementHandler;
import org.springmodules.validation.bean.conf.xml.handler.NotBlankRuleElementHandler;
import org.springmodules.validation.bean.conf.xml.handler.NotEmptyRuleElementHandler;
import org.springmodules.validation.bean.conf.xml.handler.NotNullRuleElementHandler;
import org.springmodules.validation.bean.conf.xml.handler.RangeRuleElementHandler;
import org.springmodules.validation.bean.conf.xml.handler.RegExpRuleElementHandler;
import org.springmodules.validation.bean.conf.xml.handler.SizeRuleElementHandler;
import org.springmodules.validation.bean.conf.xml.handler.ValangRuleElementHandler;
import org.w3c.dom.Element;

/**
 * Tests for {@link org.springmodules.validation.bean.conf.xml.DefaultValidationRuleElementHandlerRegistry}.
 *
 * @author Uri Boness
 */
public class DefaultValidationRuleElementHandlerRegistryTests extends TestCase {

    private DefaultValidationRuleElementHandlerRegistry registry;

    private MockControl handler1Control;
    private ValidationRuleElementHandler handler1;

    private MockControl handler2Control;
    private ValidationRuleElementHandler handler2;

    protected void setUp() throws Exception {

        handler1Control = MockControl.createControl(ValidationRuleElementHandler.class);
        handler1 = (ValidationRuleElementHandler)handler1Control.getMock();

        handler2Control = MockControl.createControl(ValidationRuleElementHandler.class);
        handler2 = (ValidationRuleElementHandler)handler2Control.getMock();

        ValidationRuleElementHandler[] handlers = new ValidationRuleElementHandler[] { handler1, handler2 };

        registry = new DefaultValidationRuleElementHandlerRegistry(handlers);
    }

    public void testEmptyContructor() throws Exception {
        registry = new DefaultValidationRuleElementHandlerRegistry();
        ValidationRuleElementHandler[] handlers = registry.getHandlers();

        assertTrue(containsHandlerOfType(handlers, NotNullRuleElementHandler.class));
        assertTrue(containsHandlerOfType(handlers, LengthRuleElementHandler.class));
        assertTrue(containsHandlerOfType(handlers, NotBlankRuleElementHandler.class));
        assertTrue(containsHandlerOfType(handlers, EmailRuleElementHandler.class));
        assertTrue(containsHandlerOfType(handlers, RegExpRuleElementHandler.class));
        assertTrue(containsHandlerOfType(handlers, SizeRuleElementHandler.class));
        assertTrue(containsHandlerOfType(handlers, NotEmptyRuleElementHandler.class));
        assertTrue(containsHandlerOfType(handlers, RangeRuleElementHandler.class));
        assertTrue(containsHandlerOfType(handlers, ValangRuleElementHandler.class));
        assertTrue(containsHandlerOfType(handlers, DateInPastRuleElementHandler.class));
        assertTrue(containsHandlerOfType(handlers, DateInFutureRuleElementHandler.class));
    }

    public void testFindHandler_WithASupportedHandler() throws Exception {
        Element element = createElement("bla");
        handler1Control.expectAndReturn(handler1.supports(element), false);
        handler2Control.expectAndReturn(handler2.supports(element), true);
        replay();
        assertSame(handler2, registry.findHandler(element));
        verify();
    }

    public void testFindHandler_WithNoSupportedHandler() throws Exception {
        Element element = createElement("bla");
        handler1Control.expectAndReturn(handler1.supports(element), false);
        handler2Control.expectAndReturn(handler2.supports(element), false);
        replay();
        assertEquals(null, registry.findHandler(element));
        verify();
    }

    public void testRegisterHandler() throws Exception {
        registry = new DefaultValidationRuleElementHandlerRegistry(new ValidationRuleElementHandler[] { handler1 });
        registry.registerHandler(handler2);
        Element element = createElement("bla");
        handler2Control.expectAndReturn(handler2.supports(element), true);
        replay();
        assertSame(handler2, registry.findHandler(element));
        verify();
    }

    public void testSetExtraHandlers() throws Exception {
        registry = new DefaultValidationRuleElementHandlerRegistry(new ValidationRuleElementHandler[] { handler1 });
        registry.setExtraHandlers(new ValidationRuleElementHandler[] { handler2 });
        Element element = createElement("bla");
        handler2Control.expectAndReturn(handler2.supports(element), true);
        replay();
        assertSame(handler2, registry.findHandler(element));
        verify();
    }

    //=============================================== Helper Methods ===================================================

    protected boolean containsHandlerOfType(ValidationRuleElementHandler[] handlers, Class handlerType) {
        for (int i=0; i<handlers.length; i++) {
            if (handlerType.isAssignableFrom(handlerType)) {
                return true;
            }
        }
        return false;
    }

    protected Element createElement(String name) throws Exception {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        return builder.newDocument().createElement(name);
    }

    protected void replay() {
        handler1Control.replay();
        handler2Control.replay();
    }

    protected void verify() {
        handler1Control.verify();
        handler2Control.verify();
    }

}