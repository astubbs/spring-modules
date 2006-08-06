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

package org.springmodules.validation.bean.converter;

import junit.framework.TestCase;

/**
 * Tests for {@link org.springmodules.validation.bean.converter.ModelAwareErrorCodeConverter}.
 *
 * @author Uri Boness
 */
public class ModelAwareErrorCodeConverterTests extends TestCase {

    public void testConvertGlobalErrorCode_WhenNotUsingFQN() throws Exception {
        ModelAwareErrorCodeConverter converter = new ModelAwareErrorCodeConverter();
        String code = converter.convertGlobalErrorCode("errorCode", Object.class);
        assertEquals("Object[errorCode]", code);
    }

    public void testConvertPropertyErrorCode_WhenNotUsingFQN() throws Exception {
        ModelAwareErrorCodeConverter converter = new ModelAwareErrorCodeConverter();
        String code = converter.convertPropertyErrorCode("errorCode", Object.class, "name");
        assertEquals("Object.name[errorCode]", code);
    }

    public void testConvertGlobalErrorCode_WhenUsingFQN() throws Exception {
        ModelAwareErrorCodeConverter converter = new ModelAwareErrorCodeConverter(true);
        String code = converter.convertGlobalErrorCode("errorCode", Object.class);
        assertEquals("java.lang.Object[errorCode]", code);
    }

    public void testConvertPropertyErrorCode_WhenUsingFQN() throws Exception {
        ModelAwareErrorCodeConverter converter = new ModelAwareErrorCodeConverter(true);
        String code = converter.convertPropertyErrorCode("errorCode", Object.class, "name");
        assertEquals("java.lang.Object.name[errorCode]", code);
    }

}