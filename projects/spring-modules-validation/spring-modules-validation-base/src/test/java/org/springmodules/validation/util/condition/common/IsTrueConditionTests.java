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

/**
 * Tests for {@link IsTrueCondition}.
 *
 * @author Uri Boness
 */
public class IsTrueConditionTests extends TestCase {

    public void testCheck_WithNonBooleanValue() throws Exception {
        try {
            new IsTrueCondition().check("non boolean");
            fail("An IllegalArgumentException must be thrown if the checked value is not of a boolean type");
        } catch (IllegalArgumentException iae) {
            // expected
        }
    }

    public void testCheck_Success() throws Exception {
        assertTrue(new IsTrueCondition().check(Boolean.TRUE));
    }

    public void testCheck_Failure() throws Exception {
        assertFalse(new IsTrueCondition().check(Boolean.FALSE));
    }

}