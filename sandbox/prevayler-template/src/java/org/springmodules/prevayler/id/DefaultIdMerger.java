package org.springmodules.prevayler.id;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.springmodules.prevayler.id.support.PrevaylerIdMergeException;
import org.springmodules.prevayler.support.PrevaylerConfigurationException;
import org.springmodules.prevayler.system.PrevalenceInfo;

/**
 * {@link IdMerger} default implementation.
 *
 * @author Sergio Bossa
 */
public class DefaultIdMerger implements IdMerger {
    
    private static final long serialVersionUID = 476105208506333743L;
    
    private static final ThreadLocal localIdentityMap = new ThreadLocal();
    
    private PrevalenceInfo prevalenceInfo;
    
    public DefaultIdMerger(PrevalenceInfo info) {
        this.prevalenceInfo = info;
    }
    
    public DefaultIdMerger() {
    }
    
    public void setPrevalenceInfo(PrevalenceInfo info) {
        this.prevalenceInfo = info;
    }
    
    public void merge(Object source, Object destination) {
        Class sourceClass = source.getClass();
        Class destinationClass = destination.getClass();
        if (! sourceClass.equals(destinationClass)) {
            throw new IllegalArgumentException("Source and destination objects are not of the same class!");
        }
        else if (source == destination) {
            return;
        }
        else {
            this.localIdentityMap.set(new IdentityHashMap());
            this.doMerge(source, destination);
        }
    }
    
    private void doMerge(Object sourceValue, Object destinationValue) {
        if (sourceValue != null && destinationValue != null) {
            if (sourceValue.getClass().isArray()) {
                this.traverseArray((Object[]) sourceValue, (Object[]) destinationValue);
            }
            else if (Collection.class.isAssignableFrom(sourceValue.getClass())) {
                this.traverseCollection((Collection) sourceValue, (Collection) destinationValue);
            }
            else if (Map.class.isAssignableFrom(sourceValue.getClass())) {
                this.traverseMap((Map) sourceValue, (Map) destinationValue);
            }
            else if (this.prevalenceInfo.getPrevalentClass(sourceValue.getClass()) != null) {
                this.mergePrevalentObjectIdentifiers(sourceValue, destinationValue);
            }
        }
    }

    private void traverseArray(Object[] sourceArray, Object[] destinationArray) {
        for (int i = 0; i < sourceArray.length; i++) {
            this.doMerge(sourceArray[i], destinationArray[i]);
        }
    }
    
    private void traverseCollection(Collection sourceCollection, Collection destinationCollection) {
        Iterator sIt = sourceCollection.iterator();
        Iterator dIt = destinationCollection.iterator();
        while (sIt.hasNext()) {
            this.doMerge(sIt.next(), dIt.next());
        }
    }
    
    private void traverseMap(Map sourceMap, Map destinationMap) {
        Iterator sIt = sourceMap.entrySet().iterator();
        Iterator dIt = destinationMap.entrySet().iterator();
        while (sIt.hasNext()) {
            Map.Entry sourceEntry = (Entry) sIt.next();
            Map.Entry destinationEntry = (Entry) dIt.next();
            this.doMerge(sourceEntry.getKey(), destinationEntry.getKey());
            this.doMerge(sourceEntry.getValue(), destinationEntry.getValue());
        }
    }

    private void mergePrevalentObjectIdentifiers(Object sourceEntity, Object destinationEntity) {
        try {
            Class sourcePrevalentClass = this.prevalenceInfo.getPrevalentClass(sourceEntity.getClass());
            Class destinationPrevalentClass = this.prevalenceInfo.getPrevalentClass(destinationEntity.getClass());
            // If entities have prevalent classes:
            if (sourcePrevalentClass != null && destinationPrevalentClass != null) {
                if (sourcePrevalentClass.equals(destinationPrevalentClass)) {
                    // Copy the identifier from source to destination:
                    Field sourceIdField = this.prevalenceInfo.getIdResolutionStrategy().resolveId(sourceEntity);
                    Field destinationIdField = this.prevalenceInfo.getIdResolutionStrategy().resolveId(destinationEntity);
                    destinationIdField.set(destinationEntity, sourceIdField.get(sourceEntity));
                    // Cascade the merge:
                    this.cascadeMerge(sourceEntity, destinationEntity);
                }
                else {
                    throw new IllegalStateException("Classes don't match!");
                }
            }
            else {
                throw new PrevaylerConfigurationException("Object with no configured prevalent class!");
            }
        }
        catch (IllegalAccessException actual) {
            throw new IllegalStateException("Cannot access id value!", actual);
        }
    }
    
    private void cascadeMerge(Object source, Object destination) {
        // We are traversing the source object graph, and we'll have to put in an identity map the source object.
        IdentityHashMap localIdentityMap = (IdentityHashMap) DefaultIdMerger.localIdentityMap.get();
        if (! localIdentityMap.containsKey(source)) {
            localIdentityMap.put(source, new Integer(1));
            this.doCascadeMerge(source, destination);
        }
    }
    
    private void doCascadeMerge(Object source, Object destination) {
        Class currentClass = source.getClass();
        while (currentClass != null) {
            try {
                Field[] fields = currentClass.getDeclaredFields();
                for (int counter = 0; counter < fields.length; counter++) {
                    Field currentField = fields[counter];
                    currentField.setAccessible(true);

                    Object sourceValue = currentField.get(source);
                    Object destinationValue = currentField.get(destination);
                    this.doMerge(sourceValue, destinationValue);    
                }
                currentClass = currentClass.getSuperclass();
            }
            catch(IllegalAccessException ex) {
                throw new PrevaylerIdMergeException(ex.getMessage(), ex);
            }
        }
    }
}
