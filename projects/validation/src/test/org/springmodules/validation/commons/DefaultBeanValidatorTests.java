package org.springmodules.validation.commons;

import java.util.Locale;

import junit.framework.TestCase;
import org.easymock.MockControl;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ClassPathResource;
import org.apache.commons.validator.Validator;
import org.apache.commons.validator.ValidatorResources;

/**
 * @author robh
 */
public class DefaultBeanValidatorTests extends TestCase {

    public void testSupports() {
        DefaultBeanValidator validator = getValidator();

        assertTrue(validator.supports(FooBean.class));
        assertFalse(validator.supports(String.class));
    }

    public void testSupportsWithFullyQualifiedName() {
        DefaultBeanValidator validator = getValidator();
        validator.setUseFullyQualifiedClassName(true);

        assertTrue(validator.supports(FooBarBean.class));
        assertFalse(validator.supports(String.class));
    }

    public void testGetLocale() {
        DefaultBeanValidator validator = getValidator();
        assertEquals(validator.getLocale(), Locale.getDefault());
    }

    public void testValidate() {
        FooBean bean = new FooBean();

        MockControl ctl = MockControl.createControl(ValidatorFactory.class);
        ValidatorFactory factory = (ValidatorFactory)ctl.getMock();

        // expect call to get validator with args "fooBean", bean and null
        factory.getValidator("fooBean", bean, null);
        ctl.setReturnValue(getCommonsValidator());

        ctl.replay();

        DefaultBeanValidator validator = new DefaultBeanValidator();
        validator.setValidatorFactory(factory);
        validator.validate(bean, null);

        ctl.verify();
    }

    private DefaultBeanValidator getValidator() {
        DefaultBeanValidator validator = new DefaultBeanValidator();
        DefaultValidatorFactory factory = new DefaultValidatorFactory();
        factory.setValidationConfigLocations(new Resource[]{new ClassPathResource("testValidation.xml", getClass())});
        validator.setValidatorFactory(factory);
        return validator;
    }

    private Validator getCommonsValidator() {
        ValidatorResources res = new ValidatorResources();
        res.process();
        return new Validator(res);
    }
}
