package org.springmodules.validation.commons;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import junit.framework.TestCase;
import org.apache.commons.validator.Validator;
import org.apache.commons.validator.ValidatorResources;
import org.easymock.MockControl;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.validation.BindException;

/**
 *
 * @author robh
 */
public class ConfigurableBeanValidatorTests extends TestCase {

     public void testSupports() {
        ConfigurableBeanValidator validator = getValidator("fooBean");

        assertTrue("Should support FooBean", validator.supports(FooBean.class));
        assertTrue("Should support String", validator.supports(String.class));
    }

    public void testGetLocale() {
        ConfigurableBeanValidator validator = getValidator("fooBean");
        assertEquals(validator.getLocale(), Locale.getDefault());
    }

    public void testValidate() {
        FooBean bean = new FooBean();
        String formName = "myForm";

        MockControl ctl = MockControl.createControl(ValidatorFactory.class);
        ValidatorFactory factory = (ValidatorFactory)ctl.getMock();

        // expect call to get validator with args "fooBean", bean and null
        factory.getValidator(formName, bean, null);
        ctl.setReturnValue(getCommonsValidator());

        ctl.replay();

        ConfigurableBeanValidator validator = new ConfigurableBeanValidator();
        validator.setValidatorFactory(factory);
        validator.setFormName(formName);
        validator.validate(bean, null);

        ctl.verify();
    }

     public void testValidate_WithMappedProperty_Failure() throws Exception {
        FooBean bean = new FooBean();
        Map attributes = new HashMap();
        bean.setAttributes(attributes);

        BindException errors = new BindException(bean, "fooBeanWithMappedProperty");

        ConfigurableBeanValidator validator = getValidator("fooBeanWithMappedProperty");
        validator.validate(bean, errors);

        assertTrue(errors.hasFieldErrors("attributes[name1]"));
    }

    public void testValidate_WithMappedProperty_Success() throws Exception {
        FooBean bean = new FooBean();
        Map attributes = new HashMap();
        attributes.put("name1", "value1");
        bean.setAttributes(attributes);

        BindException errors = new BindException(bean, "fooBeanWithMappedProperty");

        ConfigurableBeanValidator validator = getValidator("fooBeanWithMappedProperty");
        validator.validate(bean, errors);

        assertFalse(errors.hasFieldErrors("attributes[name1]"));
    }

    private ConfigurableBeanValidator getValidator(String formName) {
        ConfigurableBeanValidator validator = new ConfigurableBeanValidator();
        validator.setFormName(formName);

        DefaultValidatorFactory factory = new DefaultValidatorFactory();
        factory.setValidationConfigLocations(
            new Resource[]{
                new ClassPathResource("testValidation.xml", getClass()),
                new ClassPathResource("validation-rules.xml", getClass())
            }
        );

        validator.setValidatorFactory(factory);
        return validator;
    }

    private Validator getCommonsValidator() {
        ValidatorResources res = new ValidatorResources();
        res.process();
        return new Validator(res);
    }

}
