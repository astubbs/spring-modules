package org.springmodules.validation.predicates;

import org.apache.commons.collections.Predicate;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.validation.Errors;


/**
 * <p>Validation rule implementation that will validate a target
 * bean an return an error message is the validation fails.
 * 
 * @author Steven Devijver
 * @since 23-04-2005
 */
public class BasicValidationRule implements ValidationRule {

	private Predicate predicate = null;
	private String field = null;
	private String errorMessage = null;
	
	public BasicValidationRule(String field, Predicate predicate, String errorMessage) {
		super();
		setField(field);
		setPredicate(predicate);
		setErrorMessage(errorMessage);
	}

	private void setPredicate(Predicate predicate) {
		if (predicate == null) {
			throw new IllegalArgumentException("Predicate parameter must not be null!");
		}
		this.predicate = predicate;
	}
	
	private Predicate getPredicate() {
		return this.predicate;
	}

	private void setErrorMessage(String errorMessage) {
		if (errorMessage == null) {
			throw new IllegalArgumentException("Error message parameter must not be null!");
		}
		this.errorMessage = errorMessage;
	}

	private String getErrorMessage() {
		return this.errorMessage;
	}

	private void setField(String field) {
		if (field == null) {
			throw new IllegalArgumentException("Field parameter must not be null!");
		}
		this.field = field;
	}
	
	private String getField() {
		return this.field;
	}
	
	public void validate(Object target, Errors errors) {
		BeanWrapper beanWrapper = null;
		if (target instanceof BeanWrapper) {
			beanWrapper = (BeanWrapper)target;
		} else {
			beanWrapper = new BeanWrapperImpl(target);
		}
		if (!getPredicate().evaluate(beanWrapper)) {
			errors.rejectValue(getField(), getField(), getErrorMessage());
		}
	}
}
