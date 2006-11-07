package org.springmodules.prevayler.system;

import edu.emory.mathcs.backport.java.util.concurrent.ConcurrentHashMap;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.log4j.Logger;
import org.springmodules.prevayler.support.PrevaylerCascadePersistenceException;
import org.springmodules.prevayler.support.PrevaylerConfigurationException;
import org.springmodules.prevayler.support.PrevaylerDataRetrievalException;
import org.springmodules.prevayler.support.PrevaylerUnsavedObjectException;
import org.springmodules.prevayler.system.callback.SystemCallback;
import org.springmodules.prevayler.id.DefaultIdMerger;
import org.springmodules.prevayler.id.IdMerger;

/**
 * <p>{@link PrevalentSystem} implementation based on concurrent hash maps.</p>
 * <p>The only mandatory property to set here is the {@link PrevalenceInfo}.</p>
 * <p>This class is <b>thread safe</b>.</p>
 *
 * @author Sergio Bossa
 */
public class DefaultPrevalentSystem implements PrevalentSystem {
    
    private static final long serialVersionUID = 476105268506333743L;
    
    private transient static final Logger logger = Logger.getLogger(DefaultPrevalentSystem.class);
    
    private transient static final ThreadLocal localIdentityMap = new ThreadLocal();
    
    // FIXME: Currently not configurable:
    private IdMerger merger = new DefaultIdMerger();
    
    private PrevalenceInfo prevalenceInfo = new PrevalenceInfo();
    
    private Map entitiesGlobalMap = new ConcurrentHashMap();
    
    public DefaultPrevalentSystem() {
        this.merger.setPrevalenceInfo(this.prevalenceInfo);
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
            } else {
                throw new PrevaylerUnsavedObjectException("Cannot update unsaved object!");
            }
        } catch (IllegalAccessException actual) {
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
                } else {
                    throw new PrevaylerDataRetrievalException("Cannot find object with id: " + id);
                }
            } else {
                throw new PrevaylerUnsavedObjectException("Cannot delete unsaved object!");
            }
        } catch (IllegalAccessException actual) {
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
    
    public void merge(Object sourceEntity, Object destinationEntity) {
        this.merger.merge(sourceEntity, destinationEntity);
    }
    
    public Object execute(SystemCallback callback) {
        return callback.execute(this);
    }
    
    public void setPrevalenceInfo(PrevalenceInfo info) {
        this.prevalenceInfo = info;
        this.merger.setPrevalenceInfo(this.prevalenceInfo);
    }
    
    /*** Persistence internals ***/
    
    private Object internalSave(Object newEntity) {
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
            // Self cascade persistence (needed for updating pointers to already persisted entities):
            this.doCascadePersistence(newEntity, newEntity);
            // Return the new, updated and saved, entity:
            return newEntity;
        } catch(IllegalAccessException actual) {
            throw new IllegalStateException("Cannot access id value: " + id, actual);
        }
    }
    
    private Object internalUpdate(Object updatedEntity, Object id) {
        DefaultPrevalentSystem.logger.debug("Updating object " + updatedEntity + " with id: " + id);
        //  Get the identity map by the entity class:
        Map entityMap = this.lookupMap(updatedEntity.getClass());
        // Look for the entity in the map and update it (if found):
        Object currentEntity = entityMap.get(id);
        if ((currentEntity != null) && (currentEntity.getClass().equals(updatedEntity.getClass()))) {
            IdentityHashMap localIdentityMap = (IdentityHashMap) DefaultPrevalentSystem.localIdentityMap.get();
            // If the object has still not been reached:
            if (localIdentityMap.get(currentEntity) == null) {
                // Add the actual entity to the thread local identity map, for tracking its traversal (an entity once traversed must not be tarversed again):
                localIdentityMap.put(currentEntity, new Integer(1));
                // Copy new into actual and do cascade persistence:
                this.doCascadePersistence(updatedEntity, currentEntity);
            }
            // Return the updated (original) entity:
            return currentEntity;
        } else {
            throw new PrevaylerDataRetrievalException("Cannot find object with id: " + id);
        }
    }
    
    private void doCascadePersistence(Object source, Object destination) {
        Class currentClass = source.getClass();
        while (currentClass != null) {
            try {
                Field[] fields = currentClass.getDeclaredFields();
                for (int counter = 0; counter < fields.length; counter++) {
                    // Get the field:
                    Field currentField = fields[counter];
                    currentField.setAccessible(true);
                    // Update the field, from source to destination:
                    this.updateValue(source, destination, currentField);
                }
                currentClass = currentClass.getSuperclass();
            } catch(Exception ex) {
                throw new PrevaylerCascadePersistenceException(ex.getMessage(), ex);
            }
        }
    }
    
    private void updateValue(Object source, Object destination, Field field) throws Exception {
        Object value = field.get(source);
        if (value != null) {
            value = this.getUpdatedValue(value);
        }
        field.set(destination, value);
    }
    
    private Object getUpdatedValue(Object value) throws Exception {
        Object result = value;
        if (value.getClass().isArray()) {
            result = this.getUpdatedArray((Object[]) value);
        } else if (Collection.class.isAssignableFrom(value.getClass())) {
            result = this.getUpdatedCollection((Collection) value);
        } else if (Map.class.isAssignableFrom(value.getClass())) {
            result = this.getUpdatedMap((Map) value);
        } else if (this.prevalenceInfo.getPrevalentClass(value.getClass()) != null) {
            result = this.getUpdatedPrevalentObject(value);
        }
        return result;
    }
    
    private Object getUpdatedArray(Object[] array) throws Exception {
        Object[] result = (Object[]) array.clone();
        for (int i = 0; i < array.length; i++) {
            result[i] = this.getUpdatedValue(array[i]);
        }
        return result;
    }
    
    private Collection getUpdatedCollection(Collection collection) throws Exception {
        Collection result = (Collection) collection.getClass().getConstructor(new Class[0]).newInstance(new Object[0]);
        Iterator it = collection.iterator();
        while (it.hasNext()) {
            result.add(this.getUpdatedValue(it.next()));
        }
        return result;
    }
    
    private Map getUpdatedMap(Map map) throws Exception {
        Map result = (Map) map.getClass().getConstructor(new Class[0]).newInstance(new Object[0]);
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Entry) it.next();
            Object key = this.getUpdatedValue(entry.getKey());
            Object value = this.getUpdatedValue(entry.getValue());
            result.put(key, value);
        }
        return result;
    }
    
    private Object getUpdatedPrevalentObject(Object entity) {
        Object result = null;
        Object id = null;
        try {
            Field idField = this.prevalenceInfo.getIdResolutionStrategy().resolveId(entity);
            id = idField.get(entity);
            if (id == null) {
                result = this.internalSave(entity);
            } else {
                result = this.internalUpdate(entity, id);
            }
            return result;
        } catch (IllegalAccessException actual) {
            throw new IllegalStateException("Cannot access id value: " + id, actual);
        }
    }
    
    /** Other internals **/
    
    private Map lookupMap(Class entityClass) {
        Class prevalentClass = this.prevalenceInfo.getPrevalentClass(entityClass);
        if (prevalentClass != null) {
            Map entityMap = (Map) this.entitiesGlobalMap.get(prevalentClass);
            if (entityMap == null) {
                entityMap = new ConcurrentHashMap();
                this.entitiesGlobalMap.put(prevalentClass, entityMap);
            }
            return entityMap;
        } else {
            throw new PrevaylerConfigurationException("Object with no configured prevalent class: " + entityClass);
        }
    }
}
