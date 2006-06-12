package org.springmodules.validation.util.condition.parser;

import org.springmodules.validation.util.condition.Condition;

/**
 * @author Uri Boness
 */
public interface ConditionParser {

    Condition parse(String text) throws ConditionParseException;

}
