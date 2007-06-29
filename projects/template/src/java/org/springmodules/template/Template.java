/*
 * Copyright 2002-2007 the original author or authors.
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

import java.io.OutputStream;
import java.io.Writer;
import java.util.Map;

/**
 * An abstraction of a template that can generate output based on a template model. <code>Template</code> implemenatation
 * are expected to be thread safe.
 *
 * @author Uri Boness
 */
public interface Template {

    /**
     * Generates output based on the given model and flushes it into the given OutputStream.
     *
     * @param out The OutputStream the output will be flushed into.
     * @param model The model based on which the output will be generated.
     * @throws TemplateGenerationException Thrown if an error occured during the output generation or flushing.
     */
    void generate(OutputStream out, Map model) throws TemplateGenerationException;

    /**
     * Generates output based on the given model and flushes it into the given Writer.
     *
     * @param writer The Writer the output will be flushed into.
     * @param model The model based on which the output will be generated.
     * @throws TemplateGenerationException Thrown if an error occured during the output generation or flushing.
     */
    void generate(Writer writer, Map model) throws TemplateGenerationException;

    /**
     * Generates output based on the given model and returns it.
     *
     * @param model The model based on which the output will be generated.
     * @return The output of the template generation.
     * @throws TemplateGenerationException Thrown if an error occured during the output generation.
     */
    String generate(Map model) throws TemplateGenerationException;


}