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

package org.springmodules.validation.util.condition.adapter;

import junit.framework.TestCase;
import org.apache.commons.collections.Predicate;
import org.easymock.MockControl;
import org.springmodules.validation.util.condition.Condition;

/**
 * Tests for {@link org.springmodules.validation.util.condition.adapter.CommonsPredicateCondition}.
 *
 * @author Uri Boness
 */
public class CommonsPredicateConditionTests extends TestCase {

    private MockControl predicateControl;
    private Predicate predicate;

    protected void setUp() throws Exception {
        predicateControl = MockControl.createControl(Predicate.class);
        predicate = (Predicate)predicateControl.getMock();
    }

    public void testConstructor_WithNullPredicate() throws Exception {
        try {
            new CommonsPredicateCondition(null);
            fail("An IllegalArgumentException must be thrown if the passed in predicate is null");
        } catch (IllegalArgumentException iae) {
            // expected
        }
    }

    public void testCheck_Success() throws Exception {
        Object object = new Object();
        predicateControl.expectAndReturn(predicate.evaluate(object), true);
        predicateControl.replay();

        Condition condition = new CommonsPredicateCondition(predicate);
        assertTrue(condition.check(object));

        predicateControl.verify();
    }

    public void testCheck_Failure() throws Exception {
        Object object = new Object();
        predicateControl.expectAndReturn(predicate.evaluate(object), false);
        predicateControl.replay();

        Condition condition = new CommonsPredicateCondition(predicate);
        assertFalse(condition.check(object));

        predicateControl.verify();
    }
}