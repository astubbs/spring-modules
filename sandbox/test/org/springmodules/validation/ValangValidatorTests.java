package org.springmodules.validation;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.validation.Validator;

import junit.framework.TestCase;

public class ValangValidatorTests extends TestCase {

	public ValangValidatorTests() {
		super();
	}

	public ValangValidatorTests(String arg0) {
		super(arg0);
	}

	private static ApplicationContext appCtx = new ClassPathXmlApplicationContext("org/springmodules/validation/ValangValidator-tests.xml");
	
	public void testCustomFunctions() {
		Validator validator = (Validator)appCtx.getBean("testCustomFunctions");
	}
}
