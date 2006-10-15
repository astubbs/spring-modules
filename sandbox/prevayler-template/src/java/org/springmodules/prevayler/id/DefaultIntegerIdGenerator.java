package org.springmodules.prevayler.id;

/**
 * <p>Default id generation strategy which generates a sequence of unique integer ids.</p>
 * <p>This class is <b>thread safe</b>.</p>
 *
 * @author Sergio Bossa
 */
public class DefaultIntegerIdGenerator implements IdGenerationStrategy {
    
    private static final long serialVersionUID = 476188008586333743L;

    private int counter;
    
    /**
     * Generate a {@link java.lang.Integer} id.
     */
    synchronized public Object generateId() {
        return new Integer(++counter);
    }
}
