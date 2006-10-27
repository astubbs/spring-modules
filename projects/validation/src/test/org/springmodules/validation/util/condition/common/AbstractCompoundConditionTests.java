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

package org.springmodules.validation.util.condition.common;

import java.util.Collection;

import junit.framework.TestCase;
import org.springmodules.validation.util.condition.AbstractCondition;
import org.springmodules.validation.util.condition.Condition;

/**
 * A base class for all the compound condition tests.
 *
 * @author Uri Boness
 */
public abstract class AbstractCompoundConditionTests extends TestCase {

    public void testConstructor_WithNull() throws Exception {
        try {
            createCondition((Condition[]) null);
            fail("An IllegalArgumentException must be thrown if the AndCondition is initialized with null");
        } catch (IllegalArgumentException iae) {
            // expected
        }

        try {
            createCondition((Collection) null);
            fail("An IllegalArgumentException must be thrown if the AndCondition is initialized with null");
        } catch (IllegalArgumentException iae) {
            // expected
        }
    }

    protected abstract AbstractCompoundCondition createCondition(Collection conditions);

    protected abstract AbstractCompoundCondition createCondition(Condition[] conditions);

    //=============================================== Helper Methods ===================================================

    protected Condition createTrueCondition() {
        return new AbstractCondition() {
            public boolean doCheck(Object object) {
                return true;
            }
        };
    }

    protected Condition createFalseCondition() {
        return new AbstractCondition() {
            public boolean doCheck(Object object) {
                return false;
            }
        };
    }

}
