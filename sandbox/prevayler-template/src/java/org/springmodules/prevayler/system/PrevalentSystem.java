package org.springmodules.prevayler.system;

import java.io.Serializable;
import java.util.List;
import org.springmodules.prevayler.SystemCallback;

/**
 * <p>Prevalent system interface based on the standard well known DAO pattern. </p>
 * <p>This prevalent system has the advantage of being business-agnostic, so you can use it for any business domain
 * without implementing any additional code.</p>
 * <p>You just need to configure its {@link PrevalenceInfo}.</p>
 * <p>The <b>prevalent classes</b> is the most important information carried by the PrevalenceInfo object. 
 * Talking about them, just keep in mind the following rules:
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
    
    public Object save(Object newEntity);

    public Object update(Object entity);

    public void delete(Object entity);
    
    public void delete(Class entityClass);
    
    public Object get(Class entityClass, Object id);
    
    public List get(Class entityClass);
    
    public void merge(Object sourceEntity, Object destinationEntity);
    
    public Object execute(SystemCallback callback);
    
    public void setPrevalenceInfo(PrevalenceInfo info);
}
