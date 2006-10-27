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

package org.springmodules.validation.bean.rule.resolver;

import junit.framework.TestCase;
import org.springmodules.validation.bean.Person;

/**
 * Tests for {@link org.springmodules.validation.bean.rule.resolver.ValangErrorArgumentsResolver}.
 *
 * @author Uri Boness
 */
public class ValangErrorArgumentsResolverTests extends TestCase {

    public void testResolveArguments_WithPath() throws Exception {
        ValangErrorArgumentsResolver resolver = new ValangErrorArgumentsResolver(new String[]{"name.class.name"});
        Object[] args = resolver.resolveArguments(new Person("Uri"));

        assertNotNull(args);
        assertEquals(1, args.length);
        assertEquals("java.lang.String", args[0]);
    }

    public void testResolveArguments_WithFunction() throws Exception {
        ValangErrorArgumentsResolver resolver = new ValangErrorArgumentsResolver(new String[]{"length(name)"});
        Object[] args = resolver.resolveArguments(new Person("Uri"));

        assertNotNull(args);
        assertEquals(1, args.length);
        assertEquals(new Integer(3), args[0]);
    }

    public void testResolveArguments_WithNoExpressions() throws Exception {
        ValangErrorArgumentsResolver resolver = new ValangErrorArgumentsResolver(new String[0]);
        Object[] args = resolver.resolveArguments(new Object());

        assertNotNull(args);
        assertEquals(0, args.length);
    }
}