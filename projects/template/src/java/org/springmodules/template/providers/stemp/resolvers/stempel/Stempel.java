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
 * A static helper class that can be used to evaluate stempl expressions.
 * Stempel stands for <b>S</b>imple <b>Temp</b>late <b>E</b>xpression <b>L</b>anguage. This is an out-of-the-box
 * expression language support for the <i>Stemp</i> template engine. This language curerntly supports the following
 * rule:<br/>
 * <ol>
 *   <li>
 *     Bean Properties retrieval - One can retrieve the property of a bean in the model. If for example, the model
 *     holds a person bean that associated with "person" name in the map, one can retrieve the person's name using
 *     the following expression: <code>person.name</code>. Moreover, properties of properties are accessible as well. For
 *     example, to retrive the city in which the person lives in, one can use the following expression:
 *     <code>person.address.city</code>. Here the <i>address</i> property of the <i>person</i> is retrieved first, then
 *     the <i>city</i> property of that <i>address</i> is retrieved.
 *   </li><br/>
 *   <li>
 *     Indexed Properties retrieval - It is possible to access indexed properties of beans. Using the above example, let's
 *     assume that a person has a list of friends (each one is a person as well). In order to get all the friends we can
 *     use a normal property accessor expression (as described above) like <code>person.friends</code>. But if we want
 *     to retrieve the 2nd. best friend of the person (assuming the friends are sorted by the strength of their relationship
 *     with the person) we can use the following expression: <code>person.friends[1]</code>. This is very much similar
 *     to the way we access arrays in java.<br/>
 *     Note that a Indexed property can be ether an array or a java.util.List object.
 *   </li><br/>
 *   <li>
 *     It is also possible to access Map property entries. If for example a person has attributes as a Map, one can accesss
 *     the "favorite_color" attribute using the following expression: <code>person.attributes["favorite_color"]</code> or
 *     <code>person.attributes['favorite_color']</code> (it's possible to use both colons and single colons). It is also
 *     possible to access entries using the name notation as you'd access properties of beans, that is, the same expression
 *     as above can also be written as <code>person.attributes.favorite_color</code>.
 *   </li>
 * </ol>
 * <br/>
 * Since the implementation of Stempel is quite modular, it is relatively easy to define and add new rules to the language.
 * At the moment I feel that the current support should be enough for what stemp can ofer. More rules may be added in the
 * future if they are found to be useful.<br/><br/>
 *
 * This class uses a single instance of the StempelExpressionEvaluator (so it can be regarded as a singleton). If more
 * then one instance is needed the StempelExpressionEvaluator can be used directly.
 *
 * @author Uri Boness
 */
public class Stempel {

    private final static StempelExpressionEvaluator EVALUATOR = new StempelExpressionEvaluator();

    /**
     * Evaluates the given expression against the given Map model.
     *
     * @param expression The expression to evaluate.
     * @param model The model on which the expression will be evaluated.
     * @return The result of the expression evaluation.
     */
    public static Object evaluate(String expression, Map model) throws ExpressionEvaluationException {
        return EVALUATOR.evaluate(expression, model);
    }

}
