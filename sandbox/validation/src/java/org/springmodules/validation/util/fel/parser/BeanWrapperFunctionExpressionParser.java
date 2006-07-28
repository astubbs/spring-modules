package org.springmodules.validation.util.fel.parser;

import org.springmodules.validation.util.fel.FunctionExpressionParser;
import org.springmodules.validation.util.fel.Function;
import org.springmodules.validation.util.fel.FelEvaluationException;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;

/**
 * @author Uri Boness
 */
public class BeanWrapperFunctionExpressionParser implements FunctionExpressionParser {

    public Function parse(String expression) {
        return new BeanWrapperFunction(expression);
    }

    protected class BeanWrapperFunction implements Function {

        private String propertyPath;

        public BeanWrapperFunction(String propertyPath) {
            this.propertyPath = propertyPath;
        }

        public Object evaluate(Object argument) {
            try {
                return new BeanWrapperImpl(argument).getPropertyValue(propertyPath);
            } catch (Throwable t) {
                throw new FelEvaluationException("Could not evaluate path '" + propertyPath +
                    "' on bean '" + String.valueOf(argument) + "'", t);
            }
        }
    }

}
