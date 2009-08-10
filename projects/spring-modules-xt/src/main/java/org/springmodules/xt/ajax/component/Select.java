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

import java.util.List;

/**
 * Component implementing a select HTML element, containing {@link Option} components.
 *
 * @author Sergio Bossa
 * @author Peter Bona
 */
public class Select extends SimpleHTMLComponent {
    
    private static final long serialVersionUID = 26L;
    
    /**
     * Construct an empty select list with a name.
     *
     * @param name The name.
     */
    public Select(String name) {
        this.addAttribute("name", name);
    }
    
    /**
     * Construct a select element with a name and some options.
     *
     * @param name The name.
     * @param optionList The list of {@link Option} elements.
     */
    public Select(String name, List<Option> optionList) {
        this.addAttribute("name", name);
        for (Option option : optionList) {
            this.internalAddContent(option);
        }
    }
    
    /**
     * Add an {@link Option}.
     *
     * @param option The option to add.
     */
    public void addOption(Option option) {
        this.internalAddContent(option);
    }
    
    protected String getTagName() {
        return "select";
    }
}
