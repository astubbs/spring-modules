package org.springmodules.prevayler.system;

import EDU.oswego.cs.dl.util.concurrent.ConcurrentHashMap;
import org.springmodules.prevayler.PrevaylerCascadePersistenceException;
import org.springmodules.prevayler.PrevaylerConfigurationException;
import org.springmodules.prevayler.PrevaylerDataRetrievalException;
import org.springmodules.prevayler.PrevaylerUnsavedObjectException;
import org.springmodules.prevayler.id.DefaultLongIdGenerator;
import org.springmodules.prevayler.id.DefaultIdResolver;
import org.springmodules.prevayler.id.IdGenerationStrategy;
import org.springmodules.prevayler.id.IdResolutionStrategy;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>{@link PrevalentSystem} implementation based on concurrent hash maps.</p>
 * <p>As a default it uses {@link org.springmodules.prevayler.id.DefaultLongIdGenerator} and {@link org.springmodules.prevayler.id.DefaultIdResolver} for
 * managing object ids. So, the only mandatory property to set here is the list of prevalent classes (see {@link #setPrevalentClasses(Class[] )})</p>
 * <p>This class is <b>thread safe</b>.</p>
 * 
 * 
 * @author Sergio Bossa
 */
public class DefaultPrevalentSystem implements PrevalentSystem {
    
    private static final long serialVersionUID = 476105268506333743L;
    
    private transient static final Log logger = LogFactory.getLog(DefaultPrevalentSystem.class);
    
    private transient static final ThreadLocal localIdentityMap = new ThreadLocal();
   
    private PrevalenceInfo prevalenceInfo = new PrevalenceInfo();
    private Map entitiesGlobalMap = new ConcurrentHashMap();
    
    public DefaultPrevalentSystem() {
        this.prevalenceInfo.setIdResolutionStrategy(new DefaultIdResolver());
        this.prevalenceInfo.setIdGenerationStrategy(new DefaultLongIdGenerator());
    }
    
    public void setPrevalentClasses(Class[] prevalentClasses) {
        this.prevalenceInfo.setPrevalentClasses(prevalentClasses);
    }
    
    public void setIdResolutionStartegy(IdResolutionStrategy idResolutionStartegy) {
        this.prevalenceInfo.setIdResolutionStrategy(idResolutionStartegy);
    }

    public void setIdGenerationStrategy(IdGenerationStrategy idGenerationStrategy) {
        this.prevalenceInfo.setIdGenerationStrategy(idGenerationStrategy);
    }
    
    public PrevalenceInfo getPrevalenceInfo() {
        return this.prevalenceInfo;
    }
    
    public Object save(Object newEntity) {
        this.localIdentityMap.set(new IdentityHashMap());
        this.internalSave(newEntity);
        return newEntity;
    }
    
    public Object update(Object entity) {
        Object id = null;
        try {
            // Look for the object id:
            id = this.prevalenceInfo.getIdResolutionStrategy().resolveId(entity).get(entity);
            if (id != null) {
                this.localIdentityMap.set(new IdentityHashMap());
                this.internalUpdate(entity, id);
                return entity;
            }
            else {
               throw new PrevaylerUnsavedObjectException("Cannot update unsaved object!");
            }
        }
        catch (IllegalAccessException actual) {
            throw new IllegalStateException("Cannot access id value: " + id, actual);
        }
    }
    
    public void delete(Object entity) {
        Object id = null;
        try {
            Map identityMap = this.lookupMap(entity.getClass());
            id = this.prevalenceInfo.getIdResolutionStrategy().resolveId(entity).get(entity);
            if (id != null) {
                Object old = identityMap.get(id);
                if (old != null) {
                    identityMap.remove(id);
                }
                else {
                    throw new PrevaylerDataRetrievalException("Cannot find object with id: " + id);
                }
            }
            else {
               throw new PrevaylerUnsavedObjectException("Cannot delete unsaved object!");
            }
        }
        catch (IllegalAccessException actual) {
            throw new IllegalStateException("Cannot access id value: " + id, actual);
        }
    }
    
    public void delete(Class entityClass) {
        Map identityMap = this.lookupMap(entityClass);
        identityMap.clear();
    }
    
    public Object get(Class entityClass, Object id) {
        Map identityMap = this.lookupMap(entityClass);
        return identityMap.get(id);
    }

    public List get(Class entityClass) {
        Map identityMap = this.lookupMap(entityClass);
        List result = new LinkedList();
        Iterator it = identityMap.values().iterator();
        while (it.hasNext()) {
            Object currentEntity = it.next();
            if (entityClass.isAssignableFrom(currentEntity.getClass())) {
                result.add(currentEntity);
            }
        }
        return result;
    }
    
    /*** Class internals ***/
    
    private void internalSave(Object newEntity) {
        Object id = null;
        try {
            //  Get the identity map by entity class:
            Map identityMap = this.lookupMap(newEntity.getClass());
            // Assign id:
            id = this.prevalenceInfo.getIdGenerationStrategy().generateId();
            this.prevalenceInfo.getIdResolutionStrategy().resolveId(newEntity).set(newEntity, id);
            // Add to the system:
            DefaultPrevalentSystem.logger.debug("Saving object with id: " + id);
            this.internalAdd(newEntity, id, identityMap);
        }
        catch(IllegalAccessException actual) {
            throw new IllegalStateException("Cannot access id value: " + id, actual);
        }
    }
    
    private void internalUpdate(Object entity, Object id) {
        //  Get the identity map by entity class:
        Map identityMap = this.lookupMap(entity.getClass());
        // Look for the entity in the map and update it:
       Object old = identityMap.get(id);
        if (old != null) {
            DefaultPrevalentSystem.logger.debug("Update object with id: " + id);
            this.internalAdd(entity, id, identityMap);
        }
        else {
            throw new PrevaylerDataRetrievalException("Cannot find object with id: " + id);
        }
    }
    
    private void internalAdd(Object entity, Object id, Map identityMap) {
        // Cascade persistence management through identity maps:
        IdentityHashMap localIdentityMap = (IdentityHashMap) DefaultPrevalentSystem.localIdentityMap.get();
        if (localIdentityMap.get(entity) == null) {
            // Add the entity to the thread local identity map, for tracking its traversal (an entity once traversed must not be tarversed again):
            localIdentityMap.put(entity, new Integer(1));
             // Add the entity to the system identity map:
            identityMap.put(id, entity);
            
            this.doCascadePersistence(entity);
        }
    }

    private void doCascadePersistence(Object root) {
        Class currentClass = root.getClass();
        while (currentClass != null) {
            try {
                Field[] fields = currentClass.getDeclaredFields();
                for (int counter = 0; counter < fields.length; counter++) {
                    Field currentField = fields[counter];
                    currentField.setAccessible(true);
                    
                    Object value = currentField.get(root);
                    this.persistValue(value);    
                }
                currentClass = currentClass.getSuperclass();
            }
            catch(IllegalAccessException ex) {
                throw new PrevaylerCascadePersistenceException(ex.getMessage(), ex);
            }
        }
    }
    
    private void persistValue(Object value) {
        if (value != null) {
            if (value.getClass().isArray()) {
                this.persistArray((Object[]) value);
            }
            else if (Collection.class.isAssignableFrom(value.getClass())) {
                this.persistCollection((Collection) value);
            }
            else if (Map.class.isAssignableFrom(value.getClass())) {
                this.persistMap((Map) value);
            }
            else if (this.prevalenceInfo.getPrevalentClass(value.getClass()) != null) {
                this.persistPrevalentObject(value);
            }
        }
    }

    private void persistArray(Object[] array) {
        for (int i = 0; i < array.length; i++) {
            this.persistValue(array[i]);
        }
    }
    
    private void persistCollection(Collection collection) {
        Iterator it = collection.iterator();
        while (it.hasNext()) {
            this.persistValue(it.next());
        }
    }
    
    private void persistMap(Map map) {
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Entry) it.next();
            this.persistValue(entry.getKey());
            this.persistValue(entry.getValue());
        }
    }

    private void persistPrevalentObject(Object entity) {
        Object id = null;
        try {
            Field idField = this.prevalenceInfo.getIdResolutionStrategy().resolveId(entity);
            id = idField.get(entity);
            if (id == null) {
                this.internalSave(entity);
            }
            else {
                this.internalUpdate(entity, id);
            }
        }
        catch (IllegalAccessException actual) {
            throw new IllegalStateException("Cannot access id value: " + id, actual);
        }
    }
    
    private Map lookupMap(Class entityClass) {
        Class prevalentClass = this.prevalenceInfo.getPrevalentClass(entityClass);
        if (prevalentClass != null) {
            Map identityMap = (Map) this.entitiesGlobalMap.get(prevalentClass);
            if (identityMap == null) {
                identityMap = new ConcurrentHashMap();
                this.entitiesGlobalMap.put(prevalentClass, identityMap);
            }
            return identityMap;
        }
        else {
            throw new PrevaylerConfigurationException("Object with no configured prevalent class: " + entityClass);
        }
    }
}
