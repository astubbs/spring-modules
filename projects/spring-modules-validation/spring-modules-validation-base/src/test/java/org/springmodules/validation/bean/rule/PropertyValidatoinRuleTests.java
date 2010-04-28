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

package org.springmodules.validation.bean.rule;

import junit.framework.TestCase;
import org.easymock.MockControl;
import org.springmodules.validation.util.condition.Condition;

/**
 * Tests for {@link org.springmodules.validation.bean.rule.PropertyValidationRule}.
 *
 * @author Uri Boness
 */
public class PropertyValidatoinRuleTests extends TestCase {

    private MockControl conditionControl;

    private Condition condition;

    private MockControl ruleControl;

    private ValidationRule rule;

    protected void setUp() throws Exception {

        conditionControl = MockControl.createControl(Condition.class);
        condition = (Condition) conditionControl.getMock();

        ruleControl = MockControl.createControl(ValidationRule.class);
        rule = (ValidationRule) ruleControl.getMock();
    }

    public void testIsApplicable_Success() throws Exception {

        conditionControl.expectAndReturn(condition.check("Uri"), true);
        conditionControl.replay();

        ValidationRule innerRule = new DefaultValidationRule(null, condition, "errorCode");
        PropertyValidationRule rule = new PropertyValidationRule("name", innerRule);
        assertTrue(rule.isApplicable(new Person("Uri")));

        conditionControl.verify();
    }

    public void testIsApplicable_Failure() throws Exception {

        conditionControl.expectAndReturn(condition.check("Uri"), false);
        conditionControl.replay();

        ValidationRule innerRule = new DefaultValidationRule(null, condition, "errorCode");
        PropertyValidationRule rule = new PropertyValidationRule("name", innerRule);
        assertFalse(rule.isApplicable(new Person("Uri")));

        conditionControl.verify();
    }

    public void testIsApplicable_WhenPropertyDoesNotExist() throws Exception {
        PropertyValidationRule rule = new PropertyValidationRule("name", null);
        assertFalse(rule.isApplicable(new Object()));
    }

    public void testGetCondition_Success() throws Exception {

        conditionControl.expectAndReturn(condition.check("Uri"), true);
        ruleControl.expectAndReturn(rule.getCondition(), condition);
        conditionControl.replay();
        ruleControl.replay();

        PropertyValidationRule propertyValidationRule = new PropertyValidationRule("name", rule);
        Condition propertyCondition = propertyValidationRule.getCondition();
        assertTrue(propertyCondition.check(new Person("Uri")));

        conditionControl.verify();
        ruleControl.verify();
    }

    public void testGetCondition_Failure() throws Exception {

        conditionControl.expectAndReturn(condition.check("Uri"), false);
        ruleControl.expectAndReturn(rule.getCondition(), condition);
        conditionControl.replay();
        ruleControl.replay();

        PropertyValidationRule propertyValidationRule = new PropertyValidationRule("name", rule);
        Condition propertyCondition = propertyValidationRule.getCondition();
        assertFalse(propertyCondition.check(new Person("Uri")));

        conditionControl.verify();
        ruleControl.verify();
    }

    public void testGetErrorCode() throws Exception {

        ruleControl.expectAndReturn(rule.getErrorCode(), "errorCode");
        ruleControl.replay();

        PropertyValidationRule propertyValidationRule = new PropertyValidationRule("name", rule);
        assertEquals("errorCode", propertyValidationRule.getErrorCode());

        ruleControl.verify();
    }

    public void testGetErrorArguments() throws Exception {

        Object object = new Object();

        Object[] arguments = new Object[0];
        ruleControl.expectAndReturn(rule.getErrorArguments(object), arguments);
        ruleControl.replay();

        PropertyValidationRule propertyValidationRule = new PropertyValidationRule("name", rule);
        assertSame(arguments, propertyValidationRule.getErrorArguments(object));

        ruleControl.verify();
    }

    public void testGetDefaultErrorMessage() throws Exception {

        ruleControl.expectAndReturn(rule.getDefaultErrorMessage(), "message");
        ruleControl.replay();

        PropertyValidationRule propertyValidationRule = new PropertyValidationRule("name", rule);
        assertEquals("message", propertyValidationRule.getDefaultErrorMessage());

        ruleControl.verify();
    }

    //================================================ Inner Classes ===================================================

    private static class Person {

        private String name;

        public Person(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}