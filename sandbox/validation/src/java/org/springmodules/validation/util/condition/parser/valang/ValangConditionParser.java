package org.springmodules.validation.util.condition.parser.valang;

import java.io.StringReader;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ClassUtils;
import org.springmodules.validation.util.LifecycleAware;
import org.springmodules.validation.util.condition.Condition;
import org.springmodules.validation.util.condition.adapter.CommonsPredicateCondition;
import org.springmodules.validation.util.condition.parser.ConditionParseException;
import org.springmodules.validation.util.condition.parser.ConditionParser;
import org.springmodules.validation.util.date.DefaultDateParser;
import org.springmodules.validation.valang.functions.Function;
import org.springmodules.validation.valang.parser.ParseException;
import org.springmodules.validation.valang.parser.ValangParser;
import org.springmodules.validation.valang.parser.ValangVisitor;

/**
 * @author Uri Boness
 */
public class ValangConditionParser extends LifecycleAware implements ConditionParser {

    private final static Log logger = LogFactory.getLog(ValangConditionParser.class);

    private Map dateParserRegistrations = null;

    private Map customFunctions = null;

    public ValangConditionParser() {
    }

    public ValangConditionParser(Map customFunctions, Map dateParserRegistrations) {
        this.customFunctions = customFunctions;
        this.dateParserRegistrations = dateParserRegistrations;
    }

    public Condition parse(String text) throws ConditionParseException {
        try {
            return new CommonsPredicateCondition(createValangParser(text).parseExpression());
        } catch (ParseException pe) {
            throw new ConditionParseException("Could not parse Valang expression '" + text + "'", pe);
        }
    }

    private ValangParser createValangParser(String text) {
        ValangParser parser = new ValangParser(new StringReader(text));
        initLifecycle(parser.getVisitor());

        if (dateParserRegistrations != null) {
                for (Iterator iter = dateParserRegistrations.keySet().iterator(); iter.hasNext();) {
                    String regexp = (String)iter.next();
                    Object value = dateParserRegistrations.get(regexp);

                    if (value instanceof String) {
                        parser.getVisitor().getDateParser().register(regexp, (String)value);
                    }
                    else if (value instanceof DefaultDateParser.DateModifier) {
                        parser.getVisitor().getDateParser().register(regexp, (DefaultDateParser.DateModifier)value);
                    }
                    else {
                        throw new ClassCastException("Could not register instance [" + value + "] with date parser!");
                    }
                }
            }

            if (customFunctions != null) {
                final Map constructorMap = new HashMap();
                for (Iterator iter = customFunctions.keySet().iterator(); iter.hasNext();) {
                    Object stringNameObject = iter.next();
                    if (!(stringNameObject instanceof String)) {
                        throw new IllegalArgumentException("Key for custom functions map must be a string value!");
                    }
                    String functionName = (String)stringNameObject;
                    Object functionClassNameObject = customFunctions.get(functionName);
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
            }

        return parser;
    }

    public void setDateParserRegistrations(Map dateParserRegistrations) {
        this.dateParserRegistrations = dateParserRegistrations;
    }

    public void setCustomFunctions(Map customFunctions) {
        this.customFunctions = customFunctions;
    }

    //=============================================== Helper Methods ===================================================

    protected Class loadClass(String className) {
        try {
            return ClassUtils.forName(className);
        } catch (ClassNotFoundException cnfe) {
            logger.error("Could not load class '" + className + "'", cnfe);
            throw new IllegalArgumentException("Could not load class '" + className + "'");
        }
    }

}
