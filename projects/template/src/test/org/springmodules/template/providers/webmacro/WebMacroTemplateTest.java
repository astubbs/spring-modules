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

import java.io.*;
import java.util.*;

import org.easymock.*;
import org.springmodules.template.*;
import org.springmodules.template.Template;
import org.webmacro.*;
import org.webmacro.engine.*;

/**
 * Tests the {@link WebMacroTemplate} class.
 *
 * @author Uri Boness
 */
public class WebMacroTemplateTest extends AbstractTemplateTest {

    private WebMacro webMacro;
    private MockControl webMacroControl;

    private org.webmacro.Template webMacroTemplate;
    private MockControl webMacroTemplateControl;

    protected void setUp() throws Exception {
        super.setUp();

        webMacroControl = MockControl.createControl(WebMacro.class);
        webMacro = (WebMacro)webMacroControl.getMock();

        webMacroTemplateControl = MockControl.createControl(org.webmacro.Template.class);
        webMacroTemplate = (org.webmacro.Template)webMacroTemplateControl.getMock();
    }

    protected Template createTemplateForCommonModel() throws Exception {
        String templateText = "Name is $person.Name and age is $person.Age";

        WebMacro wm = new WM();
        StringTemplate wmTemplate = new StringTemplate(wm.getBroker(), templateText);
        return new WebMacroTemplate(wmTemplate, new DefaultContextFactory(wm));
    }

    protected String getOutputForCommonModel() {
        return "Name is John and age is 40";
    }


    //============================================== Extra Unit Tests ==================================================

    public void testGenerateWithOutputStream() throws Exception {

        OutputStream out = new ByteArrayOutputStream();
        Map model = new HashMap();
        model.put("name", "John");

        Context context = new Context();
        context.put("name", "John");

        webMacroControl.expectAndReturn(webMacro.getContext(), context);

        webMacroTemplate.write(out, context);
        webMacroTemplateControl.setMatcher(new ArgumentsMatcher() {
            public boolean matches(Object[] expectedArgs, Object[] actualArgs) {
                OutputStream out1 = (OutputStream)expectedArgs[0];
                OutputStream out2 = (OutputStream)actualArgs[0];
                if (out1 != out2) {
                    return false;
                }
                Context expected = (Context)expectedArgs[1];
                Context actual = (Context)actualArgs[1];
                return matchContexts(expected, actual);
            }
            public String toString(Object[] objects) {
                return "";
            }
        });

        webMacroTemplateControl.replay();
        webMacroControl.replay();

        WebMacroTemplate template = new WebMacroTemplate(webMacroTemplate, new DefaultContextFactory(webMacro));
        template.generate(out, model);

        webMacroControl.verify();
        webMacroTemplateControl.verify();
    }

    public void testGenerateWithWriter() throws Exception {

        StringWriter writer = new StringWriter();
        Map model = new HashMap();
        model.put("name", "John");

        Context context = new Context();
        context.put("name", "John");

        webMacroControl.expectAndReturn(webMacro.getContext(), context);

        webMacroTemplate.evaluateAsString(context);
        webMacroTemplateControl.setMatcher(new ArgumentsMatcher() {
            public boolean matches(Object[] expectedArgs, Object[] actualArgs) {
                Context expected = (Context)expectedArgs[0];
                Context actual = (Context)actualArgs[0];
                return matchContexts(expected, actual);
            }
            public String toString(Object[] objects) {
                return "";
            }
        });
        webMacroTemplateControl.setReturnValue("text");

        webMacroTemplateControl.replay();
        webMacroControl.replay();

        WebMacroTemplate template = new WebMacroTemplate(webMacroTemplate, new DefaultContextFactory(webMacro));
        template.generate(writer, model);

        assertEquals("text", writer.toString());

        webMacroControl.verify();
        webMacroTemplateControl.verify();
    }

    public void testGenerateWithString() throws Exception {

        Map model = new HashMap();
        model.put("name", "John");

        Context context = new Context();
        context.put("name", "John");

        webMacroControl.expectAndReturn(webMacro.getContext(), context);

        webMacroTemplate.evaluateAsString(context);
        webMacroTemplateControl.setMatcher(new ArgumentsMatcher() {
            public boolean matches(Object[] expectedArgs, Object[] actualArgs) {
                Context expected = (Context)expectedArgs[0];
                Context actual = (Context)actualArgs[0];
                return matchContexts(expected, actual);
            }
            public String toString(Object[] objects) {
                return "";
            }
        });
        webMacroTemplateControl.setReturnValue("text");

        webMacroTemplateControl.replay();
        webMacroControl.replay();

        WebMacroTemplate template = new WebMacroTemplate(webMacroTemplate, new DefaultContextFactory(webMacro));
        String text = template.generate(model);

        assertEquals("text", text);

        webMacroControl.verify();
        webMacroTemplateControl.verify();

    }


    //================================================= Helper methods =================================================

    private boolean matchContexts(Context c1, Context c2) {
        for (Iterator keys = c1.keySet().iterator(); keys.hasNext();) {
            Object key = keys.next();
            Object value = c1.get(key);
            if (!c2.containsKey(key) || !nullableEquals(value, c2.get(key))) {
                return false;
            }
        }
        return true;
    }

    private boolean nullableEquals(Object o1, Object o2) {
        if (o1 == o2) {
            return true;
        }
        if (o1 == null || o2 == null) {
            return false;
        }
        return o1.equals(o2);
    }

}