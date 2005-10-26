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
 * A Stemplet that evaluates an expression and generates the text that represents the evaluation result.
 *
 * @author Uri Boness
 */
public class ExpressionStemplet implements Stemplet {

    private String expression;
    private ExpressionResolver expressionResolver;
    private ExpressionWrapping expressionWrapping;

    /**
     * Constructs a new ExpressionStemplet with a given expression, expression resolver and expression wrapping.
     *
     * @param expression The expression to be evaluated by this stemplet.
     * @param expressionResolver The expression resolver to use when evaluating the expression.
     * @param expressionWrapping The expression wrapping.
     */
    public ExpressionStemplet(
        String expression,
        ExpressionResolver expressionResolver,
        ExpressionWrapping expressionWrapping) {

        this.expression = expression;
        this.expressionResolver = expressionResolver;
        this.expressionWrapping = expressionWrapping;
    }

    /**
     * @see Stemplet#generate(java.io.Writer, java.util.Map, java.util.Map)
     */
    public void generate(Writer writer, Map model, Map generationContext) throws IOException {
        writer.write(expressionResolver.resolve(expression, model));
    }

    /**
     * @see Stemplet#dump(java.io.Writer)
     */
    public void dump(Writer writer) throws IOException {
        writer.write(expressionWrapping.wrap(expression));
    }

    /**
     * @see Stemplet#getExpressions()
     */
    public String[] getExpressions() {
        return new String[] { expression };
    }

    /**
     * Returns the expression resolver that will be used by this stemplet to evaluate the expression.
     *
     * @return The expression resolver that will be used by this stemplet to evaluate the expression.
     */
    public ExpressionResolver getExpressionResolver() {
        return expressionResolver;
    }

}
