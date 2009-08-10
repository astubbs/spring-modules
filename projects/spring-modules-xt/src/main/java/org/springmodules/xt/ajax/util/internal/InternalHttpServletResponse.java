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

package org.springmodules.xt.ajax.util.internal;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Locale;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * HttpServletResponse implementation to use for making server-side includes via RequestDispatcher,
 * and having the response written into a StringWriter, for later reusing.<br>
 * This let you retrieve the response content using the {@link #getWriter()} method.<br>
 * DO NOT USE THIS CLASS AS IT WERE A TRUE HttpServletResponse.
 * 
 * @author Nicolas De Loof
 * @author Sergio Bossa
 */
public final class InternalHttpServletResponse implements HttpServletResponse {
    
    private StringWriter writer;
    private ServletOutputStream out;

    private String characterEncoding;
    private String contentType;
    private Locale locale;
    
    public InternalHttpServletResponse(StringWriter writer) {
        this.writer = writer;
        this.out = new ServletOutputStream() {
            public void write(int b) throws IOException {
                InternalHttpServletResponse.this.getWriter().append((char) b);
            }
        };
    }
    
    public void addCookie(Cookie cookie) {
        throw new UnsupportedOperationException();
    }
    
    public void addDateHeader(String name, long date) {
        throw new UnsupportedOperationException();
    }
    
    public void addHeader(String name, String value) {
        throw new UnsupportedOperationException();
    }
    
    public void addIntHeader(String name, int value) {
        throw new UnsupportedOperationException();
    }
    
    public boolean containsHeader(String name) {
        throw new UnsupportedOperationException();
    }
    
    public String encodeRedirectURL(String url) {
        throw new UnsupportedOperationException();
    }
    
    public String encodeRedirectUrl(String url) {
        throw new UnsupportedOperationException();
    }
    
    public String encodeURL(String url) {
        throw new UnsupportedOperationException();
    }
    public String encodeUrl(String url) {
        throw new UnsupportedOperationException();
    }
    
    public void sendError(int sc) throws IOException {
        throw new UnsupportedOperationException();
    }
    
    public void sendError(int sc, String msg) throws IOException {
        throw new UnsupportedOperationException();
    }
    
    public void sendRedirect(String location) throws IOException {
        throw new UnsupportedOperationException();
    }
    
    public void setDateHeader(String name, long date) {
        throw new UnsupportedOperationException();
    }
    
    public void setHeader(String name, String value) {
        throw new UnsupportedOperationException();
    }
    
    public void setIntHeader(String name, int value) {
        throw new UnsupportedOperationException();
    }
    
    public void setStatus(int sc) {
        throw new UnsupportedOperationException();
    }
    
    public void setStatus(int sc, String sm) {
        throw new UnsupportedOperationException();
    }
    
    public void flushBuffer() throws IOException {
        throw new UnsupportedOperationException();
    }
    
    public int getBufferSize() {
        return this.writer.getBuffer().length();
    }
    
    public String getCharacterEncoding() {
        return this.characterEncoding;
    }
    
    public String getContentType() {
        return this.contentType;
    }
    
    public Locale getLocale() {
        return this.locale;
    }
    
    public ServletOutputStream getOutputStream() throws IOException {
        return this.out;
    }
    
    public PrintWriter getWriter() throws IOException {
        return new PrintWriter(this.writer);
    }
    
    public boolean isCommitted() {
        throw new UnsupportedOperationException();
    }
    
    public void reset() {
        writer.getBuffer().setLength(0);
    }
    
    public void resetBuffer() {
        this.reset();
    }
    
    public void setBufferSize(int size) {
        throw new UnsupportedOperationException();
    }
    
    public void setCharacterEncoding(String charset) {
        this.characterEncoding = charset;
    }
    
    public void setContentLength(int len) {
        throw new UnsupportedOperationException();
    }
    
    public void setContentType(String type) {
        this.contentType = type;
    }
    
    public void setLocale(Locale loc) {
        this.locale = loc;
    }
}