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

import junit.framework.TestCase;
import org.easymock.MockControl;
import org.springframework.validation.BindException;
import org.springframework.validation.Validator;
import org.springmodules.validation.util.condition.Condition;

/**
 * Tests for {@link org.springmodules.validation.validator.ConditionalValidator}.
 *
 * @author Uri Boness
 */
public class ConditionalValidatorTests extends TestCase {

    private MockControl conditionControl;

    private Condition condition;

    private MockControl innerValidatorControl;

    private Validator innerValidator;

    private ConditionalValidator validator;

    protected void setUp() throws Exception {
        conditionControl = MockControl.createControl(Condition.class);
        condition = (Condition) conditionControl.getMock();

        innerValidatorControl = MockControl.createControl(Validator.class);
        innerValidator = (Validator) innerValidatorControl.getMock();

        validator = new ConditionalValidator(innerValidator, condition);
    }

    public void testSupports_WhenInnerValidatorSupports() throws Exception {
        Class clazz = Object.class;

        innerValidatorControl.expectAndReturn(innerValidator.supports(clazz), true);
        innerValidatorControl.replay();
        conditionControl.replay();

        assertTrue(validator.supports(clazz));

        innerValidatorControl.verify();
        conditionControl.verify();
    }

    public void testSupports_WhenInnerValidatorDoesNotSupport() throws Exception {
        Class clazz = Object.class;

        innerValidatorControl.expectAndReturn(innerValidator.supports(clazz), false);
        innerValidatorControl.replay();
        conditionControl.replay();

        assertFalse(validator.supports(clazz));

        innerValidatorControl.verify();
        conditionControl.verify();
    }

    public void testValidate_WhenConditionCheckPass() throws Exception {
        Object object = new Object();
        BindException errors = new BindException(object, "name");

        conditionControl.expectAndReturn(condition.check(object), true);
        innerValidator.validate(object, errors);

        conditionControl.replay();
        innerValidatorControl.replay();

        validator.validate(object, errors);

        conditionControl.verify();
        innerValidatorControl.verify();
    }

    public void testValidate_WhenConditionCheckFail() throws Exception {
        Object object = new Object();
        BindException errors = new BindException(object, "name");

        conditionControl.expectAndReturn(condition.check(object), false);

        conditionControl.replay();
        innerValidatorControl.replay();

        validator.validate(object, errors);

        conditionControl.verify();
        innerValidatorControl.verify();
    }
}