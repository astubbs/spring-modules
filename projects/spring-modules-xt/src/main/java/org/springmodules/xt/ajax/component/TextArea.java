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
 * Component implementing an HTML text area.
 *
 * @author Peter Bona
 * @author Sergio Bossa
 */
public class TextArea extends SimpleHTMLComponent {
    
    private static final long serialVersionUID = 26L;
    
    /**
     * Constructor taking the number of rows and columns.
     *
     * @param rows The number of rows.
     * @param cols The number of columns.
     */
    public TextArea(int rows, int cols) {
        this.addAttribute("rows", Integer.toString(rows));
        this.addAttribute("cols", Integer.toString(cols));
    }
    
    /**
     * Constructor taking the number of rows and columns and the text content of the area.
     *
     * @param rows The number of rows.
     * @param cols The number of columns.
     * @param text The text area content.
     */
    public TextArea(int rows, int cols, String text) {
        this(rows,cols);
        this.internalAddContent(new SimpleText(text));
    }
    
    protected String getTagName() {
        return "textarea";
    }
}
