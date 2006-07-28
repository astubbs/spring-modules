package org.springmodules.validation.util.fel.parser;

import org.springmodules.validation.valang.parser.SimpleValangBased;
import org.springmodules.validation.valang.parser.ValangParser;
import org.springmodules.validation.valang.parser.ParseException;
import org.springmodules.validation.valang.functions.TargetBeanFunction;
import org.springmodules.validation.util.fel.FunctionExpressionParser;
import org.springmodules.validation.util.fel.Function;
import org.springmodules.validation.util.fel.FelParseException;
import org.springmodules.validation.util.fel.FelEvaluationException;

/**
 * @author Uri Boness
 */
public class ValangFunctionExpressionParser extends SimpleValangBased implements FunctionExpressionParser {

    public Function parse(String expression) {
        return new ValangFunction(expression);
    }

    protected class ValangFunction implements Function {

        private String valangExpression;
        private org.springmodules.validation.valang.functions.Function valangFunction;

        public ValangFunction(String valangExpression) {
            this.valangExpression = valangExpression;
            ValangParser parser = createValangParser(valangExpression);
            try {
                this.valangFunction = parser.function(new TargetBeanFunction());
            } catch (ParseException pe) {
                throw new FelParseException("Could not parse valang function expression '" +
                    valangExpression + "'", pe);
            }
        }

        public Object evaluate(Object argument) {
            try {
                return valangFunction.getResult(argument);
            } catch (Throwable t) {
                throw new FelEvaluationException("Could not evaluate valang expression '" +
                    valangExpression + "' on bean '" + String.valueOf(argument) + "'", t);
            }
        }
    }

}
