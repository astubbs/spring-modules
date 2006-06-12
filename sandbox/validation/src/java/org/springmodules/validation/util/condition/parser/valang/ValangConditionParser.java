package org.springmodules.validation.util.condition.parser.valang;

import java.io.StringReader;

import org.springmodules.validation.util.condition.Condition;
import org.springmodules.validation.util.condition.adapter.CommonsPredicateCondition;
import org.springmodules.validation.util.condition.parser.ConditionParseException;
import org.springmodules.validation.util.condition.parser.ConditionParser;
import org.springmodules.validation.valang.parser.ParseException;
import org.springmodules.validation.valang.parser.ValangParser;

/**
 * @author Uri Boness
 */
public class ValangConditionParser implements ConditionParser {

    public Condition parse(String text) throws ConditionParseException {
        ValangParser parser = new ValangParser(new StringReader(text));
        try {
            return new CommonsPredicateCondition(parser.parseExpression());
        } catch (ParseException pe) {
            throw new ConditionParseException("Could not parse Valang expression '" + text + "'", pe);
        }
    }

}
