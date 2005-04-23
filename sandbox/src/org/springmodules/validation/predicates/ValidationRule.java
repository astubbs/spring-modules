package org.springmodules.validation.predicates;

import org.springframework.validation.Errors;


/**
 * <p>This interface represents a validation rule that will
 * validate a target bean.
 * 
 * @author Steven Devijver
 * @since 23-04-2005
 */
public interface ValidationRule {

	public void validate(Object target, Errors errors);
}
