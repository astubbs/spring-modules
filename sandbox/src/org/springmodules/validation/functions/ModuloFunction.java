package org.springmodules.validation.functions;

public class ModuloFunction extends AbstractMathFunction {

	public ModuloFunction(Function leftFunction, Function rightFunction) {
		super(leftFunction, rightFunction);
	}

	public Object getResult(Object target) {
		return new Double(transform(getLeftFunction().getResult(target)) % transform(getRightFunction().getResult(target)));
	}

}
