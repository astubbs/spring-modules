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

package org.springmodules.template.providers.webmacro;

import java.util.*;

import junit.framework.*;
import org.easymock.*;
import org.webmacro.*;

/**
 * Tests the {@link DefaultContextFactory} class.
 *
 * @author Uri Boness
 */
public class DefaultContextFactoryTest extends TestCase {

    private DefaultContextFactory contextFactory;

    private WebMacro webMacro;
    private MockControl webMacroControl;

    protected void setUp() throws Exception {
        initMocks();
        contextFactory = new DefaultContextFactory(webMacro);
    }

    protected void initMocks() {
        webMacroControl = MockControl.createControl(WebMacro.class);
        webMacro = (WebMacro)webMacroControl.getMock();
    }

    protected void replayMocks() {
        webMacroControl.replay();
    }

    protected void verifyMocks() {
        webMacroControl.verify();
    }

    public void testCreateContext() throws Exception {
        Context context = new Context();
        webMacroControl.expectAndReturn(webMacro.getContext(), context);
        replayMocks();

        Map model = new HashMap();
        model.put("name", "value");
        Context c = contextFactory.createContext(model);

        assertNotNull(c);
        assertTrue(c.containsKey("name"));
        assertEquals("value", c.get("name"));

        verifyMocks();
    }
}