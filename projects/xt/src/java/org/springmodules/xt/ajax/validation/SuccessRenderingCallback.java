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

package org.springmodules.xt.ajax.validation;

import org.springmodules.xt.ajax.AjaxAction;
import org.springmodules.xt.ajax.AjaxSubmitEvent;

/**
 * Callback to implement for rendering actions on success <b>after</b> validation.
 *
 * @author Sergio Bossa
 */
public interface SuccessRenderingCallback {
    
    /**
     * Get the actions that will be executed on success <b>after</b> validation.
     *
     * @param event The submit event.
     * @return An array of {@link org.springmodules.xt.ajax.AjaxAction}s.
     */
    public AjaxAction[] getSuccessActions(AjaxSubmitEvent event);
}
