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

import java.beans.PropertyDescriptor;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import junit.framework.TestCase;
import org.easymock.MockControl;
import org.springframework.beans.BeanUtils;
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
import org.springmodules.validation.bean.conf.xml.handler.ExpressionClassValidationElementHandler;
import org.springmodules.validation.bean.conf.xml.handler.ExpressionPropertyValidationElementHandler;
import org.w3c.dom.Element;

/**
 * Tests for {@link org.springmodules.validation.bean.conf.xml.DefaultValidationRuleElementHandlerRegistry}.
 *
 * @author Uri Boness
 */
public class DefaultValidationRuleElementHandlerRegistryTests extends TestCase {

    private DefaultValidationRuleElementHandlerRegistry registry;

    private MockControl handler1Control;
    private PropertyValidationElementHandler handler1;

    private MockControl handler2Control;
    private PropertyValidationElementHandler handler2;

    protected void setUp() throws Exception {

        handler1Control = MockControl.createControl(PropertyValidationElementHandler.class);
        handler1 = (PropertyValidationElementHandler)handler1Control.getMock();

        handler2Control = MockControl.createControl(PropertyValidationElementHandler.class);
        handler2 = (PropertyValidationElementHandler)handler2Control.getMock();

        registry = new DefaultValidationRuleElementHandlerRegistry();
        registry.setPropertyHandlers(new PropertyValidationElementHandler[] { handler1, handler2 });
    }

    public void testEmptyContructor() throws Exception {
        registry = new DefaultValidationRuleElementHandlerRegistry();

        ClassValidationElementHandler[] classHandlers = registry.getClassHandlers();
        assertTrue(containsHandlerOfType(classHandlers, ExpressionClassValidationElementHandler.class));
        
        PropertyValidationElementHandler[] propertyHandlers = registry.getPropertyHandlers();
        assertTrue(containsHandlerOfType(propertyHandlers, NotNullRuleElementHandler.class));
        assertTrue(containsHandlerOfType(propertyHandlers, LengthRuleElementHandler.class));
        assertTrue(containsHandlerOfType(propertyHandlers, NotBlankRuleElementHandler.class));
        assertTrue(containsHandlerOfType(propertyHandlers, EmailRuleElementHandler.class));
        assertTrue(containsHandlerOfType(propertyHandlers, RegExpRuleElementHandler.class));
        assertTrue(containsHandlerOfType(propertyHandlers, SizeRuleElementHandler.class));
        assertTrue(containsHandlerOfType(propertyHandlers, NotEmptyRuleElementHandler.class));
        assertTrue(containsHandlerOfType(propertyHandlers, RangeRuleElementHandler.class));
        assertTrue(containsHandlerOfType(propertyHandlers, ExpressionPropertyValidationElementHandler.class));
        assertTrue(containsHandlerOfType(propertyHandlers, DateInPastRuleElementHandler.class));
        assertTrue(containsHandlerOfType(propertyHandlers, DateInFutureRuleElementHandler.class));
    }

    public void testFindHandler_WithASupportedHandler() throws Exception {

        PropertyDescriptor descriptor = BeanUtils.getPropertyDescriptor(TestBean.class, "name");

        Element element = createElement("bla");
        handler1Control.expectAndReturn(handler1.supports(element, TestBean.class, descriptor), false);
        handler2Control.expectAndReturn(handler2.supports(element, TestBean.class, descriptor), true);
        replay();
        assertSame(handler2, registry.findPropertyHandler(element, TestBean.class, descriptor));
        verify();
    }

    public void testFindHandler_WithNoSupportedHandler() throws Exception {

        PropertyDescriptor descriptor = BeanUtils.getPropertyDescriptor(TestBean.class, "name");

        Element element = createElement("bla");
        handler1Control.expectAndReturn(handler1.supports(element, TestBean.class, descriptor), false);
        handler2Control.expectAndReturn(handler2.supports(element, TestBean.class, descriptor), false);
        replay();
        assertEquals(null, registry.findPropertyHandler(element, TestBean.class, descriptor));
        verify();
    }

    public void testSetExtraHandlers() throws Exception {

        PropertyDescriptor descriptor = BeanUtils.getPropertyDescriptor(TestBean.class, "name");

        registry = new DefaultValidationRuleElementHandlerRegistry();
        registry.registerPropertyHandler(handler1);
        registry.setExtraPropertyHandlers(new PropertyValidationElementHandler[] { handler2 });
        Element element = createElement("bla");
        handler2Control.expectAndReturn(handler2.supports(element, TestBean.class, descriptor), true);
        replay();
        assertSame(handler2, registry.findPropertyHandler(element, TestBean.class, descriptor));
        verify();
    }

    //=============================================== Helper Methods ===================================================

    protected boolean containsHandlerOfType(ClassValidationElementHandler[] handlers, Class handlerType) {
        for (int i=0; i<handlers.length; i++) {
            if (handlerType.isAssignableFrom(handlerType)) {
                return true;
            }
        }
        return false;
    }

    protected boolean containsHandlerOfType(PropertyValidationElementHandler[] handlers, Class handlerType) {
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


    //=============================================== Inner Classes ===================================================

    public class TestBean {

        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}