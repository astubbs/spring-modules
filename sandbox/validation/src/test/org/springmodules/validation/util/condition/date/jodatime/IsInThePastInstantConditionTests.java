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

package org.springmodules.validation.util.condition.date.jodatime;

import org.joda.time.Instant;
import org.joda.time.ReadableInstant;

/**
 * Tests for {@link IsInThePastInstantCondition}.
 *
 * @author Uri Boness
 */
public class IsInThePastInstantConditionTests extends AbstractInstantConditionTests {

    private ReadableInstant laterInstant;
    private ReadableInstant earlierInstant;

    public IsInThePastInstantConditionTests() {
        laterInstant = new Instant().plus(3*60*60*1000);
        earlierInstant = new Instant().minus(3*60*60*1000);
    }

    protected AbstractInstantCondition createInstantConditionWithInstant() {
        return new IsInThePastInstantCondition();
    }

    // not used in this test case
    protected AbstractInstantCondition createInstantConditionWithDate() {
        return new IsInTheFutureInstantCondition();
    }

    // not used in this test case
    protected AbstractInstantCondition createInstantConditionWithCalendar() {
        return new IsInTheFutureInstantCondition();
    }

    public void testCheck_Success() throws Exception {
        assertTrue(instantCondition.check(earlierInstant));
    }

    public void testCheck_Failure() throws Exception {
        assertFalse(instantCondition.check(laterInstant));
    }

}
