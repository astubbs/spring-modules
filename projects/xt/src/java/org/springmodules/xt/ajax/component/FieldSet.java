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

import java.util.List;

/**
 * Component implementing an HTML fieldset element.
 *
 * @author Sergio Bossa
 */
public class FieldSet extends SimpleHTMLComponent {
    
    private static final long serialVersionUID = 26L;

    /**
     * Construct an empty fieldset.
     */
    public FieldSet() {
    }
    
    /**
     * Construct a fieldset with the given list of elements.
     * @param elements The elements contained in the fieldset.
     */
    public FieldSet(List<Component> elements) {
        for (Component c : elements) {
            this.internalAddContent(c);
        }
    }

    /**
     * Construct a fieldset with the given component representing the fieldset legend.
     * @param legend The component acting as a legend.
     */
    public FieldSet(Component legend) {
        this.internalAddContent(new Legend(legend));
    }

    /**
     * Construct a fieldset with the given component representing the fieldset legend, and a list of elements.
     * @param legend The component acting as a legend.
     * @param elements The elements contained in the fieldset.
     */
    public FieldSet(Component legend, List<Component> elements) {
        this.internalAddContent(new Legend(legend));
        for (Component c : elements) {
            this.internalAddContent(c);
        }
    }

    /**
     * Add a new element to the fieldset.
     * @param element The component to add.
     */
    public void addElement(Component element) {
        this.internalAddContent(element);
    }

    protected String getTagName() {
        return "fieldset";
    }

    public static class Legend extends BaseHTMLComponent {
    
        private static final long serialVersionUID = 26L;

        private Component content;

        public Legend(Component content) {
            this.content = content;
        }

        protected String renderBody() {
            return this.content.render();
        }

        protected String getTagName() {
            return "legend";
        }
    }
}
