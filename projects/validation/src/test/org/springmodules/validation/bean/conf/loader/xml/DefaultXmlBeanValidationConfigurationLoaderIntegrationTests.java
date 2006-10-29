package org.springmodules.validation.bean.conf.loader.xml;

import junit.framework.TestCase;
import org.springframework.core.io.ClassPathResource;
import org.springframework.validation.BindException;
import org.springmodules.validation.bean.BeanValidator;
import org.springmodules.validation.bean.conf.BeanValidationConfiguration;
import org.springmodules.validation.util.cel.ognl.OgnlConditionExpressionParser;
import org.springmodules.validation.util.fel.parser.OgnlFunctionExpressionParser;

/**
 * Tests for {@link org.springmodules.validation.bean.conf.loader.xml.DefaultXmlBeanValidationConfigurationLoader}.
 *
 * @author Uri Boness
 */
public class DefaultXmlBeanValidationConfigurationLoaderIntegrationTests extends TestCase {

    private DefaultXmlBeanValidationConfigurationLoader loader;

    protected void setUp() throws Exception {
        loader = new DefaultXmlBeanValidationConfigurationLoader();
        loader.setResource(new ClassPathResource("Person.vld.xml", getClass()));
        loader.setConditionExpressionParser(new OgnlConditionExpressionParser());
        loader.setFunctionExpressionParser(new OgnlFunctionExpressionParser());
        loader.afterPropertiesSet();
    }

    public void testLoadConfiguration() throws Exception {
        BeanValidationConfiguration config = loader.loadConfiguration(Person.class);
        assertEquals(2, config.getGlobalRules().length);

        Person person = new Person();
        person.setFirstName("Uri");
        person.setLastName("Boness");
        person.setAge(-1);
        person.setEmail("uri@b");
        person.setPassword("pa");
        person.setConfirmPassword("pa1");

        BindException errors = new BindException(person, "person");
        BeanValidator validator = new BeanValidator(loader);
        validator.validate(person, errors);

        assertEquals(2, errors.getGlobalErrorCount());
        assertEquals(1, errors.getFieldErrorCount("smallInteger"));
        assertEquals(1, errors.getFieldErrorCount("lastName"));
        assertEquals("Person.lastName[validateLastNameIsLongerThanTen()]", errors.getFieldError("lastName").getCode());

    }

    public void testLoadDefaultConfiguration() throws Exception {
        loader = new DefaultXmlBeanValidationConfigurationLoader();
        loader.afterPropertiesSet();
        BeanValidationConfiguration conf = loader.loadConfiguration(Person.class);
        assertNotNull(conf);
    }

}