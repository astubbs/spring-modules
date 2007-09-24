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

import org.springmodules.xt.ajax.AjaxAction;

/**
 * Ajax action for executing Javascript code in the context of an Ajax request.
 *
 * @author Sergio Bossa
 */
public abstract class AbstractExecuteJavascriptAction implements AjaxAction {
    
    private static final String OPEN = "<execute-javascript>";
    private static final String CLOSE = "</execute-javascript>";
    
    public String render() {
        StringBuilder response = new StringBuilder();
        response.append(OPEN)
        .append("<content>")
        .append("<script type=\"text/javascript\">")
        .append(this.getJavascript())
        .append("</script>")
        .append("</content>")
        .append(CLOSE);
        return response.toString();
    }
    
    protected abstract String getJavascript();
}
