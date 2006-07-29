package org.springmodules.validation.bean.conf.loader.xml.handler;

import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springmodules.validation.bean.conf.MutableBeanValidationConfiguration;
import org.springmodules.validation.bean.conf.loader.DefaultValidationErrorCodes;
import org.springmodules.validation.bean.conf.loader.xml.XmlConfigurationException;
import org.springmodules.validation.bean.rule.DefaultValidationRule;
import org.springmodules.validation.bean.rule.ValidationRule;
import org.springmodules.validation.bean.rule.resolver.ErrorArgumentsResolver;
import org.springmodules.validation.bean.rule.resolver.FunctionErrorArgumentsResolver;
import org.springmodules.validation.util.cel.ConditionExpressionBased;
import org.springmodules.validation.util.cel.ConditionExpressionParser;
import org.springmodules.validation.util.cel.valang.ValangConditionExpressionParser;
import org.springmodules.validation.util.condition.Condition;
import org.springmodules.validation.util.condition.common.AlwaysTrueCondition;
import org.springmodules.validation.util.condition.common.IsNullCondition;
import org.springmodules.validation.util.fel.FunctionExpressionBased;
import org.springmodules.validation.util.fel.FunctionExpressionParser;
import org.springmodules.validation.util.fel.parser.ValangFunctionExpressionParser;
import org.w3c.dom.Element;

/**
 * A base class for common {@link org.springmodules.validation.bean.conf.loader.xml.handler.PropertyValidationElementHandler}
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
 * {@link org.springmodules.validation.util.cel.ConditionExpressionParser} that is associated with this handler. It
 * uses {@link org.springmodules.validation.util.cel.valang.ValangConditionExpressionParser} by default.
 *
 * @author Uri Boness
 */
