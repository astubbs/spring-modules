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

import java.util.Locale;
import org.springmodules.xt.ajax.AjaxAction;
import org.springmodules.xt.ajax.action.prototype.scriptaculous.Effect;
import org.springmodules.xt.ajax.component.Component;
import org.springframework.context.MessageSource;
import org.springframework.validation.ObjectError;
import org.springmodules.xt.ajax.component.TaggedText;
import org.springmodules.xt.ajax.validation.ErrorRenderingCallback;

/**
 * {@link org.springmodules.xt.ajax.validation.ErrorRenderingCallback} default implementation which renders the error as a
 * text surrounded by a DIV tag, and highlights it.
 *
 * @author Sergio Bossa
 */
public class DefaultErrorRenderingCallback implements  ErrorRenderingCallback {
    
    /**
     * Get the default rendering component.<br> 
     * This renders the error as plain text.
     */
    public Component getRenderingComponent(ObjectError error, MessageSource messageSource, Locale locale) {
        return new TaggedText(messageSource.getMessage(error.getCode(), error.getArguments(), error.getDefaultMessage(), locale),
                TaggedText.Tag.DIV);
    }
    
    /**
     * Get the default rendering action.<br> 
     * This renders the error highlighting it in red.
     */
    public AjaxAction getRenderingAction(ObjectError error) {
        Effect highlight = new Effect("Highlight", error.getCode());
        highlight.addOption("startcolor", "#FF0A0A");
        return highlight;
    }
}
