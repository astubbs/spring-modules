package org.springmodules.validation.functions;

/**
 * <p>This function returns the target bean.
 * 
 * @author Steven Devijver
 * @since 29-04-2005
 *
 */
public class TargetBeanFunction implements Function {

	public TargetBeanFunction() {
		super();
	}

	public Object getResult(Object target) {
		return target;
	}

}
