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

package org.springmodules.validation.bean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import junit.framework.TestCase;
import org.easymock.MockControl;
import org.springframework.beans.BeanWrapper;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springmodules.validation.bean.conf.BeanValidationConfiguration;
import org.springmodules.validation.bean.conf.BeanValidationConfigurationLoader;
import org.springmodules.validation.bean.conf.CascadeValidation;
import org.springmodules.validation.bean.converter.ErrorCodeConverter;
import org.springmodules.validation.bean.rule.ValidationRule;
import org.springmodules.validation.util.condition.common.AlwaysFalseCondition;
import org.springmodules.validation.util.condition.common.AlwaysTrueCondition;

/**
 * Tests for {@link org.springmodules.validation.bean.BeanValidator}.
 *
 * @author Uri Boness
 */
public class BeanValidatorTests extends TestCase {

    private BeanValidator validator;

    private MockControl errorsControl;
    private Errors errors;

    private MockControl configurationControl;
    private BeanValidationConfiguration configuration;

    private MockControl ruleControl1;
    private ValidationRule rule1;

    private MockControl ruleControl2;
    private ValidationRule rule2;

    private MockControl converterControl;
    private ErrorCodeConverter converter;

    private MockControl loaderControl;
    private BeanValidationConfigurationLoader loader;

    private MockControl wrapperControl = MockControl.createControl(BeanWrapper.class);
    private BeanWrapper wrapper = (BeanWrapper)wrapperControl.getMock();

    protected void setUp() throws Exception {
        converterControl = MockControl.createControl(ErrorCodeConverter.class);
        converter = (ErrorCodeConverter)converterControl.getMock();

        loaderControl = MockControl.createControl(BeanValidationConfigurationLoader.class);
        loader = (BeanValidationConfigurationLoader)loaderControl.getMock();

        validator = new BeanValidator(loader);
        validator.setErrorCodeConverter(converter);

        errorsControl = MockControl.createControl(Errors.class);
        errors = (Errors)errorsControl.getMock();

        configurationControl = MockControl.createControl(BeanValidationConfiguration.class);
        configuration = (BeanValidationConfiguration)configurationControl.getMock();

        ruleControl1 = MockControl.createControl(ValidationRule.class);
        rule1 = (ValidationRule)ruleControl1.getMock();

        ruleControl2 = MockControl.createControl(ValidationRule.class);
        rule2 = (ValidationRule)ruleControl2.getMock();

        wrapperControl = MockControl.createControl(BeanWrapper.class);
        wrapper = (BeanWrapper)wrapperControl.getMock();
    }

    public void testSupports_WhenLoaderSupports() throws Exception {
        loaderControl.expectAndReturn(loader.supports(Object.class), true);
        replay();
        assertTrue(validator.supports(Object.class));
        verify();
    }

    public void testValidateObjectGraphConstraints_WithArrayProperty() throws Exception {
        final Object object = new Object();
        final Object[] propertyValue = new Object[0];
        final Set validatedObjects = new HashSet();

        // creating the bean with stub methods
        BeanValidator validator = new BeanValidator(loader) {
            protected void applyBeanValidation(BeanValidationConfiguration conf, Object obj, Errors errors) {
                assertSame(configuration, conf);
                assertSame(object, obj);
                assertSame(BeanValidatorTests.this.errors, errors);
            }
            protected BeanWrapper wrapBean(Object bean) {
                return wrapper;
            }
            protected void validateArrayProperty(Object root, Object array, String propertyName, Errors errors, Set validatedObjs) {
                assertSame(object, root);
                assertSame(propertyValue, array);
                assertEquals("array", propertyName);
                assertSame(BeanValidatorTests.this.errors, errors);
                assertSame(validatedObjects, validatedObjs);
            }
        };
        validator.setErrorCodeConverter(converter);

        // configuring the expectations of the bean wrapper that will be used to extract the array property
        wrapperControl.expectAndReturn(wrapper.getPropertyType("array"), Object[].class);
        wrapperControl.expectAndReturn(wrapper.getPropertyValue("array"), propertyValue);

        CascadeValidation[] cascadeValidations = new CascadeValidation[] { new CascadeValidation("array") };
        configurationControl.expectAndReturn(configuration.getCascadeValidations(), cascadeValidations);
        loaderControl.expectAndReturn(loader.loadConfiguration(Object.class), configuration);

        replay();
        validator.validateObjectGraphConstraints(object, object, errors, validatedObjects);
        assertFalse(validatedObjects.isEmpty());
        assertEquals(object, validatedObjects.iterator().next());
        verify();
    }

