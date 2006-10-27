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

package org.springmodules.validation.bean.conf;

import junit.framework.TestCase;
import org.easymock.MockControl;
import org.springmodules.validation.bean.conf.loader.SimpleBeanValidationConfigurationLoader;

/**
 * Tests for {@link org.springmodules.validation.bean.conf.loader.SimpleBeanValidationConfigurationLoader}.
 *
 * @author Uri Boness
 */
public class SimpleBeanValidationConfigurationLoaderTests extends TestCase {

    private SimpleBeanValidationConfigurationLoader loader;

    protected void setUp() throws Exception {
        loader = new SimpleBeanValidationConfigurationLoader();
    }

    public void testSupports_WithoutRegisteredClass() throws Exception {
        assertFalse(loader.supports(Object.class));
    }

    public void testSupports_WithRegisteredClass() throws Exception {
        loader.setClassValidation(Object.class, null);
        assertTrue(loader.supports(Object.class));
    }

    public void testLoadConfiguration_WithoutRegisteredClass() throws Exception {
        assertNull(loader.loadConfiguration(Object.class));
    }

    public void testLoadConfiguration_WithRegisteredClass() throws Exception {
        MockControl configControl = MockControl.createControl(BeanValidationConfiguration.class);
        BeanValidationConfiguration config = (BeanValidationConfiguration) configControl.getMock();
        loader.setClassValidation(Object.class, config);
        assertEquals(config, loader.loadConfiguration(Object.class));
    }
}