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

package org.springmodules.validation.util.condition.date;

import java.util.Calendar;
import java.util.Date;

/**
 * Tests for {@link IsBeforeDateCondition}.
 *
 * @author Uri Boness
 */
public class IsBeforeDateConditionTests extends AbstractDateConditionTests {

    private Date earlierDate;
    private Date nowDate;
    private Date laterDate;

    private Calendar earlierCalendar;
    private Calendar nowCalendar;
    private Calendar laterCalendar;

    public IsBeforeDateConditionTests() {
        earlierDate = createEarlierCalendar().getTime();
        nowDate = new Date();
        laterDate = createLaterCalendar().getTime();
        earlierCalendar = createEarlierCalendar();
        nowCalendar = Calendar.getInstance();
        laterCalendar = createLaterCalendar();
    }

    protected AbstractDateCondition createDateCondition() {
        return new IsBeforeDateCondition(nowDate);
    }

    protected AbstractDateCondition createCalendarCondition() {
        return new IsBeforeDateCondition(nowCalendar);
    }

    public void testConstructor_WithNull() throws Exception {
        try {
            new IsBeforeDateCondition((Date)null);
            fail("An IllegalArgumentException must be thrown when initialized with null");
        } catch (IllegalArgumentException iae) {
            // expected
        }

        try {
            new IsBeforeDateCondition((Calendar)null);
            fail("An IllegalArgumentException must be thrown when initialized with null");
        } catch (IllegalArgumentException iae) {
            // expected
        }
    }

    public void testCheck_SuccessWithCalendar() throws Exception {
        assertTrue(dateCondition.check(earlierCalendar));
        assertTrue(calendarCondition.check(earlierCalendar));
    }

    public void testCheck_FailureWithCalendar() throws Exception {
        assertFalse(dateCondition.check(laterCalendar));
        assertFalse(calendarCondition.check(laterCalendar));
    }

    public void testCheck_SuccessWithDate() throws Exception {
        assertTrue(dateCondition.check(earlierDate));
        assertTrue(calendarCondition.check(earlierDate));
    }

    public void testCheck_FailureWithDate() throws Exception {
        assertFalse(dateCondition.check(laterDate));
        assertFalse(calendarCondition.check(laterDate));
    }

    public void testGetLater_WithDate() throws Exception {
        assertEquals(nowDate, ((IsBeforeDateCondition)dateCondition).getLater().getTime());
    }

    public void testGetEarlier_WithCalendar() throws Exception {
        assertEquals(nowCalendar, ((IsBeforeDateCondition)calendarCondition).getLater());
    }

    //=============================================== Helper Methods ===================================================

    protected Calendar createEarlierCalendar() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR, -3);
        return cal;
    }

    protected Calendar createLaterCalendar() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR, 3);
        return cal;
    }

}
