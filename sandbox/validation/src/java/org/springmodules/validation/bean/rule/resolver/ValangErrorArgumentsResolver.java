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

package org.springmodules.validation.bean.rule.resolver;

import java.io.StringReader;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ClassUtils;
import org.springmodules.validation.bean.rule.ErrorArgumentsResolver;
import org.springmodules.validation.valang.functions.Function;
import org.springmodules.validation.valang.functions.TargetBeanFunction;
import org.springmodules.validation.valang.parser.ParseException;
import org.springmodules.validation.valang.parser.ValangParser;
import org.springmodules.validation.valang.parser.ValangVisitor;

/**
 * Resolves error arguments based on valang expressions.
 *
 * @author Uri Boness
 */
public class ValangErrorArgumentsResolver implements ErrorArgumentsResolver {

    private final static Log logger = LogFactory.getLog(ValangErrorArgumentsResolver.class);

    private Function[] functions;

    /**
     * Creates a new ValangErrorArgumentsResolver with no argument expressions, that is, there will be no arguments.
     */
    public ValangErrorArgumentsResolver() {
        this(new String[0]);
    }

    /**
     * Creates a new ValangErrorArgumentsResolver with the given argument expressions. This constructor uses the default
     * {@link ValangParser).
     *
     * @param expressions The argument valang expressions.
     */
    public ValangErrorArgumentsResolver(String[] expressions) {
        this (expressions, null);
    }

    /**
     * Creates a new ValangErrorArgumentsResolver with given argument expressions and custom functions that will be used
     * by the valang parser.
     *
     * @param expressions The arguments valang expressions.
     * @param functionsByName A map of custom valang functiosns where the key is the function name and the value is
     *        the name of the function class.
     */
    public ValangErrorArgumentsResolver(String[] expressions, Map functionsByName) {
        if (expressions == null) {
            expressions = new String[0];
        }
        functions = new Function[expressions.length];
        for (int i=0; i<expressions.length; i++) {
            functions[i] = parseFunction(expressions[i], functionsByName);
        }
    }

    /**
     * Returns the error arguments that are resolved by the configured valang expressions.
     *
     * @see ErrorArgumentsResolver#resolveArguments(Object)
     */
    public Object[] resolveArguments(Object obj) {
        Object[] args = new Object[functions.length];
        for (int i=0; i<args.length; i++) {
            args[i] = functions[i].getResult(obj);
        }
        return args;
    }


    //=============================================== Helper Methods ===================================================

    /**
     * Parses a valang {@link Function} from the given argument expression and custom functions.
     *
     * @param expression The given argument expression.
     * @param functionsByName The custom valang functions.
     * @return The parsed function.
     */
    protected Function parseFunction(String expression, Map functionsByName) {

        try {

            ValangParser parser = new ValangParser(new StringReader(expression));
            if (functionsByName == null) {
                return parser.function(new TargetBeanFunction());
            }

            final Map constructorMap = new HashMap();
            for (Iterator iter = functionsByName.keySet().iterator(); iter.hasNext();) {
                Object stringNameObject = iter.next();
                if (!(stringNameObject instanceof String)) {
                    throw new IllegalArgumentException("Key for custom functions map must be a string value!");
                }
                String functionName = (String)stringNameObject;
                Object functionClassNameObject = functionsByName.get(functionName);
                if (!(functionClassNameObject instanceof String)) {
                    throw new IllegalArgumentException("Value for custom function map must be a string!");
                }
                String functionClassName = (String)functionClassNameObject;
                Class functionClass = loadClass(functionClassName);
                if (!(Function.class.isAssignableFrom(functionClass))) {
                    throw new IllegalArgumentException(
                            "Custom function classes must implement org.springmodules.validation.valang.functions.Function!");
                }
                Constructor constructor;
                try {
                    constructor = functionClass.getConstructor(new Class[] {Function[].class, int.class, int.class});
                }
                catch (NoSuchMethodException e) {
                    throw new IllegalArgumentException("Class [" + functionClass.getName()
                        + "] has no constructor with one org.springmodules.validation.valang.functions.Function parameter!");
                }
                constructorMap.put(functionName, constructor);
            }
            parser.getVisitor().setVisitor(new ValangVisitor() {
                public Function getFunction(String name, Function[] functions, int line, int column) {
                    if (constructorMap.containsKey(name)) {
                        Constructor functionConstructor = (Constructor)constructorMap.get(name);
                        return (Function) BeanUtils.instantiateClass(functionConstructor, new Object[] {functions,
                                new Integer(line), new Integer(column)});
                    }
                    return null;
                }
            });

            return parser.function(new TargetBeanFunction());

        } catch (ParseException pe) {
            logger.error("Could not parse valang expression '" + expression + "' to a function", pe);
            throw new IllegalArgumentException("Could not parse valang expression '" + expression + "' to a function");
        }
    }

    protected Class loadClass(String className) {
        try {
            return ClassUtils.forName(className);
        } catch (ClassNotFoundException cnfe) {
            logger.error("Could not load class '" + className + "'", cnfe);
            throw new IllegalArgumentException("Could not load class '" + className + "'");
        }
    }

}
