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
        super(new ValangConditionParser(functionByName, dateParserByRegexp), expression);
    }

}
