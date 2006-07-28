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

package org.springmodules.validation.bean.conf.loader.xml;

import java.beans.PropertyDescriptor;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import junit.framework.TestCase;
import org.easymock.MockControl;
import org.springframework.beans.BeanUtils;
import org.w3c.dom.Element;

/**
 * Tests for {@link SimpleValidationRuleElementHandlerRegistry}.
 *
 * @author Uri Boness
 */
public class SimpleValidationRuleElementHandlerRegistryTests extends TestCase {

    private SimpleValidationRuleElementHandlerRegistry registry;

    private MockControl handler1Control;
    private PropertyValidationElementHandler handler1;

    private MockControl handler2Control;
    private PropertyValidationElementHandler handler2;

    protected void setUp() throws Exception {

        handler1Control = MockControl.createControl(PropertyValidationElementHandler.class);
        handler1 = (PropertyValidationElementHandler)handler1Control.getMock();

        handler2Control = MockControl.createControl(PropertyValidationElementHandler.class);
        handler2 = (PropertyValidationElementHandler)handler2Control.getMock();

        registry = new SimpleValidationRuleElementHandlerRegistry();
        registry.setPropertyHandlers(new PropertyValidationElementHandler[] { handler1, handler2 });
    }

    public void testFindHandler_WithASupportedHandler() throws Exception {

        PropertyDescriptor descriptor = BeanUtils.getPropertyDescriptor(org.springmodules.validation.bean.conf.loader.xml.SimpleValidationRuleElementHandlerRegistryTests.TestBean.class, "name");

        Element element = createElement("bla");
        handler1Control.expectAndReturn(handler1.supports(element, org.springmodules.validation.bean.conf.loader.xml.SimpleValidationRuleElementHandlerRegistryTests.TestBean.class, descriptor), false);
        handler2Control.expectAndReturn(handler2.supports(element, org.springmodules.validation.bean.conf.loader.xml.SimpleValidationRuleElementHandlerRegistryTests.TestBean.class, descriptor), true);
        replay();
        assertSame(handler2, registry.findPropertyHandler(element, org.springmodules.validation.bean.conf.loader.xml.SimpleValidationRuleElementHandlerRegistryTests.TestBean.class, descriptor));
        verify();
    }

    public void testFindHandler_WithNoSupportedHandler() throws Exception {

        PropertyDescriptor descriptor = BeanUtils.getPropertyDescriptor(org.springmodules.validation.bean.conf.loader.xml.SimpleValidationRuleElementHandlerRegistryTests.TestBean.class, "name");

        Element element = createElement("bla");
        handler1Control.expectAndReturn(handler1.supports(element, org.springmodules.validation.bean.conf.loader.xml.SimpleValidationRuleElementHandlerRegistryTests.TestBean.class, descriptor), false);
        handler2Control.expectAndReturn(handler2.supports(element, org.springmodules.validation.bean.conf.loader.xml.SimpleValidationRuleElementHandlerRegistryTests.TestBean.class, descriptor), false);
        replay();
        assertEquals(null, registry.findPropertyHandler(element, org.springmodules.validation.bean.conf.loader.xml.SimpleValidationRuleElementHandlerRegistryTests.TestBean.class, descriptor));
        verify();
    }

    public void testSetExtraHandlers() throws Exception {

        PropertyDescriptor descriptor = BeanUtils.getPropertyDescriptor(org.springmodules.validation.bean.conf.loader.xml.SimpleValidationRuleElementHandlerRegistryTests.TestBean.class, "name");

        registry = new SimpleValidationRuleElementHandlerRegistry();
        registry.registerPropertyHandler(handler1);
        registry.setExtraPropertyHandlers(new PropertyValidationElementHandler[] { handler2 });
        Element element = createElement("bla");
        handler2Control.expectAndReturn(handler2.supports(element, org.springmodules.validation.bean.conf.loader.xml.SimpleValidationRuleElementHandlerRegistryTests.TestBean.class, descriptor), true);
        replay();
        assertSame(handler2, registry.findPropertyHandler(element, org.springmodules.validation.bean.conf.loader.xml.SimpleValidationRuleElementHandlerRegistryTests.TestBean.class, descriptor));
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
