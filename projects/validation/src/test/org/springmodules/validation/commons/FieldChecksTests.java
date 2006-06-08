package org.springmodules.validation.commons;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;
import org.apache.commons.validator.Field;
import org.apache.commons.validator.Validator;
import org.apache.commons.validator.ValidatorAction;
import org.apache.commons.validator.ValidatorResources;
import org.easymock.MockControl;
import org.springframework.validation.Errors;

/**
 * @author robh
 * @author Uri Boness
 */
public class FieldChecksTests extends TestCase {


    public void testExtractValueWithString() {
        String bean = "Rob Harrop";
        String result = FieldChecks.extractValue(bean, null);

        assertEquals("Should return the bean itself", bean, result);
    }

    public void testExtractValueWithMap() {
        String name = "Rob Harrop";
        String property = "name";

        Field field = new Field();
        field.setProperty(property);

        Map bean = new HashMap();
        bean.put(property, name);

        String result = FieldChecks.extractValue(bean, field);

        assertEquals("Should return Map entry", name, result);
    }

    public void testExtractWithJavaBean() {
        String name = "Rob Harrop";
        int age = 23;

        String property1 = "name";
        String property2 = "age";

        Field field1 = createFieldForProperty(property1);
        Field field2 = createFieldForProperty(property2);

        MockValidationBean validationBean = new MockValidationBean();
        validationBean.setName(name);
        validationBean.setAge(age);

        String nameResult = FieldChecks.extractValue(validationBean, field1);
        assertEquals(name, nameResult);

        String ageResult = FieldChecks.extractValue(validationBean, field2);
        assertEquals("" + age, ageResult);
    }

    public void testExtractWithNullBean() {
        String result = FieldChecks.extractValue(null, new Field());
        assertNull(result);
    }

    public void testValidateRequiredSuccess() {
        MockValidationBean bean = new MockValidationBean();
        bean.setName("Rob Harrop");

        Field field = createFieldForProperty("name");

        boolean result = FieldChecks.validateRequired(bean, null, field, null);
        assertTrue(result);
    }

    public void testValidateRequiredError() {
        MockValidationBean bean = new MockValidationBean();
        Field field = createFieldForProperty("name");

        MockControl control = MockControl.createControl(Errors.class);

        ValidatorAction validatorAction = new ValidatorAction();
        Errors errors = (Errors) control.getMock();

        boolean result = FieldChecks.validateRequired(bean, validatorAction, field, errors);
        assertFalse(result);
    }

    public void testValidateWhenValid_Success() throws Exception {
        MockValidationBean bean = new MockValidationBean();
        bean.setAge(5);
        Field field = createFieldForProperty("age");
        field.addVar("test", "(*this* < 10)", "");

        MockControl control = MockControl.createControl(Errors.class);

        ValidatorAction validatorAction = new ValidatorAction();
        Errors errors = (Errors) control.getMock();

        ValidatorResources vr = new ValidatorResources();
        vr.process();
        Validator validator = new Validator(vr);

        boolean result = FieldChecks.validateValidWhen(bean, validatorAction, field, errors, validator);
        assertTrue(result);
    }

    public void testValidateWhenValid_SuccessWithDouble() throws Exception {
        MockValidationBean bean = new MockValidationBean();
        bean.setValue(-0.45);
        Field field = createFieldForProperty("value");
        field.addVar("test", "(*this* > -10.43)", "");

        MockControl control = MockControl.createControl(Errors.class);

        ValidatorAction validatorAction = new ValidatorAction();
        Errors errors = (Errors) control.getMock();

        ValidatorResources vr = new ValidatorResources();
        vr.process();
        Validator validator = new Validator(vr);

        boolean result = FieldChecks.validateValidWhen(bean, validatorAction, field, errors, validator);
        assertTrue(result);
    }

    public void testValidateWhenValid_Failure() throws Exception {
        MockValidationBean bean = new MockValidationBean();
        Field field = createFieldForProperty("name");
        field.addVar("test", "(*this* != null)", "");

        MockControl control = MockControl.createControl(Errors.class);

        ValidatorAction validatorAction = new ValidatorAction();
        Errors errors = (Errors) control.getMock();

        ValidatorResources vr = new ValidatorResources();
        vr.process();
        Validator validator = new Validator(vr);

        boolean result = FieldChecks.validateValidWhen(bean, validatorAction, field, errors, validator);
        assertFalse(result);
    }

    public void testValidateWhenValid_SuccessWithString() throws Exception {
        MockValidationBean bean = new MockValidationBean();
        bean.setName("uri");
        Field field = createFieldForProperty("name");
        field.addVar("test", "(*this* == 'uri')", "");

        MockControl control = MockControl.createControl(Errors.class);

        ValidatorAction validatorAction = new ValidatorAction();
        Errors errors = (Errors) control.getMock();

        ValidatorResources vr = new ValidatorResources();
        vr.process();
        Validator validator = new Validator(vr);

        boolean result = FieldChecks.validateValidWhen(bean, validatorAction, field, errors, validator);
        assertTrue(result);
    }

    private Field createFieldForProperty(String property) {
        Field field = new Field();
        field.setProperty(property);
        return field;
    }

    public static class MockValidationBean {

        private String name;
        private int age;
        private double value;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public double getValue() {
            return value;
        }

        public void setValue(double value) {
            this.value = value;
        }
    }
}
