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

package org.springmodules.template;

import java.io.*;

/**
 * Represents a source of a template.
 *
 * @author Uri Boness
 */
public interface TemplateSource {

    /**
     * A template source may have a name associated with it.
     *
     * @return The name of this template source, <code>null</code> if no name is associated with this source.
     */
    public String getName();

    /**
     * Returns a reader for this source. Note that it should be possible to call this method multiple times. As
     * a concequence one cannot implement this interface just by wrapping a java.io.Reader for once a a reader has
     * been read, it cannot be re-read.
     *
     * @return A reader for this source.
     */
    public Reader getReader() throws IOException;

    /**
     * Returns an input stream for this source. Note that it should be possible to call this method multiple times. As
     * a concequence one cannot implement this interface just by wrapping a java.io.InputStream for once a a reader has
     * been read, it cannot be re-read.
     *
     * @return An input stream for this source.
     * @throws IOException
     */
    public InputStream getInputStream() throws IOException;

    /**
     * Returns the source as a string. This is an optional functionality.
     *
     * @return The source as a string.
     * @throws UnsupportedOperationException Must be thrown if this template source doesn't support string representations.
     */
    public String getAsString() throws UnsupportedOperationException;


}
