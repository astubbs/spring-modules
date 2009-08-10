/*
 * Copyright 2006-2007 the original author or authors.
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

package org.springmodules.xt.ajax.util;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springmodules.xt.ajax.AjaxResponse;

/**
 * Utility class for sending Ajax responses.
 *
 * @author Sergio Bossa
 */
public class AjaxResponseSender {
    
    private static final Logger logger = Logger.getLogger(AjaxResponseSender.class);
    
    public static void sendResponse(HttpServletResponse httpResponse, AjaxResponse ajaxResponse)
    throws IOException {
        String response = ajaxResponse.render();
        logger.debug(new StringBuilder("Sending ajax response: ").append(response));
        
        StringBuilder builder = new StringBuilder();
        builder.append("text/xml; charset=").append(ajaxResponse.getEncoding());
        httpResponse.setContentType(builder.toString());
        httpResponse.setHeader("Cache-Control", "no-cache");
        
        PrintWriter writer = httpResponse.getWriter();
        writer.print(response);
        writer.close();
    }
}
