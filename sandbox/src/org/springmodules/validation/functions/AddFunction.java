package org.springmodules.validation.functions;


public class AddFunction extends AbstractMathFunction {

	public AddFunction(Function leftFunction, Function rightFunction) {
		super(leftFunction, rightFunction);
	}

	public Object getResult(Object target) {
		return new Double(transform(getLeftFunction().getResult(target)) + transform(getRightFunction().getResult(target)));
	}

}
