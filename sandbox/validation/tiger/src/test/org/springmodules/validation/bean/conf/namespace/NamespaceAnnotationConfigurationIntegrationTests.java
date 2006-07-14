package org.springmodules.validation.bean.conf.namespace;

import junit.framework.TestCase;
import org.springmodules.validation.bean.BeanValidator;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.validation.BindException;

/**
 * Tests for {@link org.springmodules.validation.bean.conf.xml.DefaultXmlBeanValidationConfigurationLoader}.
 *
 * @author Uri Boness
 */
public class NamespaceAnnotationConfigurationIntegrationTests extends TestCase {

    private BeanValidator validator;

    protected void setUp() throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("annotation-validation.xml", getClass());
        validator = (BeanValidator)context.getBean("validator");
    }

    public void testLoadConfiguration() throws Exception {

        AnnotatedPerson person = new AnnotatedPerson();
        person.setFirstName("Uri");
        person.setLastName("Boness");
        person.setAge(-1);
        person.setEmail("uri@b");
        person.setPassword("pa");
        person.setConfirmPassword("pa1");

        BindException errors = new BindException(person, "person");
        validator.validate(person, errors);

        assertEquals(1, errors.getGlobalErrorCount());
        assertEquals(3, errors.getFieldErrorCount());

    }

    public void testLoadConfiguration_WithCustomHandlerFailure() throws Exception {

        AnnotatedPerson person = new AnnotatedPerson();
        person.setFirstName("Uri");
        person.setLastName("boness");
        person.setAge(-1);
        person.setEmail("uri@b");
        person.setPassword("pa");
        person.setConfirmPassword("pa1");

        BindException errors = new BindException(person, "person");
        validator.validate(person, errors);

        assertEquals(1, errors.getGlobalErrorCount());
        assertEquals(4, errors.getFieldErrorCount());

    }

}
