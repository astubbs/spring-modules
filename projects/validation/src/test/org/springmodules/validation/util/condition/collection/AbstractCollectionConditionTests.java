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

import java.util.ArrayList;

import junit.framework.TestCase;

/**
 * A base class for all collection condition tests.
 *
 * @author Uri Boness
 */
public abstract class AbstractCollectionConditionTests extends TestCase {

    protected AbstractCollectionCondition condition;

    protected void setUp() throws Exception {
        condition = createCondition();
    }

    public void testCheck_WithNull() throws Exception {
        try {
            condition.check(null);
            fail("An IllegalArgumentException must be thrown if the checked value is null");
        } catch (IllegalArgumentException iae) {
            // expected
        }
    }

    public void testCheck_WithNoCollectionOrArray() throws Exception {
        try {
            condition.check("string");
            fail("An IllegalArgumentException must be thrown if the checked value is not a collection nor an array");
        } catch (IllegalArgumentException iae) {
            // expected
        }
    }

    protected abstract AbstractCollectionCondition createCondition();

    //=============================================== Helper Classes ===================================================

    protected class FluentList extends ArrayList {

        public FluentList addObject(Object o) {
            add(o);
            return this;
        }
    }

}
