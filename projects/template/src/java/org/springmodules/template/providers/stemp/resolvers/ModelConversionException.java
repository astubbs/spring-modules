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

package org.springmodules.template.providers.stemp.resolvers;

import org.springmodules.template.*;

/**
 * Thrown if a conversion of a Map model to a {@link org.w3c.dom.Document} fails.
 *
 * @see ModelToDomConverter
 * @author Uri Boness
 */
public class ModelConversionException extends TemplateGenerationException {

    public ModelConversionException(String message) {
        super(message);
    }

    public ModelConversionException(String message, Throwable cause) {
        super(message, cause);
    }

}
