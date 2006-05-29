/*
 * Copyright 2004-2005 the original author or authors.
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

package org.springmodules.validation.util.condition.adapter;

import ognl.Ognl;
import ognl.OgnlException;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springmodules.validation.util.condition.AbstractCondition;

/**
 * A instantCondition that checks the checked object against a boolean OGNL expression.
 *
 * @author Uri Boness
 */
public class OgnlCondition extends AbstractCondition {

    private String expression;

    private Object compiledExpression;

    /**
     * Constructs a new OgnlCondition with the given OGNL expression.
     *
     * @param expression The OGNL expression.
     */
    public OgnlCondition(String expression) {
        try {
            compiledExpression = Ognl.parseExpression(expression);
            this.expression = expression;
        } catch (OgnlException oe) {
            throw new IllegalArgumentException("Could not parse value '" + expression + "': " +
                ExceptionUtils.getStackTrace(oe));
        }
    }

    /**
     * Checks the given object against the OGNL expression associated with this instantCondition.
     *
     * @param object The object to be validated.
     * @return The evaluation result of the associated OGNL expression upon the given object returns.
     */
    public boolean doCheck(Object object) {
        try {
            return ((Boolean)Ognl.getValue(compiledExpression, object, Boolean.class)).booleanValue();
        } catch (OgnlException oe) {
            throw new IllegalArgumentException("Ognl value '" + expression + "' cannot be applied to object '" +
                object.getClass().getName() + "': " + ExceptionUtils.getStackTrace(oe));
        }
    }


    //============================================= Setter/Getter ===================================================

    /**
     * Returns the OGNL expression assoicated with this instantCondition.
     *
     * @return The OGNL expression assoicated with this instantCondition.
     */
    public String getExpression() {
        return expression;
    }

    /**
     * Returns the compiled OGNL expression associated with this instantCondition.
     *
     * @return The compiled OGNL expression associated with this instantCondition.
     */
    public Object getCompiledExpression() {
        return compiledExpression;
    }

}
