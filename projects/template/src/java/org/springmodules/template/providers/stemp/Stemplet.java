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

package org.springmodules.template.providers.stemp;

import java.io.*;
import java.util.*;

/**
 * When the StempParser parses a stemp template, it creates a list of stemplets to represent the template. A stemplet
 * can be seen as a runtime representation of some part of the template. When the template is generated, all stemplets
 * in that list are being called to generate their parts. Togther, all stemplets generate the template output.
 *
 * @author Uri Boness
 */
public interface Stemplet {

    /**
     * Generates the template part this stemplet is associated with and puts the output in the given writer.
     *
     * @param writer The writer where the output will be put.
     * @param model The model of the template.
     * @param generationContext The generation context (not used currently, but might be used in the future to support
     *        template scope variables)
     * @throws IOException
     */
    public void generate(Writer writer, Map model, Map generationContext) throws IOException;

    /**
     * Dumps the raw part of this stemplet. The raw part is the non-generated part of the template
     * this stemplet is associated with
     *
     * @param writer The writer where the raw part will be dumped to.
     * @throws IOException
     */
    public void dump(Writer writer) throws IOException;

    /**
     * Returns a list of all the expressions in the associated template part.
     *
     * @return A list of all the expressions in the associated template part.
     */
    public String[] getExpressions();

}
