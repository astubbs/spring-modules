package org.springframework.samples.petclinic.validation;

import org.springframework.samples.petclinic.Visit;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * <code>Validator</code> for <code>Visit</code> forms.
 *
 * @author Ken Krebs
 * @author Juergen Hoeller
 */
public class VisitValidator implements Validator {

	public boolean supports(Class clazz) {
		return Visit.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		ValidationUtils.rejectIfEmpty(errors, "description", "required", "required");
	}

}
