package org.springmodules.validation.bean.conf.loader.xml;

import java.beans.PropertyDescriptor;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import junit.framework.TestCase;
import org.easymock.MockControl;
import org.springframework.beans.BeanUtils;
import org.springmodules.validation.bean.conf.CascadeValidation;
import org.springmodules.validation.bean.conf.MutableBeanValidationConfiguration;
import org.springmodules.validation.bean.rule.PropertyValidationRule;
import org.springmodules.validation.bean.rule.ValidationRule;
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
        registry = (ValidationRuleElementHandlerRegistry)registryControl.getMock();

        configurationControl = MockControl.createControl(MutableBeanValidationConfiguration.class);
        configuration = (MutableBeanValidationConfiguration)configurationControl.getMock();

        ruleControl = MockControl.createControl(ValidationRule.class);
        rule = (ValidationRule)ruleControl.getMock();

        propertyHandlerControl = MockControl.createControl(PropertyValidationElementHandler.class);
        propertyHandler = (PropertyValidationElementHandler)propertyHandlerControl.getMock();

        classHandlerControl = MockControl.createControl(ClassValidationElementHandler.class);
        classHandler = (ClassValidationElementHandler)classHandlerControl.getMock();

        loader = new DefaultXmlBeanValidationConfigurationLoader(registry);
    }

    public void testHandlePropertyDefinition_WithNoRulesAndNoCascading() throws Exception {
        Element propertyDefinition = element("property",
            new String[] { "name" },
            new String[] { "name" }
        );

        replay();
        loader.handlePropertyDefinition(propertyDefinition, Object.class, configuration);
        verify();
    }

    public void testHandlePropertyDefinition_WithNoRulesButWithCascading() throws Exception {
        Element propertyDefinition = element("property",
            new String[] { "name", "cascade" },
            new String[] { "name", "true" }
        );

        configuration.addCascadeValidation(new CascadeValidation("name"));

        replay();
        loader.handlePropertyDefinition(propertyDefinition, TestBean.class, configuration);
        verify();
    }

    public void testHandlePropertyDefinition_WithARuleAndCascading() throws Exception {
        Document document = document();
        Element propertyDefinition = element(document, "property",
            new String[] { "name", "cascade" },
            new String[] { "name", "true" }
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
            new String[] { "name", "cascade" },
            new String[] { "name", "true" }
        );
        Element ruleDefinition = element(document, "rule");
        propertyDefinition.appendChild(ruleDefinition);

        PropertyDescriptor descriptor = BeanUtils.getPropertyDescriptor(TestBean.class, "name");

        configuration.addCascadeValidation(new CascadeValidation("name"));
        registryControl.expectAndReturn(registry.findPropertyHandler(ruleDefinition, TestBean.class, descriptor), null);

        replay();

        try {
            loader.handlePropertyDefinition(propertyDefinition, TestBean.class, configuration);
            fail("An XmlConfigurationException is expected to be thrown if of the configured rules has no handler");
        } catch (XmlConfigurationException xce) {
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
            fail("An XmlConfigurationException is expected to be thrown if no proper element handler " +
                "could be found for a rule definition");
        } catch (XmlConfigurationException xce) {
            // expected
        }

        verify();
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
        for (int i=0; i<attrNames.length; i++) {
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

    }

}