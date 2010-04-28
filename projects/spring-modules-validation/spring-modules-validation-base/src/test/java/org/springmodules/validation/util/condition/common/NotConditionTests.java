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

import junit.framework.TestCase;
import org.springmodules.validation.util.condition.AbstractCondition;
import org.springmodules.validation.util.condition.Condition;

/**
 * Tests for {@link NotCondition}.
 *
 * @author Uri Boness
 */
public class NotConditionTests extends TestCase {

    public void testConstructor_WithNullCondition() throws Exception {
        try {
            new NotCondition(null);
            fail("An IllegalArgumentException must be thrown when given condition is null");
        } catch (IllegalArgumentException iae) {
            // expected
        }
    }

    public void testCheck_Success() throws Exception {
        Condition condition = new NotCondition(createFalseCondition());
        assertTrue(condition.check(new Object()));
    }

    public void testCheck_Failure() throws Exception {
        Condition condition = new NotCondition(createTrueCondition());
        assertFalse(condition.check(new Object()));
    }

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