package org.springmodules.validation.functions;

public class ModuloFunction extends AbstractMathFunction {

	public ModuloFunction(Function leftFunction, Function rightFunction, int line, int column) {
		super(leftFunction, rightFunction, line, column);
	}

	public Object getResult(Object target) {
		return getTemplate().execute(target, new FunctionCallback() {
			public Object execute(Object target) throws Exception {
				return new Double(transform(getLeftFunction().getResult(target)) % transform(getRightFunction().getResult(target)));
			}
		});
	}

}
