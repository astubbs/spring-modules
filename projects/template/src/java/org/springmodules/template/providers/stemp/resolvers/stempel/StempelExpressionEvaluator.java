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

import org.springmodules.template.providers.stemp.resolvers.stempel.rules.*;

/**
 * An evaluator for steml expressions.
 *
 * @see Stempel
 * @author Uri Boness
 */
public class StempelExpressionEvaluator {

    // the evaluation rules supported by stempel.
    private final List rules;

    // a lock for the rules list.
    private Object ruleLock = new Object();

    /**
     * Constructs a new StempelExpressionEvaluator with a default set of rules.
     */
    public StempelExpressionEvaluator() {
        rules = new ArrayList();
        rules.add(new MapValueByKeyBracketsStyleTraversalRule());
        rules.add(new MapValueByKeyPropertyStyleTraversalRule());
        rules.add(new IndexedPropertyBracketsStyleTraversalRule());
        rules.add(new BeanPropertyTraversalRule());
    }

    /**
     * Evaluates the given stempl expression against the given model.
     *
     * @param expression The expression to evaluate.
     * @param model The model on which the expression will be evaluated.
     * @return The result of the expression evaluation.
     */
    public Object evaluate(String expression, Map model) throws ExpressionEvaluationException {

        String[] expressions = parse(expression);

        EvaluationContext context = new EvaluationContext(this, model);

        TraversalRule[] rules = getRules();

        for (int i=0; i<expressions.length; i++) {
            TraversalRule rule = getApplicableRule(rules, expressions[i], context);
            if (rule == null) {
                throw new UnsupportedExpressionException("Expression: \"" + expressions[i] + "\" cannot be applied " +
                    "on object of type" + context.getCurrentObject().getClass().getName());
            }
            Object result = rule.apply(expressions[i], context);
            context.push(result);
        }

        return context.getCurrentObject();
    }

    /**
     * Adds the given traversal rule to this evaluator. The traversal rule is added last in the
     * rule list which means that this rule will be requested to be applied only if all other rule rejected the
     * request.
     *
     * @param rule The rule the ba added.
     */
    public void addTraversalRule(TraversalRule rule) {
        synchronized(ruleLock) {
            rules.add(rule);
        }
    }

    /**
     * Adds the given traversal rule to this evaluator. The traversal rule is added to the rules list at the
     * specified index.
     *
     * @param rule The rule the ba added.
     * @param index The index of the rule in the rules list.
     */
    public void addTraversalRule(TraversalRule rule, int index) {
        synchronized(ruleLock) {
            rules.add(index, rule);
        }
    }

    /**
     * Returns the rules list this evaluator uses.
     * @return The rules list this evaluator uses.
     */
    public TraversalRule[] getRules() {
        synchronized(ruleLock) {
            return (TraversalRule[])rules.toArray(new TraversalRule[rules.size()]);
        }
    }

    /**
     * looks up a rule that can be applied on the given expression under the given evaluation context.
     *
     * @param rules A list of rules in which the applicable rule will be searched for.
     * @param expression The expression to evaluate.
     * @param context the current evaluation context
     * @return The applicalbe rule if found, <code>null</code> if not found.
     */
    protected TraversalRule getApplicableRule(TraversalRule[] rules, String expression, EvaluationContext context) {
        for (int i=0; i<rules.length; i++) {
            TraversalRule rule = rules[i];
            if (rule.isApplicable(expression, context)) {
                return rule;
            }
        }
        return null;
    }

    // note about parsing:
    //
    // the right way to parse the expressions is to do the following:
    // - first try to find an applicable rule for the expression
    // - if one found, keep the expression as is.
    // - if non found, split the epression once (to get two expressions)
    // - try to find an applicable rule for the first expression.
    // - if one found, save the first expression, and repeat the process on the second expression
    // - if non was found, throw a "bad expression" error.
    //
    // this algorithm is the correct one to use, and is generic enough to support any additional rules that
    // may be added in the future. The only problem with it is that it is a bit slow. Therefore, the current
    // implementation doesn't use this algorithm. Instead the "parse()" method below, loops through the expression
    // and tokenizes it according to what the current rules support. This is in a way a hardcoded algorithm that might
    // need to be changed when new rules are added, but it's faster then the alternative.


    /**
     * Parses the given expression to sub-expressions
     *
     * @param expression The expression to parse
     * @return A list of all the sub-expressions
     */
    protected String[] parse(String expression) {
        List result = new ArrayList();
        char[] chars = expression.toCharArray();
        int startExpression = 0;
        boolean escaping = false;
        for (int i=0; i<chars.length; i++) {
            if (chars[i] == '.' && !escaping) {
                String token = new String(chars, startExpression, i-startExpression);
                if (token.length() != 0) {
                    result.add(token);
                }
                startExpression = i+1;
                continue;
            }
            if (chars[i] == '[') {
                escaping = true;
            } else if (chars[i] == ']') {
                escaping = false;
            }
        }
        String token = new String(chars, startExpression, chars.length-startExpression);
        if (token.length() != 0) {
            result.add(token);
        }
        return (String[])result.toArray(new String[result.size()]);
    }

}
