
package org.springmodules.commons.validator;

import java.util.Locale;
import java.util.HashMap;

import junit.framework.TestCase;

import org.springframework.beans.FatalBeanException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.validation.Errors;

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

		Errors errors = new MapException(new HashMap(), "fooBean");
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
