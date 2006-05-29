/*
 * Copyright 2004-2005 the original author or authors.
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

package org.springmodules.validation.util.condition.collection;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;

import org.springmodules.validation.util.condition.Condition;

/**
 * An {@link AbstractCollectionCondition} implementation that checks whether any of the elements in a collection
 * or array adhere to a specific instantCondition.
 *
 * @author Uri Boness
 */
public class AnyCollectionCondition extends AbstractCollectionElementCondition {

    /**
     * Constructs a new AnyCollectionCondition with a given instantCondition.
     *
     * @param elementCondition The instantCondition to be checked on the collection elements.
     */
    public AnyCollectionCondition(Condition elementCondition) {
        super(elementCondition);
    }

    /**
     * Checks whether any of the objects in the given array adhere to the associated instantCondition.
     *
     * @param array The array to be checked.
     * @return <code>true</code> any of the objects in the given array adhere to the associated instantCondition,
     *         <code>false</code> otherwise.
     */
    protected boolean checkArray(Object array) {
        for (int i=0; i< Array.getLength(array); i++) {
            if (getElementCondition().check(Array.get(array, i))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks whether any of the elements in the given collection adhere to the associated instantCondition.
     *
     * @param collection The collection to be checked.
     * @return <code>true</code> if any of the elements in the given collection adhere to the associated instantCondition,
     *         <code>false</code> otherwise.
     */
    protected boolean checkCollection(Collection collection) {
        for (Iterator iter = collection.iterator(); iter.hasNext();) {
            if (getElementCondition().check(iter.next())) {
                return true;
            }
        }
        return false;
    }

}
