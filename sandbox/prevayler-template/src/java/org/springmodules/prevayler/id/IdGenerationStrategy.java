package org.springmodules.prevayler.id;

import java.io.Serializable;

/**
 * <p>Strategy object for generating ids in a prevalent system.</p>
 * <p>Implementors need to be <b>thread safe</b>.</p>
 * <p>Moreover, the generation strategy must be <b>deterministic</b>: generated identifiers
 * must have a predictable sequence.</p>
 *
 * @author Sergio Bossa
 */
public interface IdGenerationStrategy extends Serializable {
    
    /**
     * Generate a new (obviously unique) id object.
     */
    public Object generateId();
}
