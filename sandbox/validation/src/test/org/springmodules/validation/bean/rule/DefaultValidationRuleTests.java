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

package org.springmodules.validation.bean.rule;

import junit.framework.TestCase;
import org.springmodules.validation.util.condition.Conditions;

/**
 * Tests for {@link org.springmodules.validation.bean.rule.DefaultValidationRule}.
 *
 * @author Uri Boness
 */
public class DefaultValidationRuleTests extends TestCase {

    public void testIsApplicable_WithExplicitApplicabilityCondition() throws Exception {
        DefaultValidationRule rule = new DefaultValidationRule(null, Conditions.notNull(), "errorCode");
        assertTrue(rule.isApplicable(new Object()));
        assertFalse(rule.isApplicable(null));
    }

    public void testIsApplicable_WithoutAnExplicitApplicabilityCondition() throws Exception {
        DefaultValidationRule rule = new DefaultValidationRule(null, "errorCode");
        assertTrue(rule.isApplicable(new Object()));
        assertTrue(rule.isApplicable(null));
    }

}