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

package org.springmodules.xt.ajax;

/**
 * Interface representing a kind of ajax action that directly renders content into a web page.<br>
 * Rendering actions use {@link ElementMatcher}s for
 * identifying the web page elements to modify.
 *
 * @author Sergio Bossa
 */
public interface AjaxRenderingAction extends AjaxAction {
   
    /**
     * Get the {@link ElementMatcher} to use for
     * identifying the web page elements to modify.
     *
     * @return An {@link ElementMatcher}.
     */
    public ElementMatcher getElementMatcher();
}
