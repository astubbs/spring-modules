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
 * Component implementing an input field, whose type depends on its {@link InputType}.
 *
 * @author Sergio Bossa
 * @author Peter Bona
 */
public class InputField extends SimpleHTMLComponent {
    
    private static final long serialVersionUID = 26L;
    
    /**
     * Construct the component.
     *
     * @param name The input field name.
     * @param value The input field value.
     * @param type The input field type.
     */
    public InputField(String name, String value, InputField.InputType type) {
        this.addAttribute("name", name);
        this.addAttribute("value", value);
        this.addAttribute("type", type.toString());
    }
    
    protected String getTagName() {
        return "input";
    };
    
    /**
     * Input field type.
     */
    public enum InputType { TEXT, CHECKBOX, RADIO, HIDDEN, PASSWORD, BUTTON, IMAGE, RESET, FILE }
}
