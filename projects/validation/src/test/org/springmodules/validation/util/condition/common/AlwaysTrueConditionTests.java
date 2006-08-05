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
import org.springmodules.validation.util.condition.Condition;

/**
 * Tests for {@link AlwaysTrueCondition}.
 *
 * @author Uri Boness
 */
public class AlwaysTrueConditionTests extends TestCase {

    private Condition condition;

    protected void setUp() throws Exception {
        condition = new AlwaysTrueCondition();
    }

    public void testCheck_WithNull() throws Exception {
        assertTrue(condition.check(null));
    }

    public void testCheck_WithObject() throws Exception {
        assertTrue(condition.check(new Object()));
    }
}