    public void testValidateObjectGraphConstraints_WithListProperty() throws Exception {
        final Object object = new Object();
        final List propertyValue = new ArrayList();
        final Set validatedObjects = new HashSet();

        // creating the bean with stub methods
        BeanValidator validator = new BeanValidator(loader) {
            protected void applyBeanValidation(BeanValidationConfiguration conf, Object obj, Errors errors) {
                assertSame(configuration, conf);
                assertSame(object, obj);
                assertSame(BeanValidatorTests.this.errors, errors);
            }
            protected BeanWrapper wrapBean(Object bean) {
                return wrapper;
            }

            protected void validateListOrSetProperty(Object root, Collection collection, String propertyName, Errors errors, Set validatedObjs) {
                assertSame(object, root);
                assertSame(propertyValue, collection);
                assertEquals("list", propertyName);
                assertSame(BeanValidatorTests.this.errors, errors);
                assertSame(validatedObjects, validatedObjs);
            }
        };
        validator.setErrorCodeConverter(converter);

        // configuring the expectations of the bean wrapper that will be used to extract the array property
        wrapperControl.expectAndReturn(wrapper.getPropertyType("list"), List.class);
        wrapperControl.expectAndReturn(wrapper.getPropertyValue("list"), propertyValue);

        CascadeValidation[] cascadeValidations = new CascadeValidation[] { new CascadeValidation("list") };
        configurationControl.expectAndReturn(configuration.getCascadeValidations(), cascadeValidations);
        loaderControl.expectAndReturn(loader.loadConfiguration(Object.class), configuration);

        replay();
        validator.validateObjectGraphConstraints(object, object, errors, validatedObjects);
        assertFalse(validatedObjects.isEmpty());
        assertEquals(object, validatedObjects.iterator().next());
        verify();
    }

    public void testValidateObjectGraphConstraints_WithSetProperty() throws Exception {
        final Object object = new Object();
        final Set propertyValue = new HashSet();
        final Set validatedObjects = new HashSet();

        // creating the bean with stub methods
        BeanValidator validator = new BeanValidator(loader) {
            protected void applyBeanValidation(BeanValidationConfiguration conf, Object obj, Errors errors) {
                assertSame(configuration, conf);
                assertSame(object, obj);
                assertSame(BeanValidatorTests.this.errors, errors);
            }
            protected BeanWrapper wrapBean(Object bean) {
                return wrapper;
            }

            protected void validateListOrSetProperty(Object root, Collection collection, String propertyName, Errors errors, Set validatedObjs) {
                assertSame(object, root);
                assertSame(propertyValue, collection);
                assertEquals("set", propertyName);
                assertSame(BeanValidatorTests.this.errors, errors);
                assertSame(validatedObjects, validatedObjs);
            }
        };
        validator.setErrorCodeConverter(converter);

        // configuring the expectations of the bean wrapper that will be used to extract the array property
        wrapperControl.expectAndReturn(wrapper.getPropertyType("set"), Set.class);
        wrapperControl.expectAndReturn(wrapper.getPropertyValue("set"), propertyValue);

        CascadeValidation[] cascadeValidations = new CascadeValidation[] { new CascadeValidation("set") };
        configurationControl.expectAndReturn(configuration.getCascadeValidations(), cascadeValidations);
        loaderControl.expectAndReturn(loader.loadConfiguration(Object.class), configuration);

        replay();
        validator.validateObjectGraphConstraints(object, object, errors, validatedObjects);
        assertFalse(validatedObjects.isEmpty());
        assertEquals(object, validatedObjects.iterator().next());
        verify();
    }

