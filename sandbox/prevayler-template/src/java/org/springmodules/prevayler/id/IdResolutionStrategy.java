package org.springmodules.prevayler.id;

import java.io.Serializable;
import java.lang.reflect.Field;

/**
 * <p>Strategy object for resolving the id of an object and retrieving it.</p>
 * <p>Implementors need to be <b>thread safe</b>.</p>
 *
 * @author Sergio Bossa
 */
public interface IdResolutionStrategy extends Serializable {
    
    /**
     * Resolve the id on the given object and return its corresponding {@link java.lang.reflect.Field} object.
     */
    public Field resolveId(Object target);
}
