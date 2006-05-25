package org.springmodules.validation.valang.predicates;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;

import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.collections.Predicate;
import org.springmodules.validation.valang.functions.Function;

/**
 * <p>AbstractPropertyPredicate deals with extracting values from properties and as such
 * is a utilty base class.
 * 
 * @author Steven Devijver
 * @since 23-04-2005
 */
public abstract class AbstractPropertyPredicate implements Predicate {

    private Function leftFunction = null;
    private Operator operator = null;
    private Function rightFunction = null;
    private int line = 0;
    private int column = 0;

    public AbstractPropertyPredicate(Function leftFunction, Operator operator, Function rightFunction, int line, int column) {
        super();
        setLeftFunction(leftFunction);
        setOperator(operator);
        setRightFunction(rightFunction);
        setLine(line);
        setColumn(column);
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

    private void setOperator(Operator operator) {
        if (operator == null) {
            throw new IllegalArgumentException("Operator parameter should not be null!");
        }
        this.operator = operator;
    }

    public final Operator getOperator() {
        return this.operator;
    }

    private void setRightFunction(Function rightFunction) {
        this.rightFunction = rightFunction;
    }

    public final Function getRightFunction() {
        return this.rightFunction;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    protected final Iterator getIterator(Object literal) {
        if (literal instanceof Collection) {
            return ((Collection)literal).iterator();
        } else if (literal instanceof Iterator) {
            return (Iterator)literal;
        } else if (literal instanceof Enumeration) {
            return IteratorUtils.asIterator((Enumeration)literal);
        } else if (literal instanceof Object[]) {
            return IteratorUtils.arrayIterator((Object[])literal);
        } else {
            throw new ClassCastException("Could not convert literal value to iterator!");
        }
    }

    protected final Object[] getArray(Object literal) {
        if (literal instanceof Collection) {
            return ((Collection)literal).toArray();
        } else if (literal instanceof Iterator) {
            return IteratorUtils.toArray((Iterator)literal);
        } else if (literal instanceof Enumeration) {
            return IteratorUtils.toArray(IteratorUtils.asIterator((Enumeration)literal));
        } else if (literal instanceof Object[]) {
            return (Object[])literal;
        } else {
            throw new ClassCastException("Could not convert literal value to array!");
        }
    }

    public abstract boolean evaluate(Object target);

    protected abstract Predicate getPredicate(Function leftFunction, Operator operator, Function rightFunction, int line, int column);
}
