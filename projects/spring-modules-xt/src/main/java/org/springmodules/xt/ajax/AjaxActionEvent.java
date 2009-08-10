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

package org.springmodules.xt.ajax;

/**
 * Interface for an Ajax action event.<br>
 * It carries:
 * <ul>
 *      <li>The command object associated with the form (if any).</li>
 * </ul>
 * The command object is set if the ajax event is generated from a form handled by a {@link org.springframework.web.servlet.mvc.BaseCommandController}, and is the same
 * object created by the command controller.
 * 
 * @author Sergio Bossa
 */
public interface AjaxActionEvent extends AjaxEvent {
    
    /**
     * Get the command object associated with the form (if any).
     * @return The command object, or null if no command object has been found.
     */
    public Object getCommandObject();
    
    /**
     * Set the command object associated with the form (if any).
     * @param command The command object.
     */
    public void setCommandObject(Object command);
}
