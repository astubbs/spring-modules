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

package org.springmodules.template.web;

import java.io.*;
import java.util.*;

import junit.framework.*;
import org.easymock.*;
import org.springmodules.template.*;

/**
 * Tests the {@link TemplateSetViewResolver} class.
 *
 * @author Uri Boness
 */
public class TemplateSetViewResolverTest extends TestCase {

    private MockControl templateSetControl;
    private TemplateSet templateSet;
    private TemplateSetViewResolver templateSetViewResolver;

    protected void setUp() throws Exception {
        templateSetControl = MockControl.createControl(TemplateSet.class);
        templateSet = (TemplateSet)templateSetControl.getMock();
        templateSetViewResolver = new TemplateSetViewResolver();
        templateSetViewResolver.setTemplateSet(templateSet);
    }

    public void testResolveTemplateWithLocaleToStringFound() throws Exception {

        Template dummy = new DummyTemplate();
        templateSetControl.expectAndReturn(templateSet.getTemplate("templateName_en_US"), dummy);

        templateSetControl.replay();

        Locale locale = Locale.US;
        Template template = templateSetViewResolver.resolveTemplate("templateName", locale);

        assertSame(dummy, template);
    }

    public void testResolveTemplateWithLocaleLanguageFound() throws Exception {

        Template dummy = new DummyTemplate();
        templateSetControl.expectAndReturn(templateSet.getTemplate("templateName_en_US"), null);
        templateSetControl.expectAndReturn(templateSet.getTemplate("templateName_en"), dummy);

        templateSetControl.replay();

        Locale locale = Locale.US;
        Template template = templateSetViewResolver.resolveTemplate("templateName", locale);

        assertSame(dummy, template);
    }

    public void testResolveTemplateWithLocaleCountryFound() throws Exception {

        Template dummy = new DummyTemplate();
        templateSetControl.expectAndReturn(templateSet.getTemplate("templateName_en_US"), null);
        templateSetControl.expectAndReturn(templateSet.getTemplate("templateName_en"), null);
        templateSetControl.expectAndReturn(templateSet.getTemplate("templateName_US"), dummy);

        templateSetControl.replay();

        Locale locale = Locale.US;
        Template template = templateSetViewResolver.resolveTemplate("templateName", locale);

        assertSame(dummy, template);
    }

    public void testResolveTemplateWithNoLocaleSpecificFound() throws Exception {

        Template dummy = new DummyTemplate();
        templateSetControl.expectAndReturn(templateSet.getTemplate("templateName_en_US"), null);
        templateSetControl.expectAndReturn(templateSet.getTemplate("templateName_en"), null);
        templateSetControl.expectAndReturn(templateSet.getTemplate("templateName_US"), null);
        templateSetControl.expectAndReturn(templateSet.getTemplate("templateName"), dummy);

        templateSetControl.replay();

        Locale locale = Locale.US;
        Template template = templateSetViewResolver.resolveTemplate("templateName", locale);

        assertSame(dummy, template);
    }


    //============================================= Inner Classes =====================================================

    // a dummy template implementation to help with the mocking.
    private static class DummyTemplate implements Template {

        public void generate(OutputStream out, Map model) throws TemplateGenerationException {
        }

        public void generate(Writer writer, Map model) throws TemplateGenerationException {
        }

        public String generate(Map model) throws TemplateGenerationException {
            return null;
        }
    }
}