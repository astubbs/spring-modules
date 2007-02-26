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
 * Component implementing text surrounded by tags represented
 * by the {@link Tag} enumeration.
 *
 * @author Sergio Bossa
 * @author Peter Bona
 */
public class TaggedText extends SimpleHTMLComponent {
    
    private static final long serialVersionUID = 26L;
    
    private TaggedText.Tag tag = TaggedText.Tag.DIV;
    
    /**
     * Construct the component, using a DIV tag as a default.
     *
     * @param text The text.
     */
    public TaggedText(String text) {
        this.tag = TaggedText.Tag.DIV;
        this.internalAddTextContent(text);
    }
    
    /**
     * Construct the component.
     *
     * @param text The text.
     * @param tag The tag to use for enclosing the given text.
     */
    public TaggedText(String text, TaggedText.Tag tag) {
        this.tag = tag;
        this.internalAddTextContent(text);
    }
    
    protected String getTagName() {
        return this.tag.getTagName();
    }
    
    private void internalAddTextContent(String content) {
        this.internalAddContent(new SimpleText(content));
    }
    
    /**
     * The wrapping tag.
     */
    public enum Tag {
        
        DIV {
            public String getTagName() {
                return "div";
            }
        },
        
        SPAN {
            public String getTagName() {
                return "span";
            }
        };
        
        public abstract String getTagName();
    }
}
