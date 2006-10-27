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
 * Tests for {@link IsAfterInstantCondition}.
 *
 * @author Uri Boness
 */
public class IsAfterInstantConditionTests extends AbstractInstantConditionTests {

    private ReadableInstant earlierInstant;

    private ReadableInstant nowInstant;

    private ReadableInstant laterInstant;

    private Date nowDate;

    private Calendar nowCalendar;

    public IsAfterInstantConditionTests() {
        nowCalendar = Calendar.getInstance();
        nowDate = nowCalendar.getTime();

        nowInstant = new Instant(nowCalendar.getTimeInMillis());
        earlierInstant = createEarlierInstant(nowInstant);
        laterInstant = createLaterInstant(nowInstant);
    }

    protected AbstractInstantCondition createInstantConditionWithInstant() {
        return new IsAfterInstantCondition(nowInstant);
    }

    protected AbstractInstantCondition createInstantConditionWithDate() {
        return new IsAfterInstantCondition(nowDate);
    }

    protected AbstractInstantCondition createInstantConditionWithCalendar() {
        return new IsAfterInstantCondition(nowCalendar);
    }


    public void testCheck_Success() throws Exception {
        assertTrue(instantCondition.check(laterInstant));
        assertTrue(dateCondition.check(laterInstant));
        assertTrue(calendarCondition.check(laterInstant));
    }

    public void testCheck_Failure() throws Exception {
        assertFalse(instantCondition.check(earlierInstant));
        assertFalse(dateCondition.check(earlierInstant));
        assertFalse(calendarCondition.check(earlierInstant));
    }

    public void testGetEarlier() throws Exception {
        assertEquals(nowInstant, ((IsAfterInstantCondition) instantCondition).getEarlier());
        assertEquals(nowDate.getTime(), ((IsAfterInstantCondition) dateCondition).getEarlier().getMillis());
        assertEquals(nowCalendar.getTimeInMillis(), ((IsAfterInstantCondition) calendarCondition).getEarlier().getMillis());
    }

    //=============================================== Helper Methods ===================================================

    protected ReadableInstant createEarlierInstant(ReadableInstant now) {
        return new Instant(now).minus(3 * 60 * 60 * 1000);
    }

    protected ReadableInstant createLaterInstant(ReadableInstant now) {
        return new Instant(now).plus(3 * 60 * 60 * 1000);
    }

}
