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

package org.springmodules.validation.valang.parser;

import java.util.HashMap;
import java.util.Map;

import org.springmodules.validation.util.BasicContextAware;
import org.springmodules.validation.valang.functions.FunctionDefinition;

/**
 * A simple implementation of {@link ValangBased}.
 *
 * @author Uri Boness
 */
public class SimpleValangBased extends BasicContextAware implements ValangBased {

    private Map customFunctions;
    private Map dateParsers;

    public SimpleValangBased() {
        customFunctions = new HashMap();
        dateParsers = new HashMap();
    }

    /**
     * @see ValangBased#setCustomFunctions(java.util.Map)
     */
    public void setCustomFunctions(Map functionByName) {
        customFunctions = functionByName;
    }

    /**
     * @see ValangBased#addCustomFunction(String, String)
     */
    public void addCustomFunction(String functionName, String functionClassName) {
        customFunctions.put(functionName, functionClassName);
    }

    /**
     * @see ValangBased#setDateParsers(java.util.Map)
     */
    public void setDateParsers(Map parserByRegexp) {
        dateParsers = parserByRegexp;
    }

    /**
     * Creates a new {@link ValangParser} that is already configured with the proper custom functions and date
     * parsers.
     *
     * @return A new {@link ValangParser}.
     */
    public ValangParser createValangParser(String expression) {
        ValangParser parser = new ValangParser(expression, getAllCustomFunctions(), dateParsers);
        initLifecycle(parser);
        return parser;
    }

    public void initValang(Object object) {
        super.initLifecycle(object);
        if (object instanceof ValangBased) {
            ((ValangBased)object).setCustomFunctions(customFunctions);
            ((ValangBased)object).setDateParsers(dateParsers);
        }
    }

    /**
     * Returns all the custom functions that can be found. This method returns all the custom functions
     * that were explicitly registered with this instance and all custom functions that can be found in the
     * application context (if one is set) by looking for {@link org.springmodules.validation.valang.functions.FunctionDefinition}
     * beans.
     *
     * @return All the custom function that can be found.
     */
    public Map getAllCustomFunctions() {
        Map functionByName = new HashMap();
        functionByName.putAll(customFunctions);
        functionByName.putAll(findAllCustomFunctionsInApplicationContext());
        return functionByName;
    }

    protected Map findAllCustomFunctionsInApplicationContext() {
        Map functionByName = new HashMap();
        if (applicationContext == null) {
            return functionByName;
        }
        String[] names = applicationContext.getBeanNamesForType(FunctionDefinition.class);
        for (int i=0; i<names.length; i++) {
            FunctionDefinition def = (FunctionDefinition)applicationContext.getBean(names[i]);
            functionByName.put(def.getName(), def.getClassName());
        }
        return functionByName;
    }

}
