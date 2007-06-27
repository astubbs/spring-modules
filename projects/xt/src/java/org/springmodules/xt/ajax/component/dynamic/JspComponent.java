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

package org.springmodules.xt.ajax.component.dynamic;

import java.io.StringWriter;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import org.springmodules.xt.ajax.component.Component;
import org.springmodules.xt.ajax.component.support.RenderingException;
import org.springmodules.xt.ajax.util.internal.InternalHttpServletResponse;


/**
 * Component that uses a Jsp to build dynamic XHTML content.
 *
 * @author Nicolas De Loof
 * @author Sergio Bossa
 */
public class JspComponent implements Component {
    
    private static final long serialVersionUID = 26L;
    
    private HttpServletRequest request;
    private String path;
    
    private String characterEncoding;
    private String contentType;
    private Locale locale;
    
    /**
     * Construct the component.
     *
     * @param request The HttpServletRequest to use for dynamic content.
     * @param path The Jsp path.
     */
    public JspComponent(HttpServletRequest request, String path) {
        this.request = request;
        this.path = path;
    }
    
    public String render() {
        StringWriter writer = new StringWriter();
        InternalHttpServletResponse response = new InternalHttpServletResponse(writer);
        response.setCharacterEncoding(this.characterEncoding);
        response.setContentType(this.contentType);
        response.setLocale(this.locale);
        try {
            request.getRequestDispatcher(this.path).include(this.request, response);
        } 
        catch (Exception e) {
            throw new RenderingException("Error while rendering a component of type: " + this.getClass().getName(), e);
        }
        return writer.toString();
    }

    public void setCharacterEncoding(String characterEncoding) {
        this.characterEncoding = characterEncoding;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }
}
