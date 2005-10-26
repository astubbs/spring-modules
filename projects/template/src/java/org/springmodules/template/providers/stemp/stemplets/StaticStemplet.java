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
package org.springmodules.template.providers.stemp.stemplets;

import java.io.*;
import java.util.*;

import org.springmodules.template.providers.stemp.*;

/**
 * A stemplet that "generates" a static text.
 *
 * @author Uri Boness
 */
public class StaticStemplet implements Stemplet {

    private String text;

    /**
     * Creates a new StaticStemplet with the given static text.
     *
     * @param text The static text.
     */
    public StaticStemplet(String text) {
        this.text = text;
    }

    /**
     * Writes the static text to the given writer. <code>model</code> and <code>generationContext</code> are
     * meaningless in this context.
     *
     * @see Stemplet#generate(java.io.Writer, java.util.Map, java.util.Map)
     */
    public void generate(Writer writer, Map model, Map generationContext) throws IOException {
        writer.write(text);
    }

    /**
     * Writes the static text to the given writer.
     *
     * @see Stemplet#dump(java.io.Writer)
     */
    public void dump(Writer writer) throws IOException {
        writer.write(text);
    }

    /**
     * Returns no expressions (empty string).
     *
     * @see org.springmodules.template.providers.stemp.Stemplet#getExpressions()
     */
    public String[] getExpressions() {
        return new String[0];
    }

    /**
     * Returns the static text of this static stemplet.
     *
     * @return The static text of this static stemplet.
     */
    public String getStaticText() {
        return text;
    }
}