    public void testValidateObjectGraphConstraints_WithMapProperty() throws Exception {
        final Object object = new Object();
        final Map propertyValue = new HashMap();
        final Set validatedObjects = new HashSet();

        // creating the bean with stub methods
        BeanValidator validator = new BeanValidator(loader) {
            protected void applyBeanValidation(BeanValidationConfiguration conf, Object obj, Errors errors) {
                assertSame(configuration, conf);
                assertSame(object, obj);
                assertSame(BeanValidatorTests.this.errors, errors);
            }
            protected BeanWrapper wrapBean(Object bean) {
                return wrapper;
            }

            protected void validateMapProperty(Object root, Map map, String propertyName, Errors errors, Set validatedObjs) {
                assertSame(object, root);
                assertSame(propertyValue, map);
                assertEquals("map", propertyName);
                assertSame(BeanValidatorTests.this.errors, errors);
                assertSame(validatedObjects, validatedObjs);
            }
        };
        validator.setErrorCodeConverter(converter);

        // configuring the expectations of the bean wrapper that will be used to extract the array property
        wrapperControl.expectAndReturn(wrapper.getPropertyType("map"), Map.class);
        wrapperControl.expectAndReturn(wrapper.getPropertyValue("map"), propertyValue);

        CascadeValidation[] cascadeValidations = new CascadeValidation[] { new CascadeValidation("map") };
        configurationControl.expectAndReturn(configuration.getCascadeValidations(), cascadeValidations);
        loaderControl.expectAndReturn(loader.loadConfiguration(Object.class), configuration);

        replay();
        validator.validateObjectGraphConstraints(object, object, errors, validatedObjects);
        assertFalse(validatedObjects.isEmpty());
        assertEquals(object, validatedObjects.iterator().next());
        verify();
    }

    public void testValidateObjectGraphConstraints_WithSubBeanProperty() throws Exception {
        final Object object = new Object();
        final Object propertyValue = new Object();
        final Set validatedObjects = new HashSet();

        // creating the bean with stub methods
        BeanValidator validator = new BeanValidator(loader) {
            protected void applyBeanValidation(BeanValidationConfiguration conf, Object obj, Errors errors) {
                assertSame(configuration, conf);
                assertSame(object, obj);
                assertSame(BeanValidatorTests.this.errors, errors);
            }
            protected BeanWrapper wrapBean(Object bean) {
                return wrapper;
            }

            protected void validatedSubBean(Object root, Object subBean, String propertyName, Errors errors, Set validatedObjs) {
                assertSame(object, root);
                assertSame(propertyValue, subBean);
                assertEquals("subBean", propertyName);
                assertSame(BeanValidatorTests.this.errors, errors);
                assertSame(validatedObjects, validatedObjs);
            }
        };
        validator.setErrorCodeConverter(converter);

        // configuring the expectations of the bean wrapper that will be used to extract the array property
        wrapperControl.expectAndReturn(wrapper.getPropertyType("subBean"), Object.class);
        wrapperControl.expectAndReturn(wrapper.getPropertyValue("subBean"), propertyValue);

        CascadeValidation[] cascadeValidations = new CascadeValidation[] { new CascadeValidation("subBean") };
        configurationControl.expectAndReturn(configuration.getCascadeValidations(), cascadeValidations);
        loaderControl.expectAndReturn(loader.loadConfiguration(Object.class), configuration);

        replay();
        validator.validateObjectGraphConstraints(object, object, errors, validatedObjects);
        assertFalse(validatedObjects.isEmpty());
        assertEquals(object, validatedObjects.iterator().next());
        verify();
    }

