package org.springmodules.validation.util.fel;

/**
 * @author Uri Boness
 */
public class FelParseException extends FelException {

    /**
     * Construct a <code>NestedRuntimeException</code> with the specified detail message.
     *
     * @param msg the detail message
     */
    public FelParseException(String msg) {
        super(msg);
    }

    /**
     * Construct a <code>NestedRuntimeException</code> with the specified detail message
     * and nested exception.
     *
     * @param msg the detail message
     * @param ex  the nested exception
     */
    public FelParseException(String msg, Throwable ex) {
        super(msg, ex);
    }

}
