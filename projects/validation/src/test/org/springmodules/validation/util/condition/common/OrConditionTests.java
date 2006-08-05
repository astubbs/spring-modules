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

import java.util.Collection;

import org.springmodules.validation.util.condition.Condition;

/**
 * Tests for {@link OrCondition}.
 *
 * @author Uri Boness
 */
public class OrConditionTests extends AbstractCompoundConditionTests {

    protected AbstractCompoundCondition createCondition(Collection conditions) {
        return new OrCondition(conditions);
    }

    protected AbstractCompoundCondition createCondition(Condition[] conditions) {
        return new OrCondition(conditions);
    }

    public void testCheck_WhenAllAreFalse() throws Exception {
        Condition condition = createCondition(new Condition[] {
            createFalseCondition(),
            createFalseCondition(),
            createFalseCondition()
        });
        assertFalse(condition.check(new Object()));
    }

    public void testCheck_WhenOneIsTrue() throws Exception {
        Condition condition = createCondition(new Condition[] {
            createFalseCondition(),
            createFalseCondition(),
            createTrueCondition()
        });
        assertTrue(condition.check(new Object()));
    }

}
