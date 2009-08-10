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

import java.util.AbstractList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import org.springmodules.xt.model.introductor.DynamicIntroductor;
import org.apache.commons.collections.map.ReferenceIdentityMap;

/**
 * {@link java.util.List} decorator, which makes all the contained objects implementing a given array of interfaces, while leaving
 * the original target list unmodified.<br>
 * This builds on top of {@link DynamicIntroductor} implementations.<br>
 * <b>Important</b>: This decorated list is <b>immutable</b>.
 * 
 * @author Sergio Bossa
 */
public class IntroductorList extends AbstractList {
    
    private List target;
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
     * Construct the decorated introductor list, starting from a target list which will not be modified.
     * @param target The list to decorate, whose objects must be introduced.
     * @param introducedInterfaces The interfaces to introduce.
     * @param tatargetObjectsInterfaceshe interfaces of the objects into the list.
     * @param introductor The concrete {@link DynamicIntroductor} to use for introducing interfaces.
     */
    public IntroductorList(List target, Class[] introducedInterfaces, Class[] targetObjectsInterfaces, DynamicIntroductor introductor) {
        this.target = target;
        this.introducedInterfaces = introducedInterfaces;
        this.targetObjectsInterfaces = targetObjectsInterfaces;
        this.introductor = introductor;
    }
    
    /**
     * Construct the decorated introductor List, starting from a target list which will not be modified.
     * @param target The list to decorate, whose objects must be introduced.
     * @param interfaces The interfaces to introduce.
     * @param introductor The concrete {@link DynamicIntroductor} to use for introducing interfaces.
     */
    public IntroductorList(List target, Class[] interfaces, DynamicIntroductor introductor) {
        this.target = target;
        this.introducedInterfaces = interfaces;
        this.introductor = introductor;
    }
    
    public Object get(int index) {
        Object o = this.target.get(index);
        return this._get(o);
    }

    public int size() {
        return this.target.size();
    }

    public boolean contains(Object o) {
        return this.target.contains(o);
    }
    
    public boolean containsAll(Collection c) {
        return this.target.containsAll(c);
    }
    
    public Iterator iterator() {
        return new IntroductorIterator(this.target.listIterator());
    }
    
    public ListIterator listIterator() {
        return new IntroductorIterator(this.target.listIterator());
    }

    public void add(int index, Object element) {
        this.target.add(index, element);
    }
    
    public boolean add(Object element) {
        return this.target.add(element);
    }

    public Object set(int index, Object element) {
        return this.target.set(index, element);
    }

    public boolean remove(Object o) {
        boolean removed = this.target.remove(o);
        if (removed) {
            this.cachedValues.remove(o);
        }
        return removed;
    }
    
    public Object remove(int index) {
        Object removed = this.target.remove(index);
        if (removed != null) {
            this.cachedValues.remove(removed);
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
    
    private Object _get(Object currentSourceObject) {
            Object cachedObject = this.cachedValues.get(currentSourceObject);
            
            // If an object corresponding to the current one has been already cached, return it:
            if (cachedObject != null) {
                return cachedObject;
            } 
            // If there's no cached object, make a new introduction:
            else {
                Object result = this.introduce(currentSourceObject);
                this.cachedValues.put(currentSourceObject, result);
                return result;
            }
        }
    
    private class IntroductorIterator implements ListIterator {
        
        private ListIterator sourceIt;
        private Object currentSourceObject;
        private int index = 0;
        
        public IntroductorIterator(ListIterator it) {
            this.sourceIt = it;
        }
        
        public boolean hasPrevious() {
            return this.sourceIt.hasPrevious();
        }
        
        public boolean hasNext() {
            return this.sourceIt.hasNext();
        }

        public int previousIndex() {
            return this.index-1;
        }

        public int nextIndex() {
            return this.index;
        }

        public Object previous() {
            this.currentSourceObject = this.sourceIt.previous();
            this.index--;
            return IntroductorList.this._get(currentSourceObject);
        }

        public Object next() {
            this.currentSourceObject = this.sourceIt.next();
            this.index++;
            return IntroductorList.this._get(currentSourceObject);
        }

        public void add(Object o) {
            this.sourceIt.add(o);
        }

        public void remove() {
            this.sourceIt.remove();
            IntroductorList.this.cachedValues.remove(this.currentSourceObject);
        }

        public void set(Object o) {
            this.sourceIt.set(o);
        }
    }
}
