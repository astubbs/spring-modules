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

import junit.framework.TestCase;
import org.springmodules.validation.bean.Person;

/**
 * Tests for {@link org.springmodules.validation.util.bel.resolver.BeanWrapperExpressionResolver}.
 *
 * @author Uri Boness
 */
public class BeanWrapperExpressionResolverTests extends TestCase {

    public void testResolve_WithSimpleProperty() throws Exception {
        BeanWrapperExpressionResolver resolver = new BeanWrapperExpressionResolver();
        Object name = resolver.resolve(new Person("Uri"), "name");
        assertEquals("Uri", name);
    }

    public void testResolve_WithNestedProperty() throws Exception {
        BeanWrapperExpressionResolver resolver = new BeanWrapperExpressionResolver();
        Object className = resolver.resolve(new Person("Uri"), "name.class.name");
        assertEquals("java.lang.String", className);
    }

    public void testResolve_WithThisReference() throws Exception {
        BeanWrapperExpressionResolver resolver = new BeanWrapperExpressionResolver();
        Person person = new Person("Uri");
        Object resolvedObject = resolver.resolve(person, "@this");
        assertSame(person, resolvedObject);
    }

    public void testResolve_WithThisReferenceAndProperty() throws Exception {
        BeanWrapperExpressionResolver resolver = new BeanWrapperExpressionResolver();
        Person person = new Person("Uri");
        Object name = resolver.resolve(person, "@this.name");
        assertSame("Uri", name);
    }
}