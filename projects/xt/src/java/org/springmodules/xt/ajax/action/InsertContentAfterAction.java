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

package org.springmodules.xt.ajax.action;

import java.util.List;
import org.springmodules.xt.ajax.component.Component;

/**
 * Taconite based action for inserting content after a given html element.
 *
 * @author Sergio Bossa
 * @author Jean van Wyk
 */
public class InsertContentAfterAction extends AbstractRenderingAction {
    
    private static final long serialVersionUID = 26L;
    
    private static final String OPEN = "<taconite-insert-after contextNodeID=\"$1\" multipleMatch=\"$2\" parseInBrowser=\"true\">";
    private static final String CLOSE = "</taconite-insert-after>";
    
    /**
     * Construct the action.
     * @param elementId The id of the html element which the content will be inserted after.
     * @param components The content to insert : a list of components (html elements).
     */
    public InsertContentAfterAction(String elementId, List<Component> components) {
        super(elementId, components);
    }
    
    /**
     * Construct the action.
     * @param elementId The id of the html element which the content will be inserted after.
     * @param component The content to insert : a component (html element).
     */
    public InsertContentAfterAction(String elementId, Component component) {
        super(elementId, component);
    }
    
    protected String getOpeningTag() {
        return OPEN;
    }
    
    protected String getClosingTag() {
        return CLOSE;
    }
}
