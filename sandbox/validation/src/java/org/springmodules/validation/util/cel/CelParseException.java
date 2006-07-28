package org.springmodules.validation.util.cel;

import org.springframework.core.NestedRuntimeException;

/**
 * @author Uri Boness
 */
public class CelParseException extends CelException {

    public CelParseException(String msg) {
        super(msg);
    }

    public CelParseException(String msg, Throwable ex) {
        super(msg, ex);
    }

}
