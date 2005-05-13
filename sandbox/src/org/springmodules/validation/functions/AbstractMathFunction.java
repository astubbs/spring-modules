package org.springmodules.validation.functions;

import java.math.BigDecimal;
import java.math.BigInteger;

public abstract class AbstractMathFunction implements Function {

	private Function leftFunction = null;
	private Function rightFunction = null;
	
	public AbstractMathFunction(Function leftFunction, Function rightFunction) {
		super();
		
		setLeftFunction(leftFunction);
		setRightFunction(rightFunction);
	}

	private void setLeftFunction(Function leftFunction) {
		if (leftFunction == null) {
			throw new IllegalArgumentException("Left function parameter should not be null!");
		}
		
		this.leftFunction = leftFunction;
	}
	
	protected final Function getLeftFunction() {
		return this.leftFunction;
	}

	private void setRightFunction(Function rightFunction) {
		if (rightFunction == null) {
			throw new IllegalArgumentException("Right function parameter should not be null!");
		}
		
		this.rightFunction = rightFunction;
	}
	
	protected final Function getRightFunction() {
		return this.rightFunction;
	}
	
	protected final static double transform(Object o) {
		if (o instanceof Number) {
			return new BigDecimal(o.toString()).doubleValue();
		} else if (o instanceof BigInteger) {
			return new BigDecimal((BigInteger)o).doubleValue();
		} else if (o instanceof BigDecimal) {
			return ((BigDecimal)o).doubleValue();
		} else if (o instanceof String) {
			return new BigDecimal((String)o).doubleValue();
		} else {
			throw new IllegalArgumentException("Could not parse instance of class [" + o.getClass().getName() + "]!");
		}
	}
}
