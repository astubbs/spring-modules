package org.springmodules.validation.valang;

import junit.framework.TestCase;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class ValangValidatorFactoryBeanTests extends TestCase {

    private static ApplicationContext appCtx = new ClassPathXmlApplicationContext("org/springmodules/validation/valang/ValangValidatorFactoryBean-tests.xml");

    public void testCustomFunctions() {
        Validator validator = (Validator)appCtx.getBean("testCustomFunctions");
        Bean bean = new Bean();


        Errors errors = new BindException(bean, "bean");
        validator.validate(bean, errors);
        LifeCycleBean lifeCycleBean = bean.getLifeCycleBean();

        assertFalse(errors.hasErrors());
        assertTrue(lifeCycleBean.isApplicationContextSet());
        assertTrue(lifeCycleBean.isApplicationEventPublisher());
        assertTrue(lifeCycleBean.isBeanFactorySet());
        assertTrue(lifeCycleBean.isInitCalled());
        assertTrue(lifeCycleBean.isMessageSourceSet());
        assertTrue(lifeCycleBean.isResourceLoaderSet());
        assertTrue(lifeCycleBean.isServletContextSet());
        assertEquals(2, lifeCycleBean.getPatternSetCount());
    }

    public static class Bean {
        private String firstName = "Steven";
        private LifeCycleBean lifeCycleBean = new LifeCycleBean();

        public String getFirstName() {
            return firstName;
        }
        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }
        public LifeCycleBean getLifeCycleBean() {
            return lifeCycleBean;
        }
        public void setLifeCycleBean(LifeCycleBean lifeCycleBean) {
            this.lifeCycleBean = lifeCycleBean;
        }
    }
}