    public void testValidateArrayProperty() throws Exception {
        final Object root = new Object();
        final Object element1 = new Object();
        final Object element2 = new Object();
        final Object[] array = new Object[] { element1, element2};
        final Set validatedObjects = new HashSet();

        BeanValidator validator = new BeanValidator(loader) {
            private int runCount = 0;
            protected void validateObjectGraphConstraints(Object rootObject, Object obj, Errors errors, Set validatedObjs) {
                assertSame(root, rootObject);
                assertSame(BeanValidatorTests.this.errors, errors);
                assertSame(validatedObjects, validatedObjs);
                switch (runCount++) {
                    case 0:
                        assertSame(element1, obj);
                        break;
                    case 1:
                        assertSame(element2, obj);
                }
            }
        };
        validator.setErrorCodeConverter(converter);

        errors.pushNestedPath("array[0]");
        errors.popNestedPath();
        errors.pushNestedPath("array[1]");
        errors.popNestedPath();

        replay();
        validator.validateArrayProperty(root, array, "array", errors, validatedObjects);
        verify();
    }

    public void testValidateListOrSetProperty_WithList() throws Exception {
        final Object root = new Object();
        final Object element1 = new Object();
        final Object element2 = new Object();
        final List list = new ArrayList(); list.add(element1); list.add(element2);
        final Set validatedObjects = new HashSet();

        BeanValidator validator = new BeanValidator(loader) {
            private int runCount = 0;
            protected void validateObjectGraphConstraints(Object rootObject, Object obj, Errors errors, Set validatedObjs) {
                assertSame(root, rootObject);
                assertSame(BeanValidatorTests.this.errors, errors);
                assertSame(validatedObjects, validatedObjs);
                switch (runCount++) {
                    case 0:
                        assertSame(element1, obj);
                        break;
                    case 1:
                        assertSame(element2, obj);
                }
            }
        };
        validator.setErrorCodeConverter(converter);

        errors.pushNestedPath("list[0]");
        errors.popNestedPath();
        errors.pushNestedPath("list[1]");
        errors.popNestedPath();

        replay();
        validator.validateListOrSetProperty(root, list, "list", errors, validatedObjects);
        verify();
    }

    public void testValidateListOrSetProperty_WithSet() throws Exception {
        final Object root = new Object();
        final Object element1 = new Integer(1);
        final Object element2 = new Integer(2);
        final Set set = new TreeSet(); set.add(element1); set.add(element2);
        final Set validatedObjects = new HashSet();

        BeanValidator validator = new BeanValidator(loader) {
            private int runCount = 0;
            protected void validateObjectGraphConstraints(Object rootObject, Object obj, Errors errors, Set validatedObjs) {
                assertSame(root, rootObject);
                assertSame(BeanValidatorTests.this.errors, errors);
                assertSame(validatedObjects, validatedObjs);
                switch (runCount++) {
                    case 0:
                        assertSame(element1, obj);
                        break;
                    case 1:
                        assertSame(element2, obj);
                }
            }
        };
        validator.setErrorCodeConverter(converter);

        errors.pushNestedPath("set[0]");
        errors.popNestedPath();
        errors.pushNestedPath("set[1]");
        errors.popNestedPath();

        replay();
        validator.validateListOrSetProperty(root, set, "set", errors, validatedObjects);
        verify();
    }

    public void testValidateMapProperty() throws Exception {
        final Object root = new Object();
        final Object element1 = new Object();
        final Object element2 = new Object();
        final Map map = new HashMap();
        map.put("e1", element1);
        map.put("e2", element2);
        final Set validatedObjects = new HashSet();

        BeanValidator validator = new BeanValidator(loader) {
            protected void validateObjectGraphConstraints(Object rootObject, Object obj, Errors errors, Set validatedObjs) {
                assertSame(root, rootObject);
                assertSame(BeanValidatorTests.this.errors, errors);
                assertSame(validatedObjects, validatedObjs);
                assertTrue(element1 == obj || element2 == obj);
            }
        };
        validator.setErrorCodeConverter(converter);

        errors.pushNestedPath("map[e1]");
        errors.popNestedPath();
        errors.pushNestedPath("map[e2]");
        errors.popNestedPath();

        replay();
        validator.validateMapProperty(root, map, "map", errors, validatedObjects);
        verify();
    }

