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

package org.springmodules.template.providers.freemarker;

import java.util.*;

import freemarker.cache.*;
import freemarker.template.*;
import org.springmodules.template.Template;
import org.springmodules.template.*;
import org.springmodules.template.resolvers.*;
import org.springmodules.template.sources.*;

/**
 * Tests the FreemarkerTemplate class.
 *
 * @author Uri Boness
 */
public class FreemarkerTemplateTest extends AbstractTemplateTest {

    protected Template createTemplateForCommonModel() throws Exception {
        String templateText = "Name is ${person.name} and age is ${person.age}";

        StringTemplateLoader templateLoader = new StringTemplateLoader();
        templateLoader.putTemplate("temp", templateText);
        Configuration configuration = new Configuration();
        configuration.setTemplateLoader(templateLoader);
        return new FreemarkerTemplate(configuration.getTemplate("temp"));
    }

    protected String getOutputForCommonModel() {
        return "Name is John and age is 40";
    }


    //================================================ Extra Tests =====================================================

    public void testTemplateSetWithSourcesArray() throws Exception {

        String lib = "<#macro greeting name>Hello ${name}</#macro>";
        String main = "<#import \"lib\" as my><@my.greeting \"John\"/>";

        FreemarkerTemplateFactory factory = new FreemarkerTemplateFactory();

        TemplateSet set = factory.createTemplateSet(new TemplateSource[] {
            new StringTemplateSource("lib", lib),
            new StringTemplateSource("main", main)
        });

        Template template = set.getTemplate("main");
        String output = template.generate(new HashMap());

        assertEquals("Hello John", output);
    }

    public void testTemplateSetWithSourceResolver() throws Exception {

        String lib = "<#macro greeting name>Hello ${name}</#macro>";
        String main = "<#import \"lib\" as my><@my.greeting \"John\"/>";

        MapTemplateSourceResolver resolver = new MapTemplateSourceResolver();
        resolver.addTemplateSource("lib", new StringTemplateSource(lib));
        resolver.addTemplateSource("main", new StringTemplateSource(main));

        FreemarkerTemplateFactory factory = new FreemarkerTemplateFactory();
        TemplateSet set = factory.createTemplateSet(resolver);

        Template template = set.getTemplate("main");
        String output = template.generate(new HashMap());

        assertEquals("Hello John", output);
    }
}