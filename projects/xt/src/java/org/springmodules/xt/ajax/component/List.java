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
 * Component implementing an HTML list element.<br>
 * The list can be ordered or unordered, depending on the
 * {@link ListType}.
 *
 * @author Sergio Bossa
 * @author Peter Bona
 */
public class List extends SimpleHTMLComponent {
    
    private static final long serialVersionUID = 26L;
    
    private List.ListType type;
    
    /**
     * Construct the component.
     * @param type The list type.
     */
    public List(List.ListType type) {
        this.type = type;
    }
    
    /**
     * Construct the component.
     * @param type The list type.
     * @param items The list of contained items.
     */
    public List(List.ListType type, java.util.List<ListItem> items) {
        this.type = type;
        for (ListItem item : items) {
            this.internalAddContent(item);
        }
    }
    
    /**
     * Add a {@link ListItem}.
     * @param item The item to add.
     */
    public void addItem(ListItem item) {
        this.internalAddContent(item);
    }
    
    protected String getTagName() {
        return this.type.getTagName();
    }
    
    /**
     * List type.
     */
    public enum ListType {
        ORDERED {
            public String getTagName() {
                return "ol";
            }
        },
        UNORDERED {
            public String getTagName() {
                return "ul";
            }
        };
        public abstract String getTagName();
    }
}
