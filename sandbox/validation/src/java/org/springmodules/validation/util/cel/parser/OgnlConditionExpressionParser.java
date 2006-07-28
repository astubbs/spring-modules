package org.springmodules.validation.util.cel.parser;

import org.springmodules.validation.util.cel.ConditionExpressionParser;
import org.springmodules.validation.util.cel.CelParseException;
import org.springmodules.validation.util.condition.Condition;
import org.springmodules.validation.util.condition.parser.ognl.OgnlCondition;

/**
 * @author Uri Boness
 */
public class OgnlConditionExpressionParser implements ConditionExpressionParser {

    public Condition parse(String expression) throws CelParseException {
        try {
            return new OgnlCondition(expression);
        } catch (IllegalArgumentException iae) {
            throw new CelParseException("Could not parse OGNL expression '" + expression + "'", iae);
        }
    }

}
