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

package org.springmodules.validation.util.condition.collection;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Tests for {@link IsEmptyCollectionCondition}.
 *
 * @author Uri Boness
 */
public class IsEmptyCollectionConditionTests extends AbstractCollectionConditionTests {

    protected AbstractCollectionCondition createCondition() {
        return new IsEmptyCollectionCondition();
    }

    public void testCheck_WithEmptyArray() throws Exception {
        assertTrue(condition.check(new Object[0]));
    }

    public void testCheck_WithEmptyCollection() throws Exception {
        assertTrue(condition.check(new ArrayList()));
    }

    public void testCheck_WithNonEmptyArray() throws Exception {
        assertFalse(condition.check(new Object[] { "test" }));
    }

    public void testCheck_WithNonEmptyCollection() throws Exception {
        Collection collection = new FluentList().addObject("test");
        assertFalse(condition.check(collection));
    }

}