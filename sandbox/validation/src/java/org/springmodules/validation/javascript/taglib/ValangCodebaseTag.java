/*
 * Copyright 2002-2005 the original author or authors.
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
package org.springmodules.validation.javascript.taglib;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import org.springframework.util.Assert;
import org.springframework.web.servlet.tags.RequestContextAwareTag;
import org.springmodules.validation.javascript.ValangJavaScriptTranslator;

/**
 * Generates the JavaScript codebase that is necessary for the use of the
 * JavaScript validation produced by {@link ValangValidateTag}.
 * 
 * <p>The generated codebase is an exact copy of the code from the file 
 * "valang_codebase.js" located in 
 * org.springmodules.validation.javascript. You can therefor avoid having to 
 * use this tag by simply placing this file on your web server and linking 
 * to it using a the following HTML:
 * <pre> 
 * &lt;script type="text/javascript" src="/somepath/valang_codebase.js"&gt;&lt;/script&gt;
 * </pre> 
 * 
 * <p>When using this tag or the HTML above you must make sure that the 
 * codebase is included before any {@link ValangValidateTag}s in you JSP file.
 * 
 * @author Oliver Hutchison
 */
public class ValangCodebaseTag extends RequestContextAwareTag {
    
    private boolean includeScriptTags; 

    /**
     * Sets whether or not the generated code should be wrapped in HTML
     * &lt;script&gt; tags. This is useful if you wont to include the
     * codebase directly in a HTML page.
     */
    public void setIncludeScriptTags(String includeScriptTags) {
        this.includeScriptTags = "true".equalsIgnoreCase(includeScriptTags);
    }

    protected int doStartTagInternal() throws ServletException, JspException {
        return SKIP_BODY;
    }

    public int doEndTag() throws JspException {
        try {
            JspWriter out = pageContext.getOut();
            if (includeScriptTags) {
                out.write("<script type=\"text/javascript\">\n");
            }
            copy(ValangJavaScriptTranslator.getCodebaseReader(), out);
            if (includeScriptTags) {
                out.write("\n</script>");
            }
            return EVAL_PAGE;
        }
        catch (IOException e) {
            throw new JspException("Could not write validation codebase", e);
        }
    }

    /**
     * Copies the chars from in to out and then closes in but 
     * leaves out open. 
     */
    private void copy(Reader in, Writer out) throws IOException {
        Assert.notNull(in, "No Reader specified");
        Assert.notNull(out, "No Writer specified");
        try {
            char[] buffer = new char[1024];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            out.flush();
        }
        finally {
            try {
                in.close();
            }
            catch (IOException ex) {
                logger.warn("Could not close Reader", ex);
            }
        }
    }
    
    public void doFinally() {
        super.doFinally();
        includeScriptTags = false;
    }
}