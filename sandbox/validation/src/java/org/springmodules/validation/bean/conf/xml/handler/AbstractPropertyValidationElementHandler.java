/*
 * Copyright 2004-2005 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springmodules.validation.bean.conf.xml.handler;

import java.beans.PropertyDescriptor;

import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springmodules.validation.bean.conf.MutableBeanValidationConfiguration;
import org.springmodules.validation.bean.conf.xml.PropertyValidationElementHandler;
import org.springmodules.validation.bean.rule.DefaultValidationRule;
import org.springmodules.validation.bean.rule.ErrorArgumentsResolver;
import org.springmodules.validation.bean.rule.ValidationRule;
import org.springmodules.validation.bean.rule.PropertyValidationRule;
import org.springmodules.validation.bean.rule.resolver.ExpressionErrorArgumentsResolver;
import org.springmodules.validation.util.bel.BeanExpressionResolver;
import org.springmodules.validation.util.bel.BeanExpressionResolverAware;
import org.springmodules.validation.util.condition.Condition;
import org.springmodules.validation.util.condition.common.AlwaysTrueCondition;
import org.springmodules.validation.util.condition.parser.ConditionParser;
import org.springmodules.validation.util.condition.parser.ConditionParserAware;
import org.springmodules.validation.util.condition.parser.valang.ValangConditionParser;
import org.w3c.dom.Element;

/**
 * A base class for common {@link org.springmodules.validation.bean.conf.xml.PropertyValidationElementHandler}
 * implementations that represent validation rules. This base handler idetifies the supported elements by their
 * tag names (qualified and local). In addition, it assumes the following common attributes:
 *
 * <ul>
 *  <li>code - Indicates the error code of the validatoin rule</li>
 *  <li>message - The default error message of the validation rule</li>
 *  <li>args - A comma separated list of error arguments</li>
 *  <li>apply-if - An condition expression that determines the applicability of the validation rule</li>
 * </ul>
 *
 * Note: The apply-if attribute is being parsed by the
 * {@link org.springmodules.validation.util.condition.parser.ConditionParser} that is associated with this handler. It
 * uses {@link org.springmodules.validation.util.condition.parser.valang.ValangConditionParser} by default.
 *
 * @author Uri Boness
 */
