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

package org.springmodules.xt.ajax.validation.support;

import org.springmodules.xt.ajax.AjaxAction;
import org.springmodules.xt.ajax.AjaxSubmitEvent;
import org.springmodules.xt.ajax.validation.SuccessRenderingCallback;

/**
 * Default {@link org.springmodules.xt.ajax.validation.SuccessRenderingCallback} that simply returns no actions.
 *
 * @author Sergio Bossa
 */
public class DefaultSuccessRenderingCallback implements SuccessRenderingCallback {
    
    public AjaxAction[] getSuccessActions(AjaxSubmitEvent event) {
        return null;
    }
}
