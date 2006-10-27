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

import java.util.Collection;

/**
 * Tests for {@link MinSizeCollectionCondition}.
 *
 * @author Uri Boness
 */
public class MinSizeCollectionConditionTests extends AbstractCollectionConditionTests {

    protected AbstractCollectionCondition createCondition() {
        return new MinSizeCollectionCondition(2);
    }

    public void testConstructor_WithNegativeMaxSize() throws Exception {
        try {
            new MinSizeCollectionCondition(-1);
            fail("An IllegalArgumentException must be thrown if given min size is nagative");
        } catch (IllegalArgumentException iae) {
            // expected
        }
    }

    public void testCheck_WithBiggerCollection() throws Exception {
        Collection collection = new FluentList().addObject("1").addObject("2").addObject("3");
        assertTrue(condition.check(collection));
    }

    public void testCheck_WithSmallerCollection() throws Exception {
        Collection collection = new FluentList().addObject("1");
        assertFalse(condition.check(collection));
    }

    public void testCheck_WithExactCollection() throws Exception {
        Collection collection = new FluentList().addObject("1").addObject("2");
        assertTrue(condition.check(collection));
    }

    public void testCheck_WithBiggerArray() throws Exception {
        Object[] array = new Object[]{"1", "2", "3"};
        assertTrue(condition.check(array));
    }

    public void testCheck_WithSmallerArray() throws Exception {
        Object[] array = new Object[]{"1"};
        assertFalse(condition.check(array));
    }

    public void testCheck_WithExactArray() throws Exception {
        Object[] array = new Object[]{"1", "2"};
        assertTrue(condition.check(array));
    }

}
