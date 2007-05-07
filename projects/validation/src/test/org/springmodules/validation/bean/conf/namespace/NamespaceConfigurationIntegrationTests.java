package org.springmodules.validation.bean.conf.namespace;

import junit.framework.TestCase;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.validation.BindException;
import org.springmodules.validation.bean.BeanValidator;
import org.apache.commons.lang.ArrayUtils;

/**
 * Tests for {@link org.springmodules.validation.bean.conf.loader.xml.DefaultXmlBeanValidationConfigurationLoader}.
 *
 * @author Uri Boness
 */
public class NamespaceConfigurationIntegrationTests extends TestCase {

    private BeanValidator validator;

    protected void setUp() throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("validation.xml", getClass());
        validator = (BeanValidator) context.getBean("validator");
    }

    public void testLoadConfiguration() throws Exception {

        Person person = new Person();
        person.setFirstName("Uri");
        person.setLastName("Boness");
        person.setAge(-1);
        person.setEmail("uri@b");
        person.setPassword("pa");
        person.setConfirmPassword("pa1");

        BindException errors = new BindException(person, "person");
        validator.validate(person, errors);

        assertEquals(1, errors.getGlobalErrorCount());

        // testing the custom error code converter
        String[] codes = errors.getGlobalError().getCodes();
        assertTrue(ArrayUtils.contains(codes, "test.passwords.do.not.match"));

        assertEquals(3, errors.getFieldErrorCount());

    }

    public void testLoadConfiguration_WithCustomHandlerFailure() throws Exception {

        Person person = new Person();
        person.setFirstName("Uri");
        person.setLastName("boness");
        person.setAge(-1);
        person.setEmail("uri@b");
        person.setPassword("pa");
        person.setConfirmPassword("pa1");

        BindException errors = new BindException(person, "person");
        validator.validate(person, errors);

        assertEquals(1, errors.getGlobalErrorCount());

        // testing the custom error code converter
        String[] codes = errors.getGlobalError().getCodes();
        assertTrue(ArrayUtils.contains(codes, "test.passwords.do.not.match"));

        assertEquals(4, errors.getFieldErrorCount());

    }

}
