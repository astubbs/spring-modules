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

/**
 * Tests for {@link CompoundValidator}.
 *
 * @author Uri Boness
 */
public class CompoundValidatorTests extends TestCase {

    private MockControl validatorControl1;
    private Validator validator1;

    private MockControl validatorControl2;
    private Validator validator2;

    private CompoundValidator validator;

    protected void setUp() throws Exception {
        validatorControl1 = MockControl.createControl(Validator.class);
        validator1 = (Validator)validatorControl1.getMock();

        validatorControl2 = MockControl.createControl(Validator.class);
        validator2 = (Validator)validatorControl2.getMock();

        validator = new CompoundValidator(new Validator[] { validator1, validator2 });
    }

    public void testSupports_WhenFirstValidatorSupports() throws Exception {

        Class clazz = Object.class;

        validatorControl1.expectAndReturn(validator1.supports(clazz), true);
        validatorControl1.replay();
        validatorControl2.replay();

        assertTrue(validator.supports(clazz));

        validatorControl1.verify();
        validatorControl2.verify();
    }

    public void testSupports_WhenSecondValidatorSupports() throws Exception {

        Class clazz = Object.class;

        validatorControl1.expectAndReturn(validator1.supports(clazz), false);
        validatorControl2.expectAndReturn(validator2.supports(clazz), true);
        validatorControl1.replay();
        validatorControl2.replay();

        assertTrue(validator.supports(clazz));

        validatorControl1.verify();
        validatorControl2.verify();
    }

    public void testSupports_WhenNoValidatorSupports() throws Exception {

        Class clazz = Object.class;

        validatorControl1.expectAndReturn(validator1.supports(clazz), false);
        validatorControl2.expectAndReturn(validator2.supports(clazz), false);
        validatorControl1.replay();
        validatorControl2.replay();

        assertFalse(validator.supports(clazz));

        validatorControl1.verify();
        validatorControl2.verify();
    }

    public void testValidate_WhenAllValidatorsSupport() throws Exception {
        Object object = new Object();
        BindException errors = new BindException(object, "name");

        validatorControl1.expectAndReturn(validator1.supports(object.getClass()), true);
        validator1.validate(object, errors);
        validatorControl2.expectAndReturn(validator2.supports(object.getClass()), true);
        validator2.validate(object, errors);

        validatorControl1.replay();
        validatorControl2.replay();

        validator.validate(object, errors);

        validatorControl1.verify();
        validatorControl2.verify();
    }

    public void testValidate_WhenOnlyOneValidatorSupports() throws Exception {
        Object object = new Object();
        BindException errors = new BindException(object, "name");

        validatorControl1.expectAndReturn(validator1.supports(object.getClass()), true);
        validator1.validate(object, errors);
        validatorControl2.expectAndReturn(validator2.supports(object.getClass()), false);

        validatorControl1.replay();
        validatorControl2.replay();

        validator.validate(object, errors);

        validatorControl1.verify();
        validatorControl2.verify();
    }

    public void testValidate_WhenNoValidatorSupports() throws Exception {
        Object object = new Object();
        BindException errors = new BindException(object, "name");

        validatorControl1.expectAndReturn(validator1.supports(object.getClass()), false);
        validatorControl2.expectAndReturn(validator2.supports(object.getClass()), false);

        validatorControl1.replay();
        validatorControl2.replay();

        validator.validate(object, errors);

        validatorControl1.verify();
        validatorControl2.verify();
    }
}