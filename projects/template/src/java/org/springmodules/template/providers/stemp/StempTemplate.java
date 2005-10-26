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

import org.springmodules.template.*;

/**
 * An implemenation of the {@link Template} inteface that uses {@link Stemp} as the underlying template "engine".
 *
 * @author Uri Boness
 */
public class StempTemplate extends AbstractTemplate {

    private Stemp stemp;

    public StempTemplate(Stemp stemp) {
        this.stemp = stemp;
    }

    public void generate(Writer writer, Map model) throws TemplateGenerationException {
        try {
            stemp.generate(model, writer);
        } catch (StempException se) {
            throw new TemplateGenerationException("Could not generated template using stemp", se);
        }
    }

}