public abstract class AbstractClassValidationElementHandler
    implements ClassValidationElementHandler, ConditionExpressionBased, FunctionExpressionBased, DefaultValidationErrorCodes {

    private static final String ERROR_CODE_ATTR = "code";
    private static final String MESSAGE_ATTR = "message";
    private static final String ARGS_ATTR = "args";
    private static final String APPLY_IF_ATTR = "apply-if";

    private String elementName;
    private String namespaceUrl;

    private ConditionExpressionParser conditionExpressionParser;
    private FunctionExpressionParser functionExpressionParser;

    /**
     * Constructs a new AbstractPropertyValidationElementHandler with given supported element name.
     *
     * @param elementName The supported element name.
     */
    public AbstractClassValidationElementHandler(String elementName) {
        this(elementName, null);
    }

    /**
     * Constructs a new AbstractPropertyValidationElementHandler with given supported element name and namespace.
     *
     * @param elementName The supported element name.
     * @param namespace The supported namespace.
     */
    public AbstractClassValidationElementHandler(String elementName, String namespace) {
        this(elementName, namespace, new ValangConditionExpressionParser(), new ValangFunctionExpressionParser());
    }

    /**
     * Constructs a new AbstractPropertyValidationElementHandler with given supported element name and namespace and a
     * condition handler to parse the <code>apply-if</code> expressions.
     *
     * @param elementName The supported element name.
     * @param namespace The supported namespace.
     * @param conditionExpressionParser The condition expression parser to be used to parse the various condition expressions.
     * @param functionExpressionParser The function expression parser to be used to parse the error arguments expressions.
     */
    public AbstractClassValidationElementHandler(
        String elementName,
        String namespace,
        ConditionExpressionParser conditionExpressionParser,
        FunctionExpressionParser functionExpressionParser) {

        this.elementName = elementName;
        this.namespaceUrl = namespace;
        this.conditionExpressionParser = conditionExpressionParser;
        this.functionExpressionParser = functionExpressionParser;
    }

    /**
     * Determines whether the given element is supported by this handler. The check is done by comparing the element
     * tag name and namespace with the ones that are configured with this handler.
     *
     * @see org.springmodules.validation.bean.conf.loader.xml.handler.PropertyValidationElementHandler#supports(org.w3c.dom.Element, Class, java.beans.PropertyDescriptor)
     */
    public boolean supports(Element element, Class clazz) {
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
     * @see org.springmodules.validation.bean.conf.loader.xml.handler.AbstractClassValidationElementHandler#handle(org.w3c.dom.Element, org.springmodules.validation.bean.conf.MutableBeanValidationConfiguration)
     */
    public void handle(Element element, MutableBeanValidationConfiguration configuration) {
        String errorCode = extractErrorCode(element);
        String message = extractMessage(element);
        ErrorArgumentsResolver argumentsResolver = extractArgumentsResolver(element);
        Condition condition = extractCondition(element);
        if (!isNullSupported()) {
            condition = new IsNullCondition().or(condition);
        }
        Condition applicabilityCondition = extractApplicabilityCondition(element);
        ValidationRule rule = new DefaultValidationRule(condition, applicabilityCondition, errorCode, message, argumentsResolver);
        configuration.addGlobalRule(rule);
    }

    /**
     * By default the element handlers handle and produce rules that can be associated with both global and non-global
     * contexts.
     */
    public boolean isConditionGloballyScoped() {
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
        String code = element.getAttribute(AbstractClassValidationElementHandler.ERROR_CODE_ATTR);
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
        return element.getAttribute(AbstractClassValidationElementHandler.MESSAGE_ATTR);
    }

    /**
     * Extracts the validation rule error arguments from the given element. Expects an "args" attribute to indicate the
     * error arguments. If no such attribute exisits, returns an empty array.
     *
     * @param element The element that represents the validation rule.
     * @return The validation rule error arguments.
     */
    protected ErrorArgumentsResolver extractArgumentsResolver(Element element) {
        String argsString = element.getAttribute(AbstractClassValidationElementHandler.ARGS_ATTR);
        String[] expressions = (argsString == null) ? new String[0] : StringUtils.tokenizeToStringArray(argsString, ", ");
        return new FunctionErrorArgumentsResolver(expressions, functionExpressionParser);
    }

    /**
     * Extracts the validation rule applicability condition from the given element. Expects an "apply-if" attribute to
     * indicate the condition expression. If no such attribute exisits, an {@link org.springmodules.validation.util.condition.common.AlwaysTrueCondition} is returned. The
     * configured {@link org.springmodules.validation.util.cel.ConditionExpressionParser} is used to parse the found expression to a condition.
     *
     * @param element The element that represents the validation rule.
     * @return The validation rule applicability condition.
     */
    protected Condition extractApplicabilityCondition(Element element) {
        String expression = element.getAttribute(AbstractClassValidationElementHandler.APPLY_IF_ATTR);
        return (StringUtils.hasText(expression)) ? conditionExpressionParser.parse(expression) : new AlwaysTrueCondition();
    }

    /**
     * @see ConditionExpressionBased#setConditionExpressionParser(org.springmodules.validation.util.cel.ConditionExpressionParser)
     */
    public void setConditionExpressionParser(ConditionExpressionParser conditionExpressionParser) {
        this.conditionExpressionParser = conditionExpressionParser;
    }

    protected ConditionExpressionParser getConditionExpressionParser() {
        return conditionExpressionParser;
    }

    /**
     * @see FunctionExpressionBased#setFunctionExpressionParser(org.springmodules.validation.util.fel.FunctionExpressionParser)
     */
    public void setFunctionExpressionParser(FunctionExpressionParser functionExpressionParser) {
        this.functionExpressionParser = functionExpressionParser;
    }

    /**
     * Returns the {@link FunctionExpressionParser} used by this handler to parse the error argument expressions.
     *
     * @return The {@link FunctionExpressionParser} used by this handler to parse the error argument expressions.
     */
    protected FunctionExpressionParser getFunctionExpressionParser() {
        return functionExpressionParser;
    }

    /**
     * Indicates whether the validation rule supports null values. Null values support means such values will be
     * passed to the rule condition during validation. If the rule doesn't support null values, such value will not
     * be evaluated by the rule condition and the validation will treat them as valid values.
     *
     * @return <code>true</code> if the validation rule support null values, <code>false</code> otherwise.
     */
    protected boolean isNullSupported() {
        return false;
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
     * @throws XmlConfigurationException When the given the format (structure) of the given element is mal-formed.
     */
    protected abstract Condition extractCondition(Element element) throws XmlConfigurationException;

}
