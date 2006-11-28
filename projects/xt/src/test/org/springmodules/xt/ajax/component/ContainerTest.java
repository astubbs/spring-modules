/*
 * Copyright 2006 the original author or authors.
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

package org.springmodules.xt.ajax.component;

import java.util.Arrays;
import java.util.LinkedList;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.springmodules.xt.test.xml.XMLEnhancedTestCase;

/**
 *
 * @author Sergio Bossa
 */
public class ContainerTest extends XMLEnhancedTestCase {
    
    public ContainerTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(ContainerTest.class);
        
        return suite;
    }

    /**
     * Test of render method, of class org.springmodules.xt.ajax.component.SimpleText.
     */
    public void testRender() throws Exception {
        Container container = new Container(Container.Type.DIV, new LinkedList<Component>(Arrays.asList(new TaggedText("Text1"))));
        container.addAttribute("class", "myDivClass");
        container.addComponent(new TaggedText("Text2"));
        
        System.out.println(container.render());
        
        String rendering = container.render();
        assertXpathExists("/div/div[position()=1]", rendering);
        assertXpathExists("/div/div[position()=2]", rendering);
        
        container = new Container(Container.Type.SPAN, new LinkedList<Component>(Arrays.asList(new TaggedText("Text1", TaggedText.Tag.SPAN))));
        container.addAttribute("class", "mySpanClass");
        container.addComponent(new TaggedText("Text2", TaggedText.Tag.SPAN));
        
        System.out.println(container.render());
        
        rendering = container.render();
        assertXpathExists("/span/span[position()=1]", rendering);
        assertXpathExists("/span/span[position()=2]", rendering);
    } 
}
