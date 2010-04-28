package org.springmodules.validation.bean.conf.loader.xml;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import junit.framework.TestCase;
import org.easymock.ArgumentsMatcher;
import org.easymock.MockControl;
import org.springframework.beans.BeanUtils;
import org.springmodules.validation.bean.conf.CascadeValidation;
import org.springmodules.validation.bean.conf.MutableBeanValidationConfiguration;
import org.springmodules.validation.bean.conf.ValidationConfigurationException;
import org.springmodules.validation.bean.conf.loader.xml.handler.ClassValidationElementHandler;
import org.springmodules.validation.bean.conf.loader.xml.handler.PropertyValidationElementHandler;
import org.springmodules.validation.bean.rule.PropertyValidationRule;
import org.springmodules.validation.bean.rule.ValidationMethodValidationRule;
import org.springmodules.validation.bean.rule.ValidationRule;
import org.springmodules.validation.bean.context.ValidationContextUtils;
import org.springmodules.validation.util.cel.ognl.OgnlConditionExpressionParser;
import org.springmodules.validation.util.fel.parser.OgnlFunctionExpressionParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Tests for {@link org.springmodules.validation.bean.conf.loader.xml.DefaultXmlBeanValidationConfigurationLoader}.
 *
 * @author Uri Boness
 */
public class DefaultXmlBeanValidationConfigurationLoaderTests extends TestCase {

    private DefaultXmlBeanValidationConfigurationLoader loader;

    private MockControl registryControl;

    private ValidationRuleElementHandlerRegistry registry;

    private MockControl configurationControl;

    private MutableBeanValidationConfiguration configuration;

    private MockControl propertyHandlerControl;

    private PropertyValidationElementHandler propertyHandler;

    private MockControl classHandlerControl;

    private ClassValidationElementHandler classHandler;

    private MockControl ruleControl;

    private ValidationRule rule;

    protected void setUp() throws Exception {

        registryControl = MockControl.createControl(ValidationRuleElementHandlerRegistry.class);
        registry = (ValidationRuleElementHandlerRegistry) registryControl.getMock();

        configurationControl = MockControl.createControl(MutableBeanValidationConfiguration.class);
        configuration = (MutableBeanValidationConfiguration) configurationControl.getMock();

        ruleControl = MockControl.createControl(ValidationRule.class);
        rule = (ValidationRule) ruleControl.getMock();

        propertyHandlerControl = MockControl.createControl(PropertyValidationElementHandler.class);
        propertyHandler = (PropertyValidationElementHandler) propertyHandlerControl.getMock();

        classHandlerControl = MockControl.createControl(ClassValidationElementHandler.class);
        classHandler = (ClassValidationElementHandler) classHandlerControl.getMock();

        loader = new DefaultXmlBeanValidationConfigurationLoader(registry);
    }

    public void testHandlePropertyDefinition_WithNoRulesAndNoCascading() throws Exception {
        Element propertyDefinition = element("property",
            new String[]{"name"},
            new String[]{"name"}
        );

        replay();
        loader.handlePropertyDefinition(propertyDefinition, Object.class, configuration);
        verify();
    }

    public void testHandlePropertyDefinition_WithNoRulesButWithCascading() throws Exception {
        Element propertyDefinition = element("property",
            new String[]{"name", "cascade"},
            new String[]{"name", "true"}
        );

        configuration.addCascadeValidation(new CascadeValidation("name"));

        replay();
        loader.handlePropertyDefinition(propertyDefinition, TestBean.class, configuration);
        verify();
    }

    public void testHandlePropertyDefinition_WithARuleAndCascading() throws Exception {
        Document document = document();
        Element propertyDefinition = element(document, "property",
            new String[]{"name", "cascade"},
            new String[]{"name", "true"}
        );
        Element ruleDefinition = element(document, "rule");
        propertyDefinition.appendChild(ruleDefinition);

        PropertyDescriptor descriptor = BeanUtils.getPropertyDescriptor(TestBean.class, "name");

        configuration.addCascadeValidation(new CascadeValidation("name"));
        registryControl.expectAndReturn(registry.findPropertyHandler(ruleDefinition, TestBean.class, descriptor), propertyHandler);
        propertyHandler.handle(ruleDefinition, "name", configuration);

        final PropertyValidationRule propertyRule = new PropertyValidationRule("name", rule);
        loader = new DefaultXmlBeanValidationConfigurationLoader(registry) {
            protected PropertyValidationRule createPropertyRule(String propertyName, ValidationRule rule) {
                return propertyRule;
            }
        };

        replay();
        loader.handlePropertyDefinition(propertyDefinition, TestBean.class, configuration);
        verify();
    }

