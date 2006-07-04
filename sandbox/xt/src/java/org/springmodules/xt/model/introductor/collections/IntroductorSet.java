/*
 * Copyright 2006 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springmodules.xt.model.introductor.collections;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.springmodules.xt.model.introductor.DynamicIntroductor;
import org.apache.commons.collections.map.ReferenceIdentityMap;

/**
 * {@link java.util.Set} decorator, which makes all the contained objects implementing a given array of interfaces, leaving
 * the original target set unmodified.<br>
 * This builds on top of {@link DynamicIntroductor} implementations.<br>
 * <b>Important</b>: Changes to this decorated collection are reflected in the original set, and vice-versa.
 * 
 * @author Sergio Bossa
 */
public class IntroductorSet extends AbstractSet {
    
    private Set target;
    private Class[] introducedInterfaces;
    private Class[] targetObjectsInterfaces;
    private DynamicIntroductor introductor;
    /**
     * Caching original target objects is necessary because when we introduce an object, the next time it is asked for, we MUST retrieve
     * the introduced one, in order to preserve introduced values.
     * This map contains weak references because entries relative to cached values must be automatically garbage collected when no more strongly referenced,
     * and it is an identity map because keys and values must be compared by address identity, not by equals() method, because this could lead
     * to consider an object already introduced while it could be not.
     */
    private Map cachedValues = new ReferenceIdentityMap(ReferenceIdentityMap.WEAK, ReferenceIdentityMap.HARD);
    /**/
    
    /**
     * Construct the decorated introductor Set, starting from a target Set which will not be modified.
     * 
     * @param target The Set to decorate, whose objects must be introduced.
     * @param introducedInterfaces The interfaces to introduce.
     * @param targetObjectsInterfaces The interfaces of the objects into the Set.
     * @param introductor The concrete {@link DynamicIntroductor} to use for introducing interfaces.
     */
    public IntroductorSet(Set target, Class[] introducedInterfaces, Class[] targetObjectsInterfaces, DynamicIntroductor introductor) {
        this.target = target;
        this.introducedInterfaces = introducedInterfaces;
        this.targetObjectsInterfaces = targetObjectsInterfaces;
        this.introductor = introductor;
    }
    
    /**
     * Construct the decorated introductor Set, starting from a target Set which will not be modified.
     * @param target The Set to decorate, whose objects must be introduced.
     * @param interfaces The interfaces to introduce.
     * @param introductor The concrete {@link DynamicIntroductor} to use for introducing interfaces.
     */
    public IntroductorSet(Set target, Class[] interfaces, DynamicIntroductor introductor) {
        this.target = target;
        this.introducedInterfaces = interfaces;
        this.introductor = introductor;
    }

    public int size() {
        return this.target.size();
    }

    public Iterator iterator() {
        return new IntroductorIterator(this.target.iterator());
    }

    public boolean contains(Object o) {
        return this.target.contains(o);
    }
    
    public boolean containsAll(Collection c) {
        return this.target.containsAll(c);
    }

    public boolean add(Object o) {
        return this.target.add(o);
    }
    
    public boolean remove(Object o) {
        boolean removed = this.target.remove(o);
        if (removed) {
            this.cachedValues.remove(o);
        }
        return removed;
    }

    private Object introduce(Object target) {
        if (this.targetObjectsInterfaces != null) {
            return this.introductor.introduceInterfaces(
                    target, 
                    this.introducedInterfaces, 
                    this.targetObjectsInterfaces);
        }
        else {
            return this.introductor.introduceInterfaces(
                    target, 
                    this.introducedInterfaces);
        }
    }
    
    private class IntroductorIterator implements Iterator {
        
        private Iterator sourceIt;
        private Object currentSourceObject;
        
        public IntroductorIterator(Iterator it) {
            this.sourceIt = it;
        }
        
        public boolean hasNext() {
            return this.sourceIt.hasNext();
        }

        public Object next() {
            this.currentSourceObject = this.sourceIt.next();
            Object cachedObject = IntroductorSet.this.cachedValues.get(this.currentSourceObject);
            
            // If an object corresponding to the current one has been already cached, return it:
            if (cachedObject != null) {
                return cachedObject;
            } 
            // If there's no cached object, make a new introduction:
            else {
                Object result = IntroductorSet.this.introduce(this.currentSourceObject);
                IntroductorSet.this.cachedValues.put(this.currentSourceObject, result);
                return result;
            }
        }

        public void remove() {
            this.sourceIt.remove();
            IntroductorSet.this.cachedValues.remove(this.currentSourceObject);
        }
    }
}
