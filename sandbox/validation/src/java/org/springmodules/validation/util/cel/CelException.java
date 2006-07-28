package org.springmodules.validation.util.cel;

import org.springframework.core.NestedRuntimeException;

/**
 * @author Uri Boness
 */
public class CelException extends NestedRuntimeException {

    public CelException(String msg) {
        super(msg);
    }

    public CelException(String msg, Throwable ex) {
        super(msg, ex);
    }

}
