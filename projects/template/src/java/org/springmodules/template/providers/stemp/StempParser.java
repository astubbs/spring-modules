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
 * A Parser to parse templates and generate a runtime representation of them using stemplet lists.
 *
 * @author Uri Boness
 */
public interface StempParser {

    /**
     * Parses a template from the given reader.
     *
     * @param reader The reader from which the template will be read.
     * @param expressionResolver The expression resolver that will be used to resolve expressions.
     * @param expressionWrapping The expression wrapping of the given template.
     * @return A list of newly created stemplets that serve as the runtime representation of the template.
     * @throws StempParseException Thrown when the parsing fails.
     */
    public List parse(Reader reader, ExpressionResolver expressionResolver, ExpressionWrapping expressionWrapping)
        throws StempParseException;

}
