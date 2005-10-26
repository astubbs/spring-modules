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

import java.util.*;

import ognl.*;
import org.springmodules.template.providers.stemp.*;
import org.apache.commons.logging.*;

/**
 * An implementation of {@link org.springmodules.template.providers.stemp.ExpressionResolver} that can resolve <a href="http://www.ognl.org">OGNL</a> expressions.
 *
 * @author Uri Boness
 */
public class OgnlExpressionResolver implements ExpressionResolver {

    private static final Log log = LogFactory.getLog(OgnlExpressionResolver.class);

    /**
     * @see org.springmodules.template.providers.stemp.ExpressionResolver#resolve(String, java.util.Map)
     */
    public String resolve(String expression, Map model) {
        try {
            return String.valueOf(Ognl.getValue(expression, model));
        } catch (OgnlException oe) {
            log.error("Could resolve Ognl exprssion '" + expression + "'.", oe);
            return null;
        }
    }

}