public abstract class AbstractPropertyValidationElementHandler
    implements PropertyValidationElementHandler, ConditionParserAware, BeanExpressionResolverAware {

    private static final String ERROR_CODE_ATTR = "code";
    private static final String MESSAGE_ATTR = "message";
    private static final String ARGS_ATTR = "args";
    private static final String APPLY_IF_ATTR = "apply-if";

    private static final ConditionParser DEFAULT_CONDITION_PARSER = new ValangConditionParser();

    private String elementName;
    private String namespaceUrl;
    private ConditionParser conditionParser;
    private BeanExpressionResolver expressionResolver;

    /**
     * Constructs a new AbstractPropertyValidationElementHandler with given supported element name.
     *
     * @param elementName The supported element name.
     */
    public AbstractPropertyValidationElementHandler(String elementName) {
        this(elementName, null);
    }

    /**
     * Constructs a new AbstractPropertyValidationElementHandler with given supported element name and namespace.
     *
     * @param elementName The supported element name.
     * @param namespace The supported namespace.
     */
    public AbstractPropertyValidationElementHandler(String elementName, String namespace) {
        this(elementName, namespace, AbstractPropertyValidationElementHandler.DEFAULT_CONDITION_PARSER);
    }

    /**
     * Constructs a new AbstractPropertyValidationElementHandler with given supported element name and namespace and a
     * condition handler to parse the <code>apply-if</code> expressions.
     *
     * @param elementName The supported element name.
     * @param namespace The supported namespace.
     * @param conditionParser The condition handler to be used when parsing the <code>apply-if</code> expression.
     */
    public AbstractPropertyValidationElementHandler(String elementName, String namespace, ConditionParser conditionParser) {
        this.elementName = elementName;
        this.namespaceUrl = namespace;
        this.conditionParser = conditionParser;
    }

    /**
     * Determines whether the given element is supported by this handler. The check is done by comparing the element
     * tag name and namespace with the ones that are configured with this handler.
     *
     * @see org.springmodules.validation.bean.conf.xml.PropertyValidationElementHandler#supports(org.w3c.dom.Element, Class, java.beans.PropertyDescriptor)
     */
    public boolean supports(Element element, Class clazz, PropertyDescriptor descriptor) {
        String localName = element.getLocalName();
        if (!localName.equals(elementName)) {
            return false;
        }
        String ns = element.getNamespaceURI();
        return ObjectUtils.nullSafeEquals(ns, namespaceUrl);
    }

    /**
     * Creates the appropriate {@link org.springmodules.validation.bean.rule.ValidationRule} based on the given element
     * and adds it to the given configuration.
     *
     * @see AbstractPropertyValidationElementHandler#handle(org.w3c.dom.Element, String, org.springmodules.validation.bean.conf.MutableBeanValidationConfiguration)
     */
    public void handle(Element element, String propertyName, MutableBeanValidationConfiguration configuration) {
        String errorCode = extractErrorCode(element);
        String message = extractMessage(element);
        ErrorArgumentsResolver argumentsResolver = extractArgumentsResolver(element);
        Condition condition = extractCondition(element);
        Condition applicabilityCondition = extractApplicabilityCondition(element);
        ValidationRule rule = new DefaultValidationRule(condition, applicabilityCondition, errorCode, message, argumentsResolver);
        if (isConditionGloballyScoped(element)) {
            configuration.addPropertyRule(propertyName, rule);
        } else {
            configuration.addPropertyRule(propertyName, new PropertyValidationRule(propertyName, rule));
        }

    }

    /**
     * By default the element handlers handle and produce rules that can be associated with both global and non-global
     * contexts.
     *
     * @see org.springmodules.validation.bean.conf.xml.ValidationRuleElementHandler#isAlwaysGlobal()
     */
    public boolean isConditionGloballyScoped(Element element) {
        return false;
    }

    /**
     * Extracts the validation rule error code from the given element. Expects a "code" attribute to indicate the
     * error code. If no such attribute exisits, returns {@link #getDefaultErrorCode(org.w3c.dom.Element)} instead.
     *
     * @param element The element that represents the validation rule.
     * @return The validation rule error code.
     */
    protected String extractErrorCode(Element element) {
        String code = element.getAttribute(AbstractPropertyValidationElementHandler.ERROR_CODE_ATTR);
        return (StringUtils.hasText(code)) ? code : getDefaultErrorCode(element);
    }

    /**
     * Extracts the validation rule error message from the given element. Expects a "message" attribute to indicate the
     * error message. If no such attribute exisits, returns {@link #extractErrorCode(org.w3c.dom.Element)} instead.
     *
     * @param element The element that represents the validation rule.
     * @return The validation rule error message.
     */
    protected String extractMessage(Element element) {
        return element.getAttribute(AbstractPropertyValidationElementHandler.MESSAGE_ATTR);
    }

    /**
     * Extracts the validation rule error arguments from the given element. Expects an "args" attribute to indicate the
     * error arguments. If no such attribute exisits, returns an empty array.
     *
     * @param element The element that represents the validation rule.
     * @return The validation rule error arguments.
     */
    protected ErrorArgumentsResolver extractArgumentsResolver(Element element) {
        String argsString = element.getAttribute(AbstractPropertyValidationElementHandler.ARGS_ATTR);
        String[] expressions = (argsString == null) ? new String[0] : StringUtils.tokenizeToStringArray(argsString, ", ");
        return new ExpressionErrorArgumentsResolver(expressions, expressionResolver);
    }

    /**
     * Extracts the validation rule applicability condition from the given element. Expects an "apply-if" attribute to
     * indicate the condition expression. If no such attribute exisits, an {@link org.springmodules.validation.util.condition.common.AlwaysTrueCondition} is returned. The
     * configured {@link org.springmodules.validation.util.condition.parser.ConditionParser} is used to parse the found expression to a condition.
     *
     * @param element The element that represents the validation rule.
     * @return The validation rule applicability condition.
     */
    protected Condition extractApplicabilityCondition(Element element) {
        String expression = element.getAttribute(AbstractPropertyValidationElementHandler.APPLY_IF_ATTR);
        return (StringUtils.hasText(expression)) ? conditionParser.parse(expression) : new AlwaysTrueCondition();
    }

    /**
     * @see org.springmodules.validation.util.condition.parser.ConditionParserAware#setConditionParser(org.springmodules.validation.util.condition.parser.ConditionParser)
     */
    public void setConditionParser(ConditionParser conditionParser) {
        this.conditionParser = conditionParser;
    }

    /**
     * @see org.springmodules.validation.util.condition.parser.ConditionParserAware#getConditionParser()
     */
    public ConditionParser getConditionParser() {
        return conditionParser;
    }

    /**
     * @see org.springmodules.validation.util.bel.BeanExpressionResolverAware#setBeanExpressionResolver(org.springmodules.validation.util.bel.BeanExpressionResolver)
     */
    public void setBeanExpressionResolver(BeanExpressionResolver resolver) {
        this.expressionResolver = resolver;
    }

    /**
     * @see org.springmodules.validation.util.bel.BeanExpressionResolverAware#getBeanExpressionResolver()
     */
    public BeanExpressionResolver getBeanExpressionResolver() {
        return expressionResolver;
    }

    /**
     * Returns the default error code for the validation rule.
     *
     * @param element The element that respresents the validation rule.
     * @return The default error code for the validation rule.
     */
    protected abstract String getDefaultErrorCode(Element element);

    /**
     * Extracts the validation rule condition from the given element.
     *
     * @param element The element that represents the validation rule.
     * @return The validation rule condition.
     * @throws org.springmodules.validation.bean.conf.xml.handler.XmlConditionConfigurationException When the given the format (structure) of the given element is mal-formed.
     */
    protected abstract Condition extractCondition(Element element) throws XmlConditionConfigurationException;

}
