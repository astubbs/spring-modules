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

import java.util.Calendar;
import java.util.Date;

import org.joda.time.Instant;
import org.joda.time.ReadableInstant;

/**
 * Tests for {@link IsBeforeInstantCondition}.
 *
 * @author Uri Boness
 */
public class IsBeforeInstantConditionTests extends AbstractInstantConditionTests {

    private ReadableInstant earlierInstant;
    private ReadableInstant nowInstant;
    private ReadableInstant laterInstant;

    private Date nowDate;
    private Calendar nowCalendar;

    public IsBeforeInstantConditionTests() {
        nowCalendar = Calendar.getInstance();
        nowDate = nowCalendar.getTime();

        nowInstant = new Instant(nowCalendar.getTimeInMillis());
        earlierInstant = createEarlierInstant(nowInstant);
        laterInstant = createLaterInstant(nowInstant);
    }

    protected AbstractInstantCondition createInstantConditionWithInstant() {
        return new IsBeforeInstantCondition(nowInstant);
    }

    protected AbstractInstantCondition createInstantConditionWithDate() {
        return new IsBeforeInstantCondition(nowDate);
    }

    protected AbstractInstantCondition createInstantConditionWithCalendar() {
        return new IsBeforeInstantCondition(nowCalendar);
    }


    public void testCheck_Success() throws Exception {
        assertTrue(instantCondition.check(earlierInstant));
        assertTrue(dateCondition.check(earlierInstant));
        assertTrue(calendarCondition.check(earlierInstant));
    }

    public void testCheck_Failure() throws Exception {
        assertFalse(instantCondition.check(laterInstant));
        assertFalse(dateCondition.check(laterInstant));
        assertFalse(calendarCondition.check(laterInstant));
    }

    public void testGetEarlier() throws Exception {
        assertEquals(nowInstant, ((IsBeforeInstantCondition)instantCondition).getLater());
        assertEquals(nowDate.getTime(), ((IsBeforeInstantCondition)instantCondition).getLater().getMillis());
        assertEquals(nowCalendar.getTimeInMillis(), ((IsBeforeInstantCondition)instantCondition).getLater().getMillis());
    }

    //=============================================== Helper Methods ===================================================

    protected ReadableInstant createEarlierInstant(ReadableInstant now) {
        return new Instant(now).minus(3*60*60*1000);
    }

    protected ReadableInstant createLaterInstant(ReadableInstant now) {
        return new Instant(now).plus(3*60*60*1000);
    }

}