    public void testHandlePropertyDefinition_WithoutAppropriateHandler() throws Exception {
        Document document = document();
        Element propertyDefinition = element(document, "property",
            new String[]{"name", "cascade"},
            new String[]{"name", "true"}
        );
        Element ruleDefinition = element(document, "rule");
        propertyDefinition.appendChild(ruleDefinition);

        PropertyDescriptor descriptor = BeanUtils.getPropertyDescriptor(TestBean.class, "name");

        configuration.addCascadeValidation(new CascadeValidation("name"));
        registryControl.expectAndReturn(registry.findPropertyHandler(ruleDefinition, TestBean.class, descriptor), null);

        replay();

        try {
            loader.handlePropertyDefinition(propertyDefinition, TestBean.class, configuration);
            fail("An ValidationConfigurationException is expected to be thrown if of the configured rules has no handler");
        } catch (ValidationConfigurationException xce) {
            // expected
        }

        verify();
    }

    public void testHandleGlobalDefinition_WithNoRules() throws Exception {
        Element globalDefinition = element("global");

        replay();
        loader.handleGlobalDefinition(globalDefinition, TestBean.class, configuration);
        verify();
    }

    public void testHandleGlobalDefinition_WithARule() throws Exception {
        Document document = document();
        Element globalDefinition = element(document, "global");
        Element ruleDefinition = element(document, "rule");
        globalDefinition.appendChild(ruleDefinition);

        registryControl.expectAndReturn(registry.findClassHandler(ruleDefinition, TestBean.class), classHandler);
        classHandler.handle(ruleDefinition, configuration);

        replay();
        loader.handleGlobalDefinition(globalDefinition, TestBean.class, configuration);
        verify();
    }

    public void testHandleGlobalDefinition_WithoutAppropriateHandler() throws Exception {
        Document document = document();
        Element globalDefinition = element(document, "global");
        Element ruleDefinition = element(document, "rule");
        globalDefinition.appendChild(ruleDefinition);

        registryControl.expectAndReturn(registry.findClassHandler(ruleDefinition, TestBean.class), null);

        replay();

        try {
            loader.handleGlobalDefinition(globalDefinition, TestBean.class, configuration);
            fail("An ValidationConfigurationException is expected to be thrown if no proper element handler " +
                "could be found for a rule definition");
        } catch (ValidationConfigurationException xce) {
            // expected
        }

        verify();
    }

    public void testHandleMethodDefinition() throws Exception {
        Document document = document();
        Element methodElement = element(document, "method",
            new String[]{"name", "code", "message", "args", "apply-if"},
            new String[]{"validate", "thecode", "themessage", "a, b, c", "true"}
        );

        loader = new DefaultXmlBeanValidationConfigurationLoader() {
            protected ValidationMethodValidationRule createMethodValidationRule(
                Class clazz,
                String methodName,
                String errorCode,
                String message,
                String argsString,
                String contextsString,
                String applyIfString) {

                assertEquals(TestBean.class, clazz);
                assertEquals("validate", methodName);
                assertEquals("thecode", errorCode);
                assertEquals("themessage", message);
                assertEquals("a, b, c", argsString);
                assertEquals("true", applyIfString);
                assertEquals("", contextsString);
                return super.createMethodValidationRule(clazz, methodName, errorCode, message, argsString, contextsString, applyIfString);
            }
        };
        loader.setFunctionExpressionParser(new OgnlFunctionExpressionParser());
        loader.setConditionExpressionParser(new OgnlConditionExpressionParser());
        loader.handleMethodDefinition(methodElement, TestBean.class, configuration);
    }

