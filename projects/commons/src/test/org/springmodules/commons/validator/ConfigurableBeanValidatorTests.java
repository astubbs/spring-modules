
package org.springmodules.commons.validator;

import java.util.Locale;

import junit.framework.TestCase;
import org.easymock.MockControl;
import org.apache.commons.validator.Validator;
import org.apache.commons.validator.ValidatorResources;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ClassPathResource;

/**
 * 
 * @author robh
 */
public class ConfigurableBeanValidatorTests extends TestCase {

	 public void testSupports() {
		ConfigurableBeanValidator validator = getValidator();

		assertTrue("Should support FooBean", validator.supports(FooBean.class));
		assertTrue("Should support String", validator.supports(String.class));
	}

	public void testGetLocale() {
		ConfigurableBeanValidator validator = getValidator();
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

	private ConfigurableBeanValidator getValidator() {
		ConfigurableBeanValidator validator = new ConfigurableBeanValidator();
		validator.setFormName("fooBean");

		DefaultValidatorFactory factory = new DefaultValidatorFactory();
		factory.setValidationConfigLocations(new Resource[]{new ClassPathResource("testValidation.xml", getClass())});

		validator.setValidatorFactory(factory);
		return validator;
	}

	private Validator getCommonsValidator() {
		ValidatorResources res = new ValidatorResources();
		return new Validator(res);
	}

}
