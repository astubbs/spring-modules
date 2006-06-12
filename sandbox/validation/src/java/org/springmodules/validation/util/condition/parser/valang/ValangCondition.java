package org.springmodules.validation.util.condition.parser.valang;

import org.springmodules.validation.util.condition.parser.ConditionParserCondition;

/**
 * @author Uri Boness
 */
public class ValangCondition extends ConditionParserCondition {

    public ValangCondition(String expression) {
        super(new ValangConditionParser(), expression);
    }

}
