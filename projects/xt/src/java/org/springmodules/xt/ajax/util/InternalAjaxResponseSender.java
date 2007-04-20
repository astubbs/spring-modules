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

package org.springmodules.xt.ajax.util;

import java.io.IOException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springmodules.xt.ajax.AjaxResponse;

/**
 * Utility class for sending Ajax responses.
 *
 * @author Sergio Bossa
 */
public class InternalAjaxResponseSender {
    
    private static final Logger logger = Logger.getLogger(InternalAjaxResponseSender.class);
    
    public static void sendResponse(HttpServletResponse httpResponse, AjaxResponse ajaxResponse)
    throws IOException {
        String response = ajaxResponse.getResponse();
        logger.debug(new StringBuilder("Sending ajax response: ").append(response));
        httpResponse.setContentType("text/xml");
        httpResponse.setCharacterEncoding(ajaxResponse.getEncoding());
        httpResponse.setHeader("Cache-Control", "no-cache");
        ServletOutputStream out = httpResponse.getOutputStream();
        out.print(response);
        out.close();
    }
}
