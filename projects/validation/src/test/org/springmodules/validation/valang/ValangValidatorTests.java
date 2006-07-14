package org.springmodules.validation.valang;

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

	private static ApplicationContext appCtx = new ClassPathXmlApplicationContext("org/springmodules/validation/valang/ValangValidator-tests.xml");
	
	public void testCustomFunctions() {

        LifeCycleBean lifeCycleBean = new LifeCycleBean();
        Person person = new Person();
        person.setLifeCycleBean(lifeCycleBean);
        person.setFirstName("FN");

        Validator validator = (Validator)appCtx.getBean("testCustomFunctions");
        BindException errors = new BindException(person, "person");
        validator.validate(person, errors);

        assertTrue(lifeCycleBean.isApplicationContextSet());
        assertTrue(lifeCycleBean.isApplicationEventPublisher());
        assertTrue(lifeCycleBean.isBeanFactorySet());
        assertTrue(lifeCycleBean.isMessageSourceSet());
        assertTrue(lifeCycleBean.isResourceLoaderSet());

        assertTrue(errors.hasFieldErrors("firstName"));
    }

    public void testCustomFunctionsFromApplicationContext() {

        LifeCycleBean lifeCycleBean = new LifeCycleBean();
        Person person = new Person();
        person.setLifeCycleBean(lifeCycleBean);
        person.setFirstName("FN");

        Validator validator = (Validator)appCtx.getBean("testCustomFunctionsFromApplicationContext");
        BindException errors = new BindException(person, "person");
        validator.validate(person, errors);

        assertTrue(lifeCycleBean.isApplicationContextSet());
        assertTrue(lifeCycleBean.isApplicationEventPublisher());
        assertTrue(lifeCycleBean.isBeanFactorySet());
        assertTrue(lifeCycleBean.isMessageSourceSet());
        assertTrue(lifeCycleBean.isResourceLoaderSet());

        assertTrue(errors.hasFieldErrors("firstName"));
    }

    public void testPersonValidator() {
		Validator validator = (Validator)appCtx.getBean("personValidator", Validator.class);
		Person person = new Person();
		Map map = new HashMap();
		map.put("name", "Steven");
		map.put("passwd", "pas");
		person.setForm(map);
		Errors errors = new BindException(person, "person");
		validator.validate(person, errors);

        assertFalse(errors.hasFieldErrors("form[name]"));
        assertTrue(errors.hasFieldErrors("form[passwd]"));
    }


    //================================================ Inner Classes ===================================================

    private class Person {

        private LifeCycleBean lifeCycleBean;
        private String firstName;

        private Map form;

        public Map getForm() {
            return form;
        }

        public void setForm(Map form) {
            this.form = form;
        }

        public LifeCycleBean getLifeCycleBean() {
            return lifeCycleBean;
        }

        public void setLifeCycleBean(LifeCycleBean lifeCycleBean) {
            this.lifeCycleBean = lifeCycleBean;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }
    }
	
}
