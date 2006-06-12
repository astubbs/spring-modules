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

package org.springmodules.validation.bean.conf.xml.parser;

import org.springmodules.validation.bean.conf.xml.ValidationRuleElementHandler;
import org.springmodules.validation.bean.rule.ValidationRule;
import org.springmodules.validation.bean.rule.DefaultValidationRule;
import org.springmodules.validation.util.condition.Condition;
import org.springmodules.validation.util.condition.common.AlwaysTrueCondition;
import org.springmodules.validation.util.condition.parser.ConditionParser;
import org.springmodules.validation.util.condition.parser.valang.ValangConditionParser;
import org.w3c.dom.Element;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

/**
 * A base class for common {@link ValidationRuleElementHandler} implementations. This base parser idetifies the
 * the supported elements by their tag names (qualified and local). In addition, it assumes the following common
 * attributes:
 *
 * <ul>
 *  <li>code - Indicates the error code of the validatoin rule</li>
 *  <li>message - The default error message of the validation rule</li>
 *  <li>args - A comma separated list of error arguments</li>
 *  <li>apply-if - An condition expression that determines the applicability of the validation rule</li>
 * </ul>
 *
 * Note: The apply-if attribute is being parsed by the {@link ConditionParser} that is associated with this parser. It
 * uses {@link ValangConditionParser} by default.
 *
 * @author Uri Boness
 */
public abstract class AbstractValidationRuleElementHandler implements ValidationRuleElementHandler {

    private static final String ERROR_CODE_ATTR = "code";
    private static final String MESSAGE_ATTR = "message";
    private static final String ARGS_ATTR = "args";
    private static final String APPLY_IF_ATTR = "apply-if";

    private static final ConditionParser DEFAULT_CONDITION_PARSER = new ValangConditionParser();

    private String elementName;
    private String namespaceUrl;
    private ConditionParser conditionParser;

    /**
     * Constructs a new AbstractValidationRuleElementHandler with given supported element name.
     *
     * @param elementName The supported element name.
     */
    public AbstractValidationRuleElementHandler(String elementName) {
        this(elementName, null);
    }

    /**
     * Constructs a new AbstractValidationRuleElementHandler with given supported element name and namespace.
     *
     * @param elementName The supported element name.
     * @param namespace The supported namespace.
     */
    public AbstractValidationRuleElementHandler(String elementName, String namespace) {
        this(elementName, namespace, DEFAULT_CONDITION_PARSER);
    }

    /**
     * Constructs a new AbstractValidationRuleElementHandler with given supported element name and namespace and a
     * condition parser to parse the <code>apply-if</code> expressions.
     *
     * @param elementName The supported element name.
     * @param namespace The supported namespace.
     * @param conditionParser The condition parser to be used when parsing the <code>apply-if</code> expression.
     */
    public AbstractValidationRuleElementHandler(String elementName, String namespace, ConditionParser conditionParser) {
        this.elementName = elementName;
        this.namespaceUrl = namespace;
        this.conditionParser = conditionParser;
    }

    /**
     * Determines whether the given element is supported by this parser. The check is done by comparing the element
     * tag name and namespace with the ones that are configured with this parser.
     *
     * @see ValidationRuleElementHandler#supports(org.w3c.dom.Element)
     */
    public boolean supports(Element element) {
        String tagName = element.getTagName();
        if (!tagName.equals(elementName)) {
            return false;
        }
        String ns = element.getNamespaceURI();
        return ObjectUtils.nullSafeEquals(ns, namespaceUrl);
    }

    /**
     * Creates and returns an appropriate {@link ValidationRule} based on the given element.
     *
     * @param element The element that represents the validation rule.
     * @return The created validation rule.
     * @see ValidationRuleElementHandler#handle(org.w3c.dom.Element)
     */
    public ValidationRule handle(Element element) {
        String errorCode = extractErrorCode(element);
        String message = extractMessage(element);
        Object[] args = extractArguments(element);
        Condition condition = extractCondition(element);
        Condition applicabilityCondition = extractApplicabilityCondition(element);
        return new DefaultValidationRule(condition, applicabilityCondition, errorCode, message, args);
    }

    /**
     * Extracts the validation rule error code from the given element. Expects a "code" attribute to indicate the
     * error code. If no such attribute exisits, returns {@link #getDefaultErrorCode(org.w3c.dom.Element)} instead.
     *
     * @param element The element that represents the validation rule.
     * @return The validation rule error code.
     */
    protected String extractErrorCode(Element element) {
        String code = element.getAttribute(ERROR_CODE_ATTR);
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
        return element.getAttribute(MESSAGE_ATTR);
    }

    /**
     * Extracts the validation rule error arguments from the given element. Expects an "args" attribute to indicate the
     * error arguments. If no such attribute exisits, returns an empty array.
     *
     * @param element The element that represents the validation rule.
     * @return The validation rule error arguments.
     */
    protected Object[] extractArguments(Element element) {
        String argsString = element.getAttribute(ARGS_ATTR);
        if (argsString == null) {
            return new Object[0];
        }
        return StringUtils.tokenizeToStringArray(argsString, ", ");
    }

    /**
     * Extracts the validation rule applicability condition from the given element. Expects an "apply-if" attribute to
     * indicate the condition expression. If no such attribute exisits, an {@link AlwaysTrueCondition} is returned. The
     * configured {@link ConditionParser} is used to parse the found expression to a condition.
     *
     * @param element The element that represents the validation rule.
     * @return The validation rule applicability condition.
     */
    protected Condition extractApplicabilityCondition(Element element) {
        String expression = element.getAttribute(APPLY_IF_ATTR);
        return (StringUtils.hasText(expression)) ? conditionParser.parse(expression) : new AlwaysTrueCondition();
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
     * @throws XmlConditionConfigurationException When the given the format (structure) of the given element is mal-formed.
     */
    protected abstract Condition extractCondition(Element element) throws XmlConditionConfigurationException;
}
