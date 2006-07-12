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

package org.springmodules.xt.ajax.taconite;

import java.util.List;
import org.springmodules.xt.ajax.component.Component;

/**
 * Taconite based action for replacing the content of a given html element.
 * 
 * @author Sergio Bossa
 */
public class TaconiteReplaceContentAction extends AbstractTaconiteRenderingAction {
    
    /**
     * Construct the action.
     * @param elementId The id of the html element whose content must be replaced.
     * @param components A list of components (html elements) that will replace the content of the given element.
     */
    public TaconiteReplaceContentAction(String elementId, List<Component> components) {
        super(elementId, components);
    }
    
    /**
     * Construct the action.
     * @param elementId The id of the html element whose content must be replaced.
     * @param components The component (html element) that will replace the content of the given element.
     */
    public TaconiteReplaceContentAction(String elementId, Component component) {
        super(elementId, component);
    }
    
    protected String getOpeningTag() {
        return "<taconite-replace-children contextNodeID=\"$1\" parseInBrowser=\"true\">";
    }

    protected  String getClosingTag() {
        return "</taconite-replace-children>";
    }
}
