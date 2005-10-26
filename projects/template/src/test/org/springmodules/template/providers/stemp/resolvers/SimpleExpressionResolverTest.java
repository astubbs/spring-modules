/*
 * Copyright 2002-2005 the original author or authors.
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

package org.springmodules.template.providers.stemp.resolvers;

import java.util.*;

import junit.framework.*;
import org.springmodules.template.providers.stemp.resolvers.SimpleExpressionResolver;

/**
 * Tests the {@link SimpleExpressionResolver} class.
 *
 * @author Uri Boness
 */
public class SimpleExpressionResolverTest extends TestCase {

    private SimpleExpressionResolver resolver;

    protected void setUp() throws Exception {
        resolver = new SimpleExpressionResolver();
    }

    /**
     * Tests the {@link SimpleExpressionResolver#resolve(String, java.util.Map)}
     */
    public void testResolve() throws Exception {
        Map model = new HashMap();
        model.put("exp", "value");
        String result = resolver.resolve("exp", model);

        assertEquals("value", result);
    }
}