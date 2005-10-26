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

package org.springmodules.template.providers.stemp.resolvers.stempel;

import java.util.*;

/**
 * Reresents the evaluation context for each traversal rule.
 *
 * @author Uri Boness
 */
public class EvaluationContext {

    // the evaluator used for the current evaluation.
    private StempelExpressionEvaluator evaluator;

    // the model the evaluation "works" on.
    private Map model;

    // the evaluation stack. each rule evaluates some part of the expression. this parts are put on
    // the evaluation stack through which they are accessible to the following rules.
    private Stack evaluationStack;

    /**
     * Constructs a new EvaluationContext with a given evaluator and model.
     *
     * @param evaluator The evaluator that created this evaluation context.
     * @param model The model on which the expression is evaluated.
     */
    public EvaluationContext(StempelExpressionEvaluator evaluator, Map model) {
        this.evaluator = evaluator;
        this.model = model;
        evaluationStack = new Stack();
        evaluationStack.push(model);
    }

    /**
     * Returns the evaluator of this context.
     * @return The evaluator of this context.
     */
    public StempelExpressionEvaluator getEvaluator() {
        return evaluator;
    }

    /**
     * Returns the model on which the expression is evaluated.
     * @return The model on which the expression is evaluated.
     */
    public Map getModel() {
        return model;
    }

    /**
     * Returns the top most object on the evaluation stack.
     * @return The top most object on the evaluation stack.
     */
    public Object getCurrentObject() {
        return evaluationStack.peek();
    }

    /**
     * Removes the top most object from the evaluation stack and returns it.
     * @return The tomp most object of the evaluation stack.
     */
    public Object pop() {
        return evaluationStack.pop();
    }

    /**
     * Adds the given object to the top of the evaluation stack.
     * @param object The object to add to the evaluation stack.
     */
    public void push(Object object) {
        evaluationStack.push(object);
    }
}
