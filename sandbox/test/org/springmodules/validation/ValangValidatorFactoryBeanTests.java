package org.springmodules.validation;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.validation.Validator;

import junit.framework.TestCase;

public class ValangValidatorFactoryBeanTests extends TestCase {

	public ValangValidatorFactoryBeanTests() {
		super();
	}

	public ValangValidatorFactoryBeanTests(String arg0) {
		super(arg0);
	}

	private static ApplicationContext appCtx = new ClassPathXmlApplicationContext("org/springmodules/validation/ValangValidatorFactoryBean-tests.xml");
	
	public void testCustomFunctions() {
		Validator validator = (Validator)appCtx.getBean("testCustomFunctions");
	}
}
