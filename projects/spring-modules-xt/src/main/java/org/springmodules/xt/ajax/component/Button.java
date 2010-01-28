/*
 * Copyright 2006 - 2008 the original author or authors.
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
 * Component implementing a button, whose type depends on its {@link ButtonType}.
 *
 * @author Sergio Bossa
 */
public class Button extends SimpleHTMLComponent {
    
    private static final long serialVersionUID = 26L;
    
    /**
     * Construct the component.
     *
     * @param name The button name.
     * @param value The button value.
     * @param type The button type.
     * @param content The button content as a {@link Component} (usually a {@link SimpleText}).
     */
    public Button(String name, String value, Button.ButtonType type, Component content) {
        this.addAttribute("name", name);
        this.addAttribute("value", value);
        this.addAttribute("type", type.toString());
        this.internalAddContent(content);
    }
    
    protected String getTagName() {
        return "button";
    };
    
    /**
     * Button type.
     */
    public enum ButtonType { BUTTON, RESET, SUBMIT }
}
