package org.springmodules.validation.util.fel;

/**
 * @author Uri Boness
 */
public interface FunctionExpressionParser {

    Function parse(String expression) throws FelParseException;

}
