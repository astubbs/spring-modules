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

import java.util.*;
import java.io.*;

import junit.framework.*;
import org.springmodules.template.providers.stemp.stemplets.ExpressionStemplet;
import org.springmodules.template.providers.stemp.*;
import org.easymock.*;

/**
 * Tests the {@link ExpressionStemplet} class.
 *
 * @author Uri Boness
 */
public class ExpressionStempletTest extends TestCase {

    private ExpressionStemplet stemplet;

    private MockControl resolverControl;
    private ExpressionResolver resolver;
    private ExpressionWrapping wrapping;

    protected void setUp() throws Exception {
        resolverControl = MockControl.createControl(ExpressionResolver.class);
        resolver = (ExpressionResolver)resolverControl.getMock();

        wrapping = new ExpressionWrapping("#");
    }

    /**
     * Tests the {@link ExpressionStemplet#generate(java.io.Writer, java.util.Map, java.util.Map)} method.
     */
    public void testGenerate() throws Exception {

        Map model = new HashMap();
        String expression = "exp";

        resolverControl.expectAndReturn(resolver.resolve(expression, model), "result");
        resolverControl.replay();

        stemplet = new ExpressionStemplet(expression, resolver, wrapping);
        StringWriter writer = new StringWriter();
        stemplet.generate(writer, model, new HashMap());

        assertEquals("generation result should be resolved expression", "result", writer.toString());
        resolverControl.verify();
    }

    /**
     * Tests the {@link ExpressionStemplet#dump(java.io.Writer)}
     */
    public void testDump() throws Exception {
        resolverControl.replay(); // the resolver should not be called.

        String expression = "exp";
        stemplet = new ExpressionStemplet(expression, resolver, wrapping);

        StringWriter writer = new StringWriter();
        stemplet.dump(writer);

        assertEquals("dump should return the expression wrapped with the expression wrapping", "#exp#", writer.toString());

        resolverControl.verify(); // verifying the resolver wasn't called.
    }

    /**
     * Tests the {@link org.springmodules.template.providers.stemp.stemplets.ExpressionStemplet#getExpressions()} method.
     */
    public void testGetExpressions() throws Exception {
        resolverControl.replay(); // the resolver should not be called.

        String expression = "exp";
        stemplet = new ExpressionStemplet(expression, resolver, wrapping);

        String[] expressions = stemplet.getExpressions();

        assertNotNull(expressions);
        assertEquals("An exprssion stemplet should have only one expression", 1, expressions.length);
        assertEquals("exp", expressions[0]);

        resolverControl.verify(); // verifying the resolver wasn't called.
    }
}