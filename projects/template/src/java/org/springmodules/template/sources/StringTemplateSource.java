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

package org.springmodules.template.sources;

import java.io.*;

import org.springmodules.template.*;

/**
 * Wraps a string as a template source.
 *
 * @author Uri Boness
 */
public class StringTemplateSource extends AbstractTemplateSource {

    private String source;

    /**
     * Constructs a new template source out of the given string.
     *
     * @param source The given source as a string.
     */
    public StringTemplateSource(String source) {
        this.source = source;
    }

    /**
     * Constructs a new template source out of the given string source and associates it with the given name.
     *
     * @param name The name of this template source.
     * @param source The source of this template source.
     */
    public StringTemplateSource(String name, String source) {
        super(name);
        this.source = source;
    }

    /**
     * Returns a reader for this source.
     *
     * @return A reader for this source.
     */
    public Reader getReader() {
        return new StringReader(source);
    }

    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(source.getBytes());
    }

    /**
     * Returns the source as a string. This is an optional functionality.
     *
     * @return The source as a string.
     * @throws UnsupportedOperationException Must be thrown if this template source doesn't support string representations.
     */
    public String getAsString() {
        return source;
    }

}
