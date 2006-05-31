package org.springmodules.parallel;

import org.springframework.core.NestedRuntimeException;

public abstract class ParallelException extends NestedRuntimeException {

    public ParallelException(String msg) {
        super(msg);
    }
    
    public ParallelException(String msg, Throwable t) {
        super(msg, t);
    }

}