    public void testValidatedSubBean() throws Exception {
        final Object root = new Object();
        final Object subBean = new Object();
        final Set validatedObjects = new HashSet();

        BeanValidator validator = new BeanValidator(loader) {
            protected void validateObjectGraphConstraints(Object rootObject, Object obj, Errors errors, Set validatedObjs) {
                assertSame(root, rootObject);
                assertSame(BeanValidatorTests.this.errors, errors);
                assertSame(validatedObjects, validatedObjs);
                assertSame(subBean, obj);
            }
        };
        validator.setErrorCodeConverter(converter);

        errors.pushNestedPath("bean");
        errors.popNestedPath();

        replay();
        validator.validatedSubBean(root, subBean, "bean", errors, validatedObjects);
        verify();
    }

    public void testApplyBeanValidation() throws Exception {
        final Object object = new Object();

        BeanValidator validator = new BeanValidator(loader) {
            protected void applyGlobalValidationRules(BeanValidationConfiguration conf, Object obj, Errors errors) {
                assertSame(configuration, conf);
                assertSame(object, obj);
                assertSame(BeanValidatorTests.this.errors, errors);
            }
            protected void applyPropertiesValidationRules(BeanValidationConfiguration conf, Object obj, Errors errors) {
                assertSame(configuration, conf);
                assertSame(object, obj);
                assertSame(BeanValidatorTests.this.errors, errors);
            }
            protected void applyCustomValidator(BeanValidationConfiguration conf, Object obj, Errors errors) {
                assertSame(configuration, conf);
                assertSame(object, obj);
                assertSame(BeanValidatorTests.this.errors, errors);
            }
        };

        replay();
        validator.applyBeanValidation(configuration, object, errors);
        verify();
    }

    public void testApplyGlobalValidationRules() throws Exception {
        Object object = new Object();

        Object[] args = new Object[0];
        ruleControl1.expectAndReturn(rule1.isApplicable(object), true);
        ruleControl1.expectAndReturn(rule1.getCondition(), new AlwaysFalseCondition());
        ruleControl1.expectAndReturn(rule1.getErrorCode(), "errorCode1");
        ruleControl1.expectAndReturn(rule1.getDefaultErrorMessage(), "message1");
        ruleControl1.expectAndReturn(rule1.getErrorArguments(object), args);

        ruleControl2.expectAndReturn(rule2.isApplicable(object), true);
        ruleControl2.expectAndReturn(rule2.getCondition(), new AlwaysTrueCondition());

        ValidationRule[] globalRules = new ValidationRule[] { rule1, rule2 };
        configurationControl.expectAndReturn(configuration.getGlobalRules(), globalRules);

        converterControl.expectAndReturn(converter.convertGlobalErrorCode("errorCode1", Object.class), "_errorCode1");

        errorsControl.expectAndReturn(errors.getNestedPath(), "");
        errors.reject("_errorCode1", args, "message1");

        replay();
        validator.applyGlobalValidationRules(configuration, object, errors);
        verify();
    }

    public void testApplyPropertiesValidationRules() throws Exception {
        final Object object = new Object();

        final ValidationRule[] rules = new ValidationRule[] { rule1, rule2 };
        configurationControl.expectAndReturn(configuration.getValidatedProperties(), new String[] { "name" });
        configurationControl.expectAndReturn(configuration.getPropertyRules("name"), rules);

        BeanValidator validator = new BeanValidator() {
            protected void validateAndShortCircuitRules(ValidationRule[] validationRules, String propertyName, Object obj, Errors errors) {
                assertSame(rules, rules);
                assertEquals("name", propertyName);
                assertEquals(object, obj);
                assertSame(BeanValidatorTests.this.errors, errors);
            }
        };
        validator.setErrorCodeConverter(converter);

        replay();
        validator.applyPropertiesValidationRules(configuration, object, errors);
        verify();
    }


