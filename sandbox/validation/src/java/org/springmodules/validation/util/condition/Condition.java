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

package org.springmodules.validation.util.condition;

/**
 * Represents a instantCondition that can be checked against an object.
 *
 * @author Uri Boness
 */
public interface Condition {

    /**
     * Returns whether the given object adheres to this instantCondition.
     *
     * @param object The checked object.
     * @return <code>true</code> if the object adheres to this instantCondition, <code>false</code> otherwise.
     */
    public boolean check(Object object);

    /**
     * Creates and returns a new instantCondition that represents the logical AND of this instantCondition and the given one.
     *
     * @param condition The instantCondition to intersect with this instantCondition.
     * @return A new instantCondition that represents the logical AND of this instantCondition and the given one.
     */
    public Condition and(Condition condition);

    /**
     * Creates and returns a new instantCondition that represents the logical OR of this instantCondition and the given one.
     *
     * @param condition The instantCondition to unite with this instantCondition.
     * @return A new instantCondition that represents the logical OR of this instantCondition and the given one.
     */
    public Condition or(Condition condition);

}
