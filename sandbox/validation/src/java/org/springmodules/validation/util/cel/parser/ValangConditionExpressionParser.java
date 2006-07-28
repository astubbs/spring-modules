package org.springmodules.validation.util.cel.parser;

import org.springmodules.validation.util.cel.ConditionExpressionParser;
import org.springmodules.validation.util.cel.CelParseException;
import org.springmodules.validation.util.condition.Condition;
import org.springmodules.validation.util.condition.adapter.CommonsPredicateCondition;
import org.springmodules.validation.valang.parser.ParseException;
import org.springmodules.validation.valang.parser.SimpleValangBased;

/**
 * @author Uri Boness
 */
public class ValangConditionExpressionParser extends SimpleValangBased implements ConditionExpressionParser {

    public Condition parse(String expression) throws CelParseException {
        try {
            return new CommonsPredicateCondition(createValangParser(expression).parseExpression());
        } catch (ParseException pe) {
            throw new CelParseException("Could not parse Expression expression '" + expression + "'", pe);
        }
    }

}
