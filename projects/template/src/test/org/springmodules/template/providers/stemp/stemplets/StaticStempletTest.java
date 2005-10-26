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

package org.springmodules.template.providers.stemp.stemplets;

import java.io.*;
import java.util.*;

import junit.framework.*;

/**
 * A test class for {@link StaticStemplet}
 *
 * @author Uri Boness
 */
public class StaticStempletTest extends TestCase {

    private StaticStemplet staticStemplet;

    private String staticText;

    protected void setUp() throws Exception {
        staticText = "some static text";
        staticStemplet = new StaticStemplet(staticText);
    }

    /**
     * Tests the {@link StaticStemplet#generate(java.io.Writer, java.util.Map, java.util.Map)} method.
     */
    public void testGenerate() throws Exception {
        StringWriter writer = new StringWriter();
        staticStemplet.generate(writer, new HashMap(), new HashMap());

        assertEquals("StaticStemplet.generate(...) should \"generate\" the static text", staticText, writer.toString());
    }

    /**
     * Tests the {@link StaticStemplet#dump(java.io.Writer)} method.
     */
    public void testDump() throws Exception {
        StringWriter writer = new StringWriter();
        staticStemplet.dump(writer);

        assertEquals("StaticStemplet.generate(...) should \"generate\" the static text", staticText, writer.toString());
    }

    /**
     * Tests the {@link org.springmodules.template.providers.stemp.stemplets.StaticStemplet#getExpressions()} method.
     */
    public void testGetExpressions() throws Exception {
        String[] expressions = staticStemplet.getExpressions();

        assertNotNull(expressions);
        assertEquals("StaticStemplet should have no expressions", 0, expressions.length);
    }
}