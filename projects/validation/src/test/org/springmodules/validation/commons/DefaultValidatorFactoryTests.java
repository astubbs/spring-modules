package org.springmodules.validation.commons;

import java.util.Locale;

import junit.framework.TestCase;
import org.springframework.beans.FatalBeanException;
import org.springframework.validation.Errors;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ClassPathResource;
import org.easymock.MockControl;
import org.apache.commons.validator.Validator;

/**
 * @author robh
 */
public class DefaultValidatorFactoryTests extends TestCase {

    public void testWithNoValidationConfigLocations() throws Exception {
        try {
            new DefaultValidatorFactory().afterPropertiesSet();
            fail("Should not be able to initialize a DefaultValidatorFactory without any config locations");
        }
        catch (FatalBeanException ex) {
            // success
        }
    }

    public void testSetValidationConfigLocations() throws Exception {
        DefaultValidatorFactory factory = getFactory();

        assertNotNull("ValidatorResources should not be null", factory.getValidatorResources());
    }


    public void testHasRulesForBean() throws Exception {
        DefaultValidatorFactory factory = getFactory();
        assertTrue(factory.hasRulesForBean("fooBean", Locale.getDefault()));
        assertFalse(factory.hasRulesForBean("barBean", Locale.getDefault()));
    }

    public void testGetValidator() throws Exception {
        DefaultValidatorFactory factory = getFactory();

        MockControl control = MockControl.createControl(Errors.class);

        Errors errors = (Errors) control.getMock();
        Object bean = new Object();

        Validator val = factory.getValidator("fooBean", bean, errors);
        assertSame(val.getParameterValue(Validator.BEAN_PARAM), bean);
        assertSame(val.getParameterValue(DefaultValidatorFactory.ERRORS_KEY), errors);
    }

    private DefaultValidatorFactory getFactory() throws Exception {
        DefaultValidatorFactory factory = new DefaultValidatorFactory();

        factory.setValidationConfigLocations(getConfigLocations());

        factory.afterPropertiesSet();
        return factory;
    }

    private Resource[] getConfigLocations() {
        return new Resource[]{new ClassPathResource("testValidation.xml", getClass())};
    }
}
