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

package org.springmodules.validation.util.bel.resolver;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;
import org.springmodules.validation.bean.Person;
import org.springmodules.validation.valang.functions.UpperCaseFunction;

/**
 * Tests for {@link org.springmodules.validation.util.bel.resolver.ValangFunctionExpressionResolver}.
 *
 * @author Uri Boness
 */
public class ValangFunctionExpressionResolverTests extends TestCase {

    public void testResolve_WithSimpleProperty() throws Exception {
        ValangFunctionExpressionResolver resolver = new ValangFunctionExpressionResolver();
        Object name = resolver.resolve(new Person("Uri"), "name");
        assertEquals("Uri", name);
    }

    public void testResolve_WithNestedProperty() throws Exception {
        ValangFunctionExpressionResolver resolver = new ValangFunctionExpressionResolver();
        Object className = resolver.resolve(new Person("Uri"), "name.class.name");
        assertEquals("java.lang.String", className);
    }

    public void testResolve_WithPredefinedFunction() throws Exception {
        ValangFunctionExpressionResolver resolver = new ValangFunctionExpressionResolver();
        Object nameLength = resolver.resolve(new Person("Uri"), "length(name)");
        assertEquals(new Integer(3), nameLength);
    }

    public void testResolve_WithCustomFunction() throws Exception {
        ValangFunctionExpressionResolver resolver = new ValangFunctionExpressionResolver();
        Map functions = new HashMap();
        functions.put("tupper", UpperCaseFunction.class.getName());
        resolver.setCustomFunctions(functions);
        Object upperCaseName = resolver.resolve(new Person("Uri"), "tupper(name)");
        assertEquals("URI", upperCaseName);
    }

}