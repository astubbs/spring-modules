package org.springmodules.validation.util.condition.parser.valang;

import java.util.Map;

import org.springmodules.validation.util.condition.parser.ConditionParserCondition;

/**
 * @author Uri Boness
 */
public class ValangCondition extends ConditionParserCondition {

    public ValangCondition(String expression) {
        super(new ValangConditionParser(), expression);
    }

    public ValangCondition(String expression, Map functionByName, Map dateParserByRegexp) {
        super(createParser(functionByName, dateParserByRegexp), expression);
    }

    private static ValangConditionParser createParser(Map functionByName, Map dateParserByRegexp) {
        ValangConditionParser parser = new ValangConditionParser();
        parser.setCustomFunctions(functionByName);
        parser.setDateParsers(dateParserByRegexp);
        return parser;
    }

}
