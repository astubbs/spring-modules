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
 * Tests for {@link IsNullCondition}.
 *
 * @author Uri Boness
 */
public class IsNullConditionTests extends TestCase {

    public void testCheck_Success() throws Exception {
        assertTrue(new IsNullCondition().check(null));
    }

    public void testCheck_Failure() throws Exception {
        assertFalse(new IsNullCondition().check("not null"));
    }

}