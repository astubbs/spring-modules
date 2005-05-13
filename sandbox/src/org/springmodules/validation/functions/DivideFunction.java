package org.springmodules.validation.functions;

public class DivideFunction extends AbstractMathFunction {

	public DivideFunction(Function leftFunction, Function rightFunction) {
		super(leftFunction, rightFunction);
	}

	public Object getResult(Object target) {
		return new Double(transform(getLeftFunction().getResult(target)) / transform(getRightFunction().getResult(target)));
	}

}
