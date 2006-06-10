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

package org.springmodules.validation.validator;

import org.easymock.MockControl;
import org.springframework.validation.Errors;
import org.springmodules.validation.util.condition.Condition;
import junit.framework.TestCase;

/**
 * Tests for {@link org.springmodules.validation.validator.RuleBasedValidator}.
 *
 * @author Uri Boness
 */
public class RuleBasedValidatorTests extends TestCase {

    private RuleBasedValidator validator;

    private MockControl globalConditionControl;
    private Condition globalCondition;

    private MockControl propertyConditionControl;
    private Condition propertyCondition;

    private MockControl errorsControl;
    private Errors errors;

    protected void setUp() throws Exception {
        globalConditionControl = MockControl.createControl(Condition.class);
        globalCondition = (Condition)globalConditionControl.getMock();

        propertyConditionControl = MockControl.createControl(Condition.class);
        propertyCondition = (Condition)propertyConditionControl.getMock();

        errorsControl = MockControl.createControl(Errors.class);
        errors = (Errors)errorsControl.getMock();

        validator = createRuleBasedValidator(Person.class);
    }

    public void testValidate() throws Exception {

        Object[] args = new Object[0];
        validator.addGlobalRule(globalCondition, "global.errorCode", "global.message", args);
        validator.addPropertyRule("name", propertyCondition, "property.errorCode", "property.message", args);

        Person person = new Person("Uri");

        globalConditionControl.expectAndReturn(globalCondition.check(person), false);
        propertyConditionControl.expectAndReturn(propertyCondition.check("Uri"), false);
        errors.reject("global.errorCode", args, "global.message");
        errors.rejectValue("name", "property.errorCode", args, "property.message");

        globalConditionControl.replay();
        propertyConditionControl.replay();
        errorsControl.replay();

        validator.validate(person, errors);

        globalConditionControl.verify();
        propertyConditionControl.verify();
        errorsControl.verify();
    }

    public void testValidate_WithoutDefaultErrorMessages() throws Exception {

        Object[] args = new Object[0];
        validator.addGlobalRule(globalCondition, "global.errorCode", args);
        validator.addPropertyRule("name", propertyCondition, "property.errorCode", args);

        Person person = new Person("Uri");

        globalConditionControl.expectAndReturn(globalCondition.check(person), false);
        propertyConditionControl.expectAndReturn(propertyCondition.check("Uri"), false);
        errors.reject("global.errorCode", args, "global.errorCode");
        errors.rejectValue("name", "property.errorCode", args, "property.errorCode");

        globalConditionControl.replay();
        propertyConditionControl.replay();
        errorsControl.replay();

        validator.validate(person, errors);

        globalConditionControl.verify();
        propertyConditionControl.verify();
        errorsControl.verify();
    }

    public void testValidate_WhenOnlyPropertyValidationFails() throws Exception {

        Object[] args = new Object[0];
        validator.addGlobalRule(globalCondition, "global.errorCode", args);
        validator.addPropertyRule("name", propertyCondition, "property.errorCode", args);

        Person person = new Person("Uri");

        globalConditionControl.expectAndReturn(globalCondition.check(person), true);
        propertyConditionControl.expectAndReturn(propertyCondition.check("Uri"), false);
        errors.rejectValue("name", "property.errorCode", args, "property.errorCode");

        globalConditionControl.replay();
        propertyConditionControl.replay();
        errorsControl.replay();

        validator.validate(person, errors);

        globalConditionControl.verify();
        propertyConditionControl.verify();
        errorsControl.verify();
    }

    public void testValidate_WhenOnlyGlobalValidationFails() throws Exception {

        Object[] args = new Object[0];
        validator.addGlobalRule(globalCondition, "global.errorCode", args);
        validator.addPropertyRule("name", propertyCondition, "property.errorCode", args);

        Person person = new Person("Uri");

        globalConditionControl.expectAndReturn(globalCondition.check(person), false);
        propertyConditionControl.expectAndReturn(propertyCondition.check("Uri"), true);
        errors.reject("global.errorCode", args, "global.errorCode");

        globalConditionControl.replay();
        propertyConditionControl.replay();
        errorsControl.replay();

        validator.validate(person, errors);

        globalConditionControl.verify();
        propertyConditionControl.verify();
        errorsControl.verify();
    }

    protected RuleBasedValidator createRuleBasedValidator(Class clazz) {
        return new RuleBasedValidator();
    }


    //================================================ Inner Classes ===================================================

    private static class Person {

        private String name;
        private String nickname;

        public Person() {
        }

        public Person(String name) {
            this(name, name);
        }

        public Person(String name, String nickname) {
            this.name = name;
            this.nickname = nickname;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }
    }

}