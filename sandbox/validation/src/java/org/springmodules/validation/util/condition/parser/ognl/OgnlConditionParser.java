package org.springmodules.validation.util.condition.parser.ognl;

import org.springmodules.validation.util.condition.Condition;
import org.springmodules.validation.util.condition.parser.ConditionParseException;
import org.springmodules.validation.util.condition.parser.ConditionParser;

/**
 * @author Uri Boness
 */
public class OgnlConditionParser implements ConditionParser {

    public Condition parse(String text) throws ConditionParseException {
        try {
            return new OgnlCondition(text);
        } catch (IllegalArgumentException iae) {
            throw new ConditionParseException("Could not parse OGNL expression '" + text + "'", iae);
        }
    }

}
