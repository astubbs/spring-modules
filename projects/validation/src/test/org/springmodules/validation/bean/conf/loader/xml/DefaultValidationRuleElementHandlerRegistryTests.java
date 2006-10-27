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

import junit.framework.TestCase;
import org.springmodules.validation.bean.conf.loader.xml.handler.*;
import org.springmodules.validation.bean.conf.loader.xml.handler.jodatime.InstantInFutureRuleElementHandler;
import org.springmodules.validation.bean.conf.loader.xml.handler.jodatime.InstantInPastRuleElementHandler;

/**
 * Tests for {@link org.springmodules.validation.bean.conf.loader.xml.DefaultValidationRuleElementHandlerRegistry}.
 *
 * @author Uri Boness
 */
public class DefaultValidationRuleElementHandlerRegistryTests extends TestCase {

    public void testEmptyContructor() throws Exception {
        DefaultValidationRuleElementHandlerRegistry registry = new DefaultValidationRuleElementHandlerRegistry();

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
        assertTrue(containsHandlerOfType(propertyHandlers, InstantInFutureRuleElementHandler.class));
        assertTrue(containsHandlerOfType(propertyHandlers, InstantInPastRuleElementHandler.class));
    }

    //=============================================== Helper Methods ===================================================

    protected boolean containsHandlerOfType(ClassValidationElementHandler[] handlers, Class handlerType) {
        for (int i = 0; i < handlers.length; i++) {
            if (handlerType.isAssignableFrom(handlerType)) {
                return true;
            }
        }
        return false;
    }

    protected boolean containsHandlerOfType(PropertyValidationElementHandler[] handlers, Class handlerType) {
        for (int i = 0; i < handlers.length; i++) {
            if (handlerType.isAssignableFrom(handlerType)) {
                return true;
            }
        }
        return false;
    }

}