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

/**
 * Abstraction of an expression traversal rule. When evaluating an expression, the original expression is first
 * being parse and split into multiple sub-expressions. The evaluator traverse over the list of sub-expressions and applies
 * a traversal rule on each expression. There can be multiple traveral rules. Given the sub-expression and the evaluation
 * context, a rule should be able to decide whether it can be applied on the sub-expression or not.
 *
 * @author Uri Boness
 */
public interface TraversalRule {

    /**
     * Applies this rule on the model using the given (sub-)expression.
     *
     * @param expression The (sub-)expression to be used.
     * @param context The evaluation context at the moment of this method call.
     * @return The result of the evaluation.
     */
    public Object apply(String expression, EvaluationContext context);

    /**
     * Decides whether this rule can be applied on the given (sub-)expression.
     * @param expression The given sub-expression
     * @param context The evaluation context at the moment of this method call.
     * @return True if this rule can be applied on the expression, false otherwise.
     */
    public boolean isApplicable(String expression, EvaluationContext context);

}
