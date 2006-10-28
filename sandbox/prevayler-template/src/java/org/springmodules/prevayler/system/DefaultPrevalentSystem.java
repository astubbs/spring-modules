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
    
    public Object save(Object newEntity) {
        this.localIdentityMap.set(new IdentityHashMap());
        this.internalSave(newEntity);
        return newEntity;
    }
    
    public Object update(Object entity) {
        Object id = null;
        try {
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
            Map entityMap = this.lookupMap(entity.getClass());
            id = this.prevalenceInfo.getIdResolutionStrategy().resolveId(entity).get(entity);
            if (id != null) {
                Object old = entityMap.get(id);
                if (old != null) {
                    entityMap.remove(id);
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
        Map entityMap = this.lookupMap(entityClass);
        entityMap.clear();
    }
    
    public Object get(Class entityClass, Object id) {
        Map entityMap = this.lookupMap(entityClass);
        return entityMap.get(id);
    }

    public List get(Class entityClass) {
        Map entityMap = this.lookupMap(entityClass);
        List result = new LinkedList();
        Iterator it = entityMap.values().iterator();
        while (it.hasNext()) {
            Object currentEntity = it.next();
            if (entityClass.isAssignableFrom(currentEntity.getClass())) {
                result.add(currentEntity);
            }
        }
        return result;
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
    
    /*** Class internals ***/
    
    private void internalSave(Object newEntity) {
        DefaultPrevalentSystem.logger.debug("Saving object: " + newEntity);
        Object id = null;
        try {
            //  Get the identity map by entity class:
            Map entityMap = this.lookupMap(newEntity.getClass());
            // Assign id:
            id = this.prevalenceInfo.getIdGenerationStrategy().generateId();
            this.prevalenceInfo.getIdResolutionStrategy().resolveId(newEntity).set(newEntity, id);
            // Add the entity to the thread local identity map, for tracking its traversal (an entity once traversed must not be tarversed again):
            IdentityHashMap localIdentityMap = (IdentityHashMap) DefaultPrevalentSystem.localIdentityMap.get();
            localIdentityMap.put(newEntity, new Integer(1));
            // Add the new entity to the system:
            entityMap.put(id, newEntity);
            // Cascade persistence:
            this.doCascadePersistence(newEntity);
        }
        catch(IllegalAccessException actual) {
            throw new IllegalStateException("Cannot access id value: " + id, actual);
        }
    }
    
    private void internalUpdate(Object updatedEntity, Object id) {
        DefaultPrevalentSystem.logger.debug("Updating object " + updatedEntity + " with id: " + id);
        //  Get the identity map by the entity class:
        Map entityMap = this.lookupMap(updatedEntity.getClass());
        // Look for the entity in the map and update it (if found):
       Object actualEntity = entityMap.get(id);
        if ((actualEntity != null) && (actualEntity.getClass().equals(updatedEntity.getClass()))) {
            IdentityHashMap localIdentityMap = (IdentityHashMap) DefaultPrevalentSystem.localIdentityMap.get();
            // If the object has still not been reached:
            if (localIdentityMap.get(actualEntity) == null) {
                // Add the actual entity to the thread local identity map, for tracking its traversal (an entity once traversed must not be tarversed again):
                localIdentityMap.put(actualEntity, new Integer(1));
                // Copy new into actual and do cascade persistence:
                this.doCopyAndCascadePersistence(updatedEntity, actualEntity);
            }
        }
        else {
            throw new PrevaylerDataRetrievalException("Cannot find object with id: " + id);
        }
    }

    private void doCascadePersistence(Object root) {
        Class currentClass = root.getClass();
        while (currentClass != null) {
            try {
                Field[] fields = currentClass.getDeclaredFields();
                for (int counter = 0; counter < fields.length; counter++) {
                    // Get the field:
                    Field currentField = fields[counter];
                    currentField.setAccessible(true);
                    // Traverse its sub-graph, making cascade persistence:
                    Object value = currentField.get(root);
                    this.traverseValue(value);    
                }
                currentClass = currentClass.getSuperclass();
            }
            catch(IllegalAccessException ex) {
                throw new PrevaylerCascadePersistenceException(ex.getMessage(), ex);
            }
        }
    }
    
    private void doCopyAndCascadePersistence(Object source, Object destination) {
        Class currentClass = source.getClass();
        while (currentClass != null) {
            try {
                Field[] fields = currentClass.getDeclaredFields();
                for (int counter = 0; counter < fields.length; counter++) {
                    // Get the field:
                    Field currentField = fields[counter];
                    currentField.setAccessible(true);
                    // Copy the source value into destination one:
                    Object sourceValue = currentField.get(source);
                    currentField.set(destination, sourceValue); 
                    // Make cascade persistence:
                    this.traverseValue(sourceValue);
                }
                currentClass = currentClass.getSuperclass();
            }
            catch(IllegalAccessException ex) {
                throw new PrevaylerCascadePersistenceException(ex.getMessage(), ex);
            }
        }
    }
    
    private void traverseValue(Object value) {
        if (value != null) {
            if (value.getClass().isArray()) {
                this.traverseArray((Object[]) value);
            }
            else if (Collection.class.isAssignableFrom(value.getClass())) {
                this.traverseCollection((Collection) value);
            }
            else if (Map.class.isAssignableFrom(value.getClass())) {
                this.traverseMap((Map) value);
            }
            else if (this.prevalenceInfo.getPrevalentClass(value.getClass()) != null) {
                this.persistPrevalentObject(value);
            }
        }
    }

    private void traverseArray(Object[] array) {
        for (int i = 0; i < array.length; i++) {
            this.traverseValue(array[i]);
        }
    }
    
    private void traverseCollection(Collection collection) {
        Iterator it = collection.iterator();
        while (it.hasNext()) {
            this.traverseValue(it.next());
        }
    }
    
    private void traverseMap(Map map) {
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Entry) it.next();
            this.traverseValue(entry.getKey());
            this.traverseValue(entry.getValue());
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
            Map entityMap = (Map) this.entitiesGlobalMap.get(prevalentClass);
            if (entityMap == null) {
                entityMap = new ConcurrentHashMap();
                this.entitiesGlobalMap.put(prevalentClass, entityMap);
            }
            return entityMap;
        }
        else {
            throw new PrevaylerConfigurationException("Object with no configured prevalent class: " + entityClass);
        }
    }
}
