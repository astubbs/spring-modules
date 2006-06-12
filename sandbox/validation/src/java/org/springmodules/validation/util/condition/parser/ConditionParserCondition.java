package org.springmodules.validation.util.condition.parser;

import org.springmodules.validation.util.condition.common.ConditionProxyCondition;

/**
 * @author Uri Boness
 */
public class ConditionParserCondition extends ConditionProxyCondition {

    public ConditionParserCondition(ConditionParser parser, String expression) {
        super(parser.parse(expression));
    }

}