    public void testValidateAndShortCircuitRules_WhenOnlyTheSecondFails() throws Exception {
        Object object = new Object();

        ruleControl1.expectAndReturn(rule1.isApplicable(object), true);
        ruleControl1.expectAndReturn(rule1.getCondition(), new AlwaysTrueCondition());

        Object[] args = new Object[0];
        ruleControl2.expectAndReturn(rule2.isApplicable(object), true);
        ruleControl2.expectAndReturn(rule2.getCondition(), new AlwaysFalseCondition());
        ruleControl2.expectAndReturn(rule2.getErrorCode(), "errorCode2");
        ruleControl2.expectAndReturn(rule2.getDefaultErrorMessage(), "message2");
        ruleControl2.expectAndReturn(rule2.getErrorArguments(object), args);

        ValidationRule[] rules = new ValidationRule[] { rule1, rule2 };

        converterControl.expectAndReturn(converter.convertPropertyErrorCode("errorCode2", Object.class, "name"), "_errorCode2");

        errors.rejectValue("name", "_errorCode2", args, "message2");

        replay();
        validator.validateAndShortCircuitRules(rules, "name", object, errors);
        verify();
    }

    public void testValidateAndShortCircuitRules_WhenTheFirstFails() throws Exception {
        Object object = new Object();

        Object[] args = new Object[0];
        ruleControl1.expectAndReturn(rule1.isApplicable(object), true);
        ruleControl1.expectAndReturn(rule1.getCondition(), new AlwaysFalseCondition());
        ruleControl1.expectAndReturn(rule1.getErrorCode(), "errorCode1");
        ruleControl1.expectAndReturn(rule1.getDefaultErrorMessage(), "message1");
        ruleControl1.expectAndReturn(rule1.getErrorArguments(object), args);

        ValidationRule[] rules = new ValidationRule[] { rule1, rule2 };

        converterControl.expectAndReturn(converter.convertPropertyErrorCode("errorCode1", Object.class, "name"), "_errorCode1");

        errors.rejectValue("name", "_errorCode1", args, "message1");

        replay();
        validator.validateAndShortCircuitRules(rules, "name", object, errors);
        verify();
    }

    public void testValidateAndShortCircuitRules_WhenNoneFail() throws Exception {
        Object object = new Object();

        ruleControl1.expectAndReturn(rule1.isApplicable(object), true);
        ruleControl1.expectAndReturn(rule1.getCondition(), new AlwaysTrueCondition());

        ruleControl2.expectAndReturn(rule2.isApplicable(object), true);
        ruleControl2.expectAndReturn(rule2.getCondition(), new AlwaysTrueCondition());

        ValidationRule[] rules = new ValidationRule[] { rule1, rule2 };

        replay();
        validator.validateAndShortCircuitRules(rules, "name", object, errors);
        verify();
    }

    public void testApplyCustomValidator() throws Exception {
        Object object = new Object();

        MockControl customValidatorControl = MockControl.createControl(Validator.class);
        Validator customValidator = (Validator)customValidatorControl.getMock();
        customValidator.validate(object, errors);
        configurationControl.expectAndReturn(configuration.getCustomValidator(), customValidator);

        replay();
        validator.applyCustomValidator(configuration, object, errors);
        verify();
    }

    public void testApplyCustomValidator_WhenThereIsNoCustomValidator() throws Exception {
        Object object = new Object();

        configurationControl.expectAndReturn(configuration.getCustomValidator(), null);

        replay();
        validator.applyCustomValidator(configuration, object, errors);
        verify();
    }

    //=============================================== Helper Methods ===================================================

    protected void replay() {
        ruleControl1.replay();
        ruleControl2.replay();
        configurationControl.replay();
        errorsControl.replay();
        converterControl.replay();
        loaderControl.replay();
        wrapperControl.replay();
    }

    protected void verify() {
        ruleControl1.verify();
        ruleControl2.verify();
        configurationControl.verify();
        errorsControl.verify();
        converterControl.verify();
        loaderControl.verify();
        wrapperControl.verify();
    }

}