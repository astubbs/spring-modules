package org.springmodules.prevayler.system;

import org.springmodules.prevayler.id.IdGenerationStrategy;
import org.springmodules.prevayler.id.IdResolutionStrategy;
import java.io.Serializable;
import java.util.List;

/**
 * <p>Prevalent system interface based on the standard well known DAO pattern. </p>
 * <p>This prevalent system has the advantage of being business-agnostic, so you can use it for any business domain
 * without implementing any additional code.</p>
 * <p>You just need to configure:
 * <ul>
 * <li>An {@link org.springmodules.prevayler.id.IdGenerationStrategy} for generating business objects id.</li>
 * <li>An {@link org.springmodules.prevayler.id.IdResolutionStrategy} for setting/getting business objects id.</li>
 * <li>The list of <b>prevalent classes</b>, that is, the classes you want to persist into this prevalent system.</li>
 * </ul>
 * </p>
 * <p>Configuring <b>prevalent classes</b> is the most important, yet very simple, step. Just keep in mind the following rules:
 * <ul>
 * <li>If you try to directly persist/retrieve an object of a class not configured here, the prevalent system will throw an exception.</li>
 * <li>If you configure a prevalent class <i>A</i>, all its sub-classes/sub-interfaces will be considered prevalent. This is <b>very important</b>,
 * because thanks to it you are able to do polymorphic data access operations: i.e. you are able to retrieve all objects of interface <i>A</i>.</li>
 * </ul>
 * </p>
 * <p>Implementors need to be <b>thread safe</b>.</p>
 * 
 * @author Sergio Bossa
 */
public interface PrevalentSystem extends Serializable {
    
    public void setPrevalentClasses(Class[] prevalentClasses);
    
    public void setIdResolutionStartegy(IdResolutionStrategy idResolutionStartegy);

    public void setIdGenerationStrategy(IdGenerationStrategy idGenerationStrategy);
    
    public PrevalenceInfo getPrevalenceInfo();
    
    public Object save(Object newEntity);

    public Object update(Object entity);

    public void delete(Object entity);
    
    public void delete(Class entityClass);
    
    public Object get(Class entityClass, Object id);
    
    public List get(Class entityClass);
}
