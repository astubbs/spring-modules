package org.springmodules.validation.util.condition.parser;

import org.springframework.core.NestedRuntimeException;

/**
 * @author Uri Boness
 */
public class ConditionParseException extends NestedRuntimeException {

    public ConditionParseException(String message) {
        super(message);
    }

    public ConditionParseException(String message, Throwable cause) {
        super(message, cause);
    }

}
