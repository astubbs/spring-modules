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

import java.io.StringWriter;
import javax.servlet.http.HttpServletRequest;
import org.springmodules.xt.ajax.util.InternalHttpServletResponse;
import org.springmodules.xt.ajax.component.support.RenderingException;


/**
 * Component that uses a JSP to build dynamic XHTML content.
 *
 * @author Nicolas De Loof
 * @author Sergio Bossa
 */
public class JspComponent implements Component {
    
    private HttpServletRequest request;
    private String path;
    
    /**
     * Construct the component.
     *
     * @param request The HttpServletRequest to use for dynamic content.
     * @param path The JSP path: it is relative to the current path.
     */
    public JspComponent(HttpServletRequest request, String path) {
        this.request = request;
        this.path = path;
    }
    
    public String render() {
        StringWriter writer = new StringWriter();
        try {
            request.getRequestDispatcher(path).include(request, new InternalHttpServletResponse(writer));
        } 
        catch (Exception e) {
            throw new RenderingException("Error while rendering a component of type: " + this.getClass().getName(), e);
        }
        return writer.toString();
    }
}
