package org.springmodules.validation.valang.functions;

import java.math.BigDecimal;
import java.math.BigInteger;

public abstract class AbstractMathFunction implements Function {

    private Function leftFunction = null;
    private Function rightFunction = null;
    private FunctionTemplate template = null;

    public AbstractMathFunction(Function leftFunction, Function rightFunction, int line, int column) {
        super();

        setLeftFunction(leftFunction);
        setRightFunction(rightFunction);
        setTemplate(new FunctionTemplate(line, column));
    }

    private void setLeftFunction(Function leftFunction) {
        if (leftFunction == null) {
            throw new IllegalArgumentException("Left function parameter should not be null!");
        }

        this.leftFunction = leftFunction;
    }

    public final Function getLeftFunction() {
        return this.leftFunction;
    }

    private void setRightFunction(Function rightFunction) {
        if (rightFunction == null) {
            throw new IllegalArgumentException("Right function parameter should not be null!");
        }

        this.rightFunction = rightFunction;
    }

    public final Function getRightFunction() {
        return this.rightFunction;
    }

    private void setTemplate(FunctionTemplate template) {
        this.template = template;
    }

    protected FunctionTemplate getTemplate() {
        return this.template;
    }

    protected static double transform(Object o) {
        if (o instanceof BigInteger) {
            return new BigDecimal((BigInteger)o).doubleValue();
        } else if (o instanceof BigDecimal) {
            return ((BigDecimal)o).doubleValue();
        } else if (o instanceof Number) {
            return new BigDecimal(o.toString()).doubleValue();
        } else  if (o instanceof String) {
            return new BigDecimal((String)o).doubleValue();
        } else {
            throw new IllegalArgumentException("Could not parse instance of class [" + o.getClass().getName() + "]!");
        }
    }
}
