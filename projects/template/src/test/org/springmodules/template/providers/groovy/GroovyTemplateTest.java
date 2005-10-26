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

package org.springmodules.template.providers.groovy;

import java.io.*;
import java.util.*;

import groovy.text.*;
import groovy.text.Template;
import org.springmodules.template.*;

/**
 * Tests the GroovyTemplate class;
 *
 * @author Uri Boness
 */
public class GroovyTemplateTest extends AbstractTemplateTest {

    protected org.springmodules.template.Template createTemplateForCommonModel() throws Exception {
        String templateText = "Name is ${person.name} and age is ${person.age}";
        Template template = new SimpleTemplateEngine().createTemplate(templateText);
        return new GroovyTemplate(template);
    }

    protected String getOutputForCommonModel() {
        return "Name is John and age is 40";
    }


    //================================================ Extra Tests =====================================================

    public void testGenerateWithSimpleLoop() throws Exception {

        String templateText = "<% 3.times { %>${number}<% } %>";
        Template template = new SimpleTemplateEngine().createTemplate(templateText);

        GroovyTemplate groovyTemplate = new GroovyTemplate(template);

        StringWriter writer = new StringWriter();
        Map model = new HashMap();
        model.put("number", "1");
        groovyTemplate.generate(writer, model);

        assertEquals("expected generated text", "111", writer.toString());
    }

}