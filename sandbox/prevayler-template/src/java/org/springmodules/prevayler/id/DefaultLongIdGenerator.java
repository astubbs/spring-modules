package org.springmodules.prevayler.id;

/**
 * <p>Default id generation strategy which generates a sequence of unique long ids.</p>
 * <p>This class is <b>thread safe</b>.</p>
 *
 * @author Sergio Bossa
 */
public class DefaultLongIdGenerator implements IdGenerationStrategy {
    
    private static final long serialVersionUID = 476105268586333743L;

    private long counter;
    
    /**
     * Generate a {@link java.lang.Long} id.
     */
    synchronized public Object generateId() {
        return new Long(++counter);
    }
}
