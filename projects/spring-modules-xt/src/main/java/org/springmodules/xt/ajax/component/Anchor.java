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

/**
 * Component representing an HTML anchor, that is, an HTML link.
 *
 * @author Sergio Bossa
 * @author Peter Bona
 */
public class Anchor extends SimpleHTMLComponent {
    
    private static final long serialVersionUID = 26L;
    
    /**
     * Construct the component.
     * @param link The anchor link.
     */
    public Anchor(String link) {
        this.addAttribute("href", link);
    }
    
    /**
     * Construct the component.
     * @param link The anchor link.
     * @param content The anchor content.
     */
    public Anchor(String link, Component content) {
        super(content);
        this.addAttribute("href", link);
    }
    
    protected String getTagName() {
        return "a";
    }
}
