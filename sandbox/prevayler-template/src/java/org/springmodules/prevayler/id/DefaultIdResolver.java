package org.springmodules.prevayler.id;

import java.lang.reflect.Field;
import org.springmodules.prevayler.id.support.PrevaylerIdResolutionException;

/**
 * <p>Default id resolver which uses a default (configurable) id name: <i>id</i>.</p>
 * <p>This class is <b>thread safe</b>.</p>
 *
 * @author Sergio Bossa
 */
public class DefaultIdResolver implements IdResolutionStrategy {
    
    private static final long serialVersionUID = 476105268506378743L;
    
    private String name = "id";
    
    public Field resolveId(Object target) {
        Field idField = null;
        Class currentClass = target.getClass();
        while (currentClass != null) {
            try {
                idField = currentClass.getDeclaredField(this.name);
                break;
            }
            catch(Exception ex) {
                currentClass = currentClass.getSuperclass();
            }
        }
        if (idField != null) {
            idField.setAccessible(true);
            return idField;
        }
        else {
             throw new PrevaylerIdResolutionException("No id field found with name: " + this.name + " in object: " + target);
        }
    }

    /**
     * Get the id name to use (defaults to <i>id</i>).
     */
    public String getName() {
        return name;
    }

    /**
     * Set the id name to use (defaults to <i>id</i>).
     */
    public void setName(String name) {
        this.name = name;
    }
}
