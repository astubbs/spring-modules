package org.springmodules.prevayler.system;

import java.io.Serializable;
import org.springmodules.prevayler.id.DefaultIdResolver;
import org.springmodules.prevayler.id.DefaultLongIdGenerator;
import org.springmodules.prevayler.id.IdGenerationStrategy;
import org.springmodules.prevayler.id.IdResolutionStrategy;

/**
 * Class containing information about the prevalent system, used to plug prevalence information into a 
 * {@link PrevalentSystem}.<br>
 * In particular, it holds the following information:
 * <ul>
 * <li>An {@link org.springmodules.prevayler.id.IdGenerationStrategy} for generating business objects id.</li>
 * <li>An {@link org.springmodules.prevayler.id.IdResolutionStrategy} for setting/getting business objects id.</li>
 * <li>The list of <b>prevalent classes</b>, that is, the classes you want to persist into this prevalent system.</li>
 * </ul>
 * As a default it uses {@link org.springmodules.prevayler.id.DefaultLongIdGenerator} and {@link org.springmodules.prevayler.id.DefaultIdResolver} for
 * managing object ids.
 *
 * @author Sergio Bossa
 */
public class PrevalenceInfo implements Serializable {
    
    private static final long serialVersionUID = 556105268506333743L;
    
    private Class[] prevalentClasses = new Class[0];
    private IdResolutionStrategy idResolutionStartegy = new DefaultIdResolver();
    private IdGenerationStrategy idGenerationStrategy = new DefaultLongIdGenerator();
    
    /**
     * Get the prevalent class corresponding to the given object class: that is, check if the given object class
     * is a prevalent class in the system.
     *
     * @return The prevalent class corresponding to the object class passed as argument.
     */
    public Class getPrevalentClass(Class objectClass) {
        Class pClass = null;
        for (int i = 0; i < this.prevalentClasses.length; i++) {
            Class current = this.prevalentClasses[i];
            if (current.isAssignableFrom(objectClass)) {
                pClass = this.prevalentClasses[i];
                break;
            }
        }
        return pClass;
    }

    /**
     * Return the prevalent classes in the system.
     */
    public Class[] getPrevalentClasses() {
        return prevalentClasses;
    }

    /**
     * Set the prevalent classes in the system.
     */
    public void setPrevalentClasses(Class[] prevalentClasses) {
        this.prevalentClasses = prevalentClasses;
    }

    /**
     * Get the {@link org.springmodules.prevayler.id.IdResolutionStrategy}.
     */
    public IdResolutionStrategy getIdResolutionStrategy() {
        return idResolutionStartegy;
    }

    /**
     * Set the {@link org.springmodules.prevayler.id.IdResolutionStrategy}.
     */
    public void setIdResolutionStrategy(IdResolutionStrategy idResolutionStartegy) {
        this.idResolutionStartegy = idResolutionStartegy;
    }

    /**
     * Get the {@link org.springmodules.prevayler.id.IdGenerationStrategy}.
     */
    public IdGenerationStrategy getIdGenerationStrategy() {
        return idGenerationStrategy;
    }

    /**
     * Set the {@link org.springmodules.prevayler.id.IdGenerationStrategy}.
     */
    public void setIdGenerationStrategy(IdGenerationStrategy idGenerationStrategy) {
        this.idGenerationStrategy = idGenerationStrategy;
    }
}
