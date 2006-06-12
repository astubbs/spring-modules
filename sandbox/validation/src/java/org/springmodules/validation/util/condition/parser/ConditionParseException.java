package org.springmodules.validation.util.condition.parser;

/**
 * @author Uri Boness
 */
public class ConditionParseException extends RuntimeException {

    public ConditionParseException(String message) {
        super(message);
    }

    public ConditionParseException(String message, Throwable cause) {
        super(message, cause);
    }

}
