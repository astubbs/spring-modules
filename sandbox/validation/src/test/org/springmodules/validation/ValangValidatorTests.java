package org.springmodules.validation;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

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
	
	public void testPersonValidator() {
		class Person {
			private Map form;

			public Map getForm() {
				return form;
			}

			public void setForm(Map form) {
				this.form = form;
			}		
		}
		
		Validator validator = (Validator)appCtx.getBean("personValidator", Validator.class);
		Person person = new Person();
		Map map = new HashMap();
		map.put("name", "Steven");
		map.put("passwd", "password");
		person.setForm(map);
		Errors errors = new BindException(person, "person");
		validator.validate(person, errors);
	}
	
	
}
