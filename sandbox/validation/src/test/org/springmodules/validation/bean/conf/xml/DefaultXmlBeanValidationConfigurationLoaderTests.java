package org.springmodules.validation.bean.conf.xml;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import junit.framework.TestCase;
import org.easymock.MockControl;
import org.springmodules.validation.bean.conf.MutableBeanValidationConfiguration;
import org.springmodules.validation.bean.rule.PropertyValidationRule;
import org.springmodules.validation.bean.rule.ValidationRule;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Tests for {@link org.springmodules.validation.bean.conf.xml.DefaultXmlBeanValidationConfigurationLoader}.
 *
 * @author Uri Boness
 */
public class DefaultXmlBeanValidationConfigurationLoaderTests extends TestCase {

    private DefaultXmlBeanValidationConfigurationLoader loader;

    private MockControl registryControl;
    private ValidationRuleElementHandlerRegistry registry;

    private MockControl configurationControl;
    private MutableBeanValidationConfiguration configuration;

    private MockControl handlerControl;
    private ValidationRuleElementHandler handler;

    private MockControl ruleControl;
    private ValidationRule rule;

    protected void setUp() throws Exception {

        registryControl = MockControl.createControl(ValidationRuleElementHandlerRegistry.class);
        registry = (ValidationRuleElementHandlerRegistry)registryControl.getMock();

        configurationControl = MockControl.createControl(MutableBeanValidationConfiguration.class);
        configuration = (MutableBeanValidationConfiguration)configurationControl.getMock();

        ruleControl = MockControl.createControl(ValidationRule.class);
        rule = (ValidationRule)ruleControl.getMock();

        handlerControl = MockControl.createControl(ValidationRuleElementHandler.class);
        handler = (ValidationRuleElementHandler)handlerControl.getMock();

        loader = new DefaultXmlBeanValidationConfigurationLoader(registry);
    }

    public void testHandlePropertyDefinition_WithNoRulesAndNoCascading() throws Exception {
        Element propertyDefinition = element("property",
            new String[] { "name" },
            new String[] { "name" }
        );

        replay();
        loader.handlePropertyDefinition(propertyDefinition, configuration);
        verify();
    }

    public void testHandlePropertyDefinition_WithNoRulesButWithCascading() throws Exception {
        Element propertyDefinition = element("property",
            new String[] { "name", "valid" },
            new String[] { "name", "true" }
        );

        configuration.addRequiredValidatableProperty("name");

        replay();
        loader.handlePropertyDefinition(propertyDefinition, configuration);
        verify();
    }

    public void testHandlePropertyDefinition_WithARulesAndCascading() throws Exception {
        Document document = document();
        Element propertyDefinition = element(document, "property",
            new String[] { "name", "valid" },
            new String[] { "name", "true" }
        );
        Element ruleDefinition = element(document, "rule");
        propertyDefinition.appendChild(ruleDefinition);

        configuration.addRequiredValidatableProperty("name");
        registryControl.expectAndReturn(registry.findHandler(ruleDefinition), handler);
        handlerControl.expectAndReturn(handler.handle(ruleDefinition), rule);
        handlerControl.expectAndReturn(handler.isAlwaysGlobal(), false);

        final PropertyValidationRule propertyRule = new PropertyValidationRule("name", rule);
        loader = new DefaultXmlBeanValidationConfigurationLoader(registry) {
            protected PropertyValidationRule createPropertyRule(String propertyName, ValidationRule rule) {
                return propertyRule;
            }
        };

        configuration.addPropertyRule("name", propertyRule);

        replay();
        loader.handlePropertyDefinition(propertyDefinition, configuration);
        verify();
    }

    public void testHandlePropertyDefinition_WithoutAppropriateHandler() throws Exception {
        Document document = document();
        Element propertyDefinition = element(document, "property",
            new String[] { "name", "valid" },
            new String[] { "name", "true" }
        );
        Element ruleDefinition = element(document, "rule");
        propertyDefinition.appendChild(ruleDefinition);

        configuration.addRequiredValidatableProperty("name");
        registryControl.expectAndReturn(registry.findHandler(ruleDefinition), null);

        replay();

        try {
            loader.handlePropertyDefinition(propertyDefinition, configuration);
            fail("An XmlConfigurationException is expected to be thrown if of the configured rules has no handler");
        } catch (XmlConfigurationException xce) {
            // expected
        }

        verify();
    }

    public void testHandleGlobalDefinition_WithNoRules() throws Exception {
        Element globalDefinition = element("global");

        replay();
        loader.handleGlobalDefinition(globalDefinition, configuration);
        verify();
    }

    public void testHandleGlobalDefinition_WithARule() throws Exception {
        Document document = document();
        Element globalDefinition = element(document, "global");
        Element ruleDefinition = element(document, "rule");
        globalDefinition.appendChild(ruleDefinition);

        configuration.addGlobalRule(rule);
        registryControl.expectAndReturn(registry.findHandler(ruleDefinition), handler);
        handlerControl.expectAndReturn(handler.handle(ruleDefinition), rule);

        replay();
        loader.handleGlobalDefinition(globalDefinition, configuration);
        verify();
    }

    public void testHandleGlobalDefinition_WithoutAppropriateHandler() throws Exception {
        Document document = document();
        Element globalDefinition = element(document, "global");
        Element ruleDefinition = element(document, "rule");
        globalDefinition.appendChild(ruleDefinition);

        registryControl.expectAndReturn(registry.findHandler(ruleDefinition), null);

        replay();

        try {
            loader.handleGlobalDefinition(globalDefinition, configuration);
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
        handlerControl.replay();
        ruleControl.replay();
    }

    protected void verify() {
        registryControl.verify();
        configurationControl.verify();
        handlerControl.verify();
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

}