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

package org.springmodules.validation.bean.conf.loader.xml;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;
import org.springframework.core.io.Resource;

/**
 * Tests for {@link org.springmodules.validation.bean.conf.loader.xml.AbstractResourceBasedBeanValidationConfigurationLoader}.
 *
 * @author Uri Boness
 */
public class AbstractResourceBasedBeanValidationConfigurationLoaderTests extends TestCase {

    private AbstractResourceBasedBeanValidationConfigurationLoader loader;

    protected void setUp() throws Exception {
        loader = new TestLoader();
    }

    public void testCreateDefaultConfigurationFileName() throws Exception {
        String fileName = loader.createDefaultConfigurationFileName(Person.class);
        assertEquals("Person.vld.xml", fileName);
    }

    private class TestLoader extends AbstractResourceBasedBeanValidationConfigurationLoader {
        protected Map loadConfigurations(Resource resource) {
            return new HashMap();
        }
    }
}