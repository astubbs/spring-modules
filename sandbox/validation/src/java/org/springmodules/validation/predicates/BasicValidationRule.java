package org.springmodules.validation.predicates;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.collections.Predicate;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springmodules.validation.functions.Function;


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
	private String errorKey = null;

	private Collection errorArgs = null;
	
	public BasicValidationRule(String field, Predicate predicate, String errorMessage) {
		super();
		setField(field);
		setPredicate(predicate);
		setErrorMessage(errorMessage);
	}

	/*
	 * JIRA-MOD-20: added error key and error args to validation rule, kudos to Cèsar Ordiñana.
	 */
	public BasicValidationRule(String field, Predicate predicate, String errorKey, String errorMessage, Collection errorArgs) {
		this(field, predicate, errorMessage);
		setErrorKey(errorKey);
		setErrorArgs(errorArgs);
	}

	private void setPredicate(Predicate predicate) {
		if (predicate == null) {
			throw new IllegalArgumentException("Predicate parameter must not be null!");
		}
		this.predicate = predicate;
	}
	
	public Predicate getPredicate() {
		return this.predicate;
	}

	private void setErrorMessage(String errorMessage) {
		if (errorMessage == null) {
			throw new IllegalArgumentException("Error message parameter must not be null!");
		}
		this.errorMessage = errorMessage;
	}

    public String getErrorMessage() {
		return this.errorMessage;
	}

	private void setField(String field) {
		if (field == null) {
			throw new IllegalArgumentException("Field parameter must not be null!");
		}
		this.field = field;
	}
	
    public String getField() {
		return this.field;
	}
    
    private void setErrorKey(String errorKey) {
		this.errorKey = errorKey;
	}
	
    public String getErrorKey() {
		return this.errorKey;
	}

	private void setErrorArgs(Collection errorArgs) {
		this.errorArgs = errorArgs;
	}
	
    public Collection getErrorArgs() {
		return this.errorArgs;
	}
	
	public void validate(Object target, Errors errors) {
		Object tmpTarget;

		if (target instanceof BeanWrapper || target instanceof Map) {
			tmpTarget = target;
		} else {
			tmpTarget = new BeanWrapperImpl(target);
		}
		if (!getPredicate().evaluate(tmpTarget)) {

			/*
			 * JIRA-MOD-20: take into account error key and error args for localization, kudos to Cèsar Ordiñana.
			 */
			if (StringUtils.hasLength(getErrorKey())) {
				if (getErrorArgs() != null && !getErrorArgs().isEmpty()) {
					Collection tmpColl = new ArrayList();
					for (Iterator iter = getErrorArgs().iterator(); iter.hasNext();) {
						tmpColl.add(((Function)iter.next()).getResult(tmpTarget));
					}
					errors.rejectValue(getField(), getErrorKey(), tmpColl.toArray(), getErrorMessage());
				} else {
					errors.rejectValue(getField(), getErrorKey(), getErrorMessage());
				}
			} else {
				errors.rejectValue(getField(), getField(), getErrorMessage());
			}
			
		}
	}

    
}
