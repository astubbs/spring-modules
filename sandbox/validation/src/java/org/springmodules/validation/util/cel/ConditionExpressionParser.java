package org.springmodules.validation.util.cel;

import org.springmodules.validation.util.condition.Condition;

/**
 * @author Uri Boness
 */
public interface ConditionExpressionParser {

    Condition parse(String expression) throws CelParseException;

}
