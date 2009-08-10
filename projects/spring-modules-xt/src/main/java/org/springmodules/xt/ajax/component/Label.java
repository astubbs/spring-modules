/*
 * Copyright 2006 - 2007 the original author or authors.
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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Component implementing an HTML label element.
 *
 * @author Sergio Bossa
 */
public class Label extends BaseHTMLComponent {
    
    private static final long serialVersionUID = 26L;
    
    private Component content;
    
    /**
     * Construct a label referencing a given element and having a given content.
     *
     * @param forAttribute The value of the <i>for</i> attribute, that is, the id of the element referenced by the label.
     * @param content The label content.
     */
    public Label(String forAttribute, Component content) {
        this.addAttribute("for", forAttribute);
        this.content = content;
    }
    
    protected String renderBody() {
        return this.content.render();
    }

    protected String getTagName() {
        return "label";
    }
}
