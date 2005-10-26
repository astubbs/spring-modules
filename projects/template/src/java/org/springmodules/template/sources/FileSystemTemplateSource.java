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
 * A file system based template source. In general, when using this classes with spring, there is no
 * need to use this class for the org.springframework.core.io.FileSystemResource can be used instead.
 * This implementation is mainly for usages outside the spring framework context.
 *
 * @author Uri Boness
 */
public class FileSystemTemplateSource extends AbstractTemplateSource {

    private File file;

    /**
     * Constructs a new FileSystemTemplateSource with a given file.
     *
     * @param file The file that serves as the source for the template.
     */
    public FileSystemTemplateSource(File file) {
        this(file.getAbsolutePath(), file);
    }

    /**
     * Constructs a new FileSystemTemplateSource with a given file.
     *
     * @param name The name of this template source.
     * @param file The
     */
    public FileSystemTemplateSource(String name, File file) {
        super(name);
        this.file = file;
    }

    public Reader getReader() throws IOException {
        return new FileReader(file);
    }

    public InputStream getInputStream() throws IOException {
        return new FileInputStream(file);
    }

}
