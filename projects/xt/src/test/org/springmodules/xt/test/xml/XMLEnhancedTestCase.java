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
package org.springmodules.xt.test.xml;

import org.custommonkey.xmlunit.XMLTestCase;

/**
 * Extensions to {@link org.custommonkey.xmlunit.XMLTestCase}.
 *
 * @author Sergio Bossa
 */
public class XMLEnhancedTestCase extends XMLTestCase {
    
    public XMLEnhancedTestCase() {
        super();
    }
    
    public XMLEnhancedTestCase(String name) {
        super(name);
    }
    
    public String encloseIntoRoot(String xml) {
        StringBuilder builder = new StringBuilder("<root>");
        builder.append(xml)
                   .append("</root>");
        return builder.toString();
    }
}