    public void testHandleMethodDefinition_WithProperty() throws Exception {
        Document document = document();
        Element methodElement = element(document, "method",
            new String[]{"name", "for-property", "code", "message", "args", "contexts", "apply-if"},
            new String[]{"validate", "name", "thecode", "themessage", "a, b, c", "bla", "true"}
        );

        // a hack to ensure the createMethodValidationRule method
        // is actually called. In the call an element is added to
        // this list and after the test this list is asserted to be
        // non-empty
        final List list = new ArrayList();

        loader = new DefaultXmlBeanValidationConfigurationLoader() {
            protected ValidationMethodValidationRule createMethodValidationRule(
                Class clazz,
                String methodName,
                String errorCode,
                String message,
                String argsString,
                String contextStrings,
                String applyIfString) {

                list.add("entered");
                assertEquals(TestBean.class, clazz);
                assertEquals("validate", methodName);
                assertEquals("thecode", errorCode);
                assertEquals("themessage", message);
                assertEquals("a, b, c", argsString);
                assertEquals("bla", contextStrings);
                assertEquals("true", applyIfString);
                return super.createMethodValidationRule(clazz, methodName, errorCode, message, argsString, contextStrings, applyIfString);
            }
        };

        configuration.addPropertyRule("name", null);
        configurationControl.setMatcher(new ArgumentsMatcher() {
            public boolean matches(Object[] objects, Object[] objects1) {
                return objects.length == 2 && objects[0].equals(objects1[0]);
            }

            public String toString(Object[] objects) {
                return "";
            }
        });

        loader.setFunctionExpressionParser(new OgnlFunctionExpressionParser());
        loader.setConditionExpressionParser(new OgnlConditionExpressionParser());

        replay();
        loader.handleMethodDefinition(methodElement, TestBean.class, configuration);
        verify();

        assertFalse("createMethodValidationRule on configuration was not called", list.isEmpty());
    }

    public void testCreateMethodValidationRule() throws Exception {
        loader.setFunctionExpressionParser(new OgnlFunctionExpressionParser());
        loader.setConditionExpressionParser(new OgnlConditionExpressionParser());
        ValidationMethodValidationRule rule = loader.createMethodValidationRule(TestBean.class, "validate", "code", "message", "'a', 'b', 'c'", "bla", "true");
        TestBean testBean = new TestBean() {
            public boolean validate() {
                return false;
            }
        };

        assertEquals("code", rule.getErrorCode());
        assertEquals("message", rule.getDefaultErrorMessage());
        ValidationContextUtils.setContext("bla");
        assertTrue(rule.isApplicable(testBean));
        ValidationContextUtils.clearContext();

        Object[] args = rule.getErrorArguments(testBean);
        assertEquals(3, args.length);
        assertEquals(new Character('a'), args[0]);
        assertEquals(new Character('b'), args[1]);
        assertEquals(new Character('c'), args[2]);

        assertFalse(rule.getCondition().check(testBean));

    }

    //=============================================== Helper Methods ===================================================

    protected void replay() {
        registryControl.replay();
        configurationControl.replay();
        propertyHandlerControl.replay();
        classHandlerControl.replay();
        ruleControl.replay();
    }

    protected void verify() {
        registryControl.verify();
        configurationControl.verify();
        propertyHandlerControl.verify();
        classHandlerControl.verify();
        ruleControl.verify();
    }

    protected Document document() throws Exception {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        return builder.newDocument();
    }

    protected Element element(Document doc, String name) throws Exception {
        return doc.createElement(name);
    }

    protected Element element(String name) throws Exception {
        return element(name, new String[0], new String[0]);
    }

    protected Element element(String name, String[] attrNames, String[] attrValues) throws Exception {
        return element(document(), name, attrNames, attrValues);
    }

    protected Element element(Document doc, String name, String[] attrNames, String[] attrValues) throws Exception {
        if (attrNames.length != attrValues.length) {
            throw new IllegalArgumentException("attribute names and attribute values have different sizes");
        }
        Element element = doc.createElement(name);
        for (int i = 0; i < attrNames.length; i++) {
            element.setAttribute(attrNames[i], attrValues[i]);
        }
        return element;
    }

    //=============================================== Inner Classes ===================================================

    public class TestBean {

        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean validate() {
            return true;
        }

    }

}