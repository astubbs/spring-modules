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

package org.springmodules.xt.ajax.action.behaviour;

import org.springmodules.xt.ajax.action.AbstractExecuteJavascriptAction;

/**
 * Ajax action for applying Behaviour rules after DOM updates.<br>
 * This action should be the last one added to your Ajax response, in order to apply rules
 * to all updated DOM elements.<br><br>
 * This action uses the Behaviour Javascript library (see http://www.bennolan.com/behaviour/), 
 * so you need to include it in your web pages.
 *
 * @author Sergio Bossa
 */
public class ApplyBehaviour extends AbstractExecuteJavascriptAction {
    
    private static final long serialVersionUID = 26L;
    
    /**
     * Apply Behaviour rules.
     */
    public ApplyBehaviour() {
    }
    
    protected String getJavascript() {
        return "Behaviour.apply()";
    }
}
