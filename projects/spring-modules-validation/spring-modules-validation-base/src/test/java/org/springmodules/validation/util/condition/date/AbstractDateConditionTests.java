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

import junit.framework.TestCase;

/**
 * A base class for all the date conditions test cases
 *
 * @author Uri Boness
 */
public abstract class AbstractDateConditionTests extends TestCase {

    protected AbstractDateCondition dateCondition;

    protected AbstractDateCondition calendarCondition;

    protected void setUp() throws Exception {
        dateCondition = createDateCondition();
        calendarCondition = createCalendarCondition();
    }

    public void testCheck_WithNonDateOrCalendar() throws Exception {
        try {
            dateCondition.check(new Integer(1));
            fail("An IllegalArgumentException should be thrown when a non-date and non-calendar value is being checked");
        } catch (IllegalArgumentException iae) {
            // expected
        }

        try {
            calendarCondition.check(new Integer(1));
            fail("An IllegalArgumentException should be thrown when a non-date and non-calendar value is being checked");
        } catch (IllegalArgumentException iae) {
            // expected
        }
    }

    public void testCheck_WithNull() throws Exception {
        try {
            dateCondition.check(null);
            fail("An IllegalArgumentException should be thrown when a null value is being checked");
        } catch (IllegalArgumentException iae) {
            // expected
        }

        try {
            calendarCondition.check(null);
            fail("An IllegalArgumentException should be thrown when a null value is being checked");
        } catch (IllegalArgumentException iae) {
            // expected
        }
    }

    protected abstract AbstractDateCondition createDateCondition();

    protected abstract AbstractDateCondition createCalendarCondition();

}
