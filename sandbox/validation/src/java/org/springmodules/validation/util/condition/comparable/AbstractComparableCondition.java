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

package org.springmodules.validation.util.condition.comparable;

import org.springmodules.validation.util.condition.NonNullAcceptingTypeSpecificCondition;

/**
 * A base class for all comparable conditions.
 *
 * @author Uri Boness
 */
public abstract class AbstractComparableCondition extends NonNullAcceptingTypeSpecificCondition {

    /**
     * Constructs a new AbstractComparableCondition.
     */
    public AbstractComparableCondition() {
        super(Comparable.class);
    }

    /**
     * See {@link NonNullAcceptingTypeSpecificCondition#doCheck(Object)}.
     *
     * Delegates to {@link #check(Comparable)}.
     */
    public final boolean doCheck(Object bean) {
        return check((Comparable)bean);
    }

    /**
     * Checks the instantCondition upon the given comparable object.
     *
     * @param comparable The checked comparable object.
     * @return <code>true</code> if the given comparable adheres to this instantCondition, <code>false</code> otherwise.
     */
    protected abstract boolean check(Comparable comparable);

}
