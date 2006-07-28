package org.springmodules.validation.util.fel;

import org.springframework.core.NestedRuntimeException;

/**
 * @author Uri Boness
 */
public class FelException extends NestedRuntimeException {

    public FelException(String msg) {
        super(msg);
    }

    public FelException(String msg, Throwable ex) {
        super(msg, ex);
    }

}
