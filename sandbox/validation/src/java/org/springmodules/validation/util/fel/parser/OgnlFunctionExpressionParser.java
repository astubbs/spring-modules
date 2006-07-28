package org.springmodules.validation.util.fel.parser;

import org.springmodules.validation.util.fel.FunctionExpressionParser;
import org.springmodules.validation.util.fel.Function;
import org.springmodules.validation.util.fel.FelParseException;
import org.springmodules.validation.util.fel.FelEvaluationException;
import ognl.Ognl;
import ognl.OgnlException;

/**
 * @author Uri Boness
 */
public class OgnlFunctionExpressionParser implements FunctionExpressionParser {

    public Function parse(String expression) {
        return new OgnlFunction(expression);
    }

    protected class OgnlFunction implements Function {

        private String expressionAsString;
        private Object ognlExpression;

        public OgnlFunction(String expressionAsString) {
            this.expressionAsString = expressionAsString;
            try {
                this.ognlExpression = Ognl.parseExpression(expressionAsString);
            } catch (OgnlException oe) {
                throw new FelParseException("Could not parse OGNL expression '" + expressionAsString + "'", oe);
            }
        }

        public Object evaluate(Object argument) {
            try {
                return Ognl.getValue(ognlExpression, argument);
            } catch (OgnlException oe) {
                throw new FelEvaluationException("Could not evaluate OGNL expression '" + expressionAsString +
                    "' on argument '" + String.valueOf(argument), oe);
            }
        }
    }

}
