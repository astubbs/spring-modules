package org.springframework.samples.petclinic.validation;

import org.springframework.samples.petclinic.Pet;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * <code>Validator</code> for <code>Pet</code> forms.
 *
 * @author Ken Krebs
 * @author Juergen Hoeller
 */
public class PetValidator implements Validator {

	public boolean supports(Class clazz) {
		return Pet.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		Pet pet = (Pet) obj;

		String name = pet.getName();
		if (!StringUtils.hasLength(name)) {
			errors.rejectValue("name", "required", "required");
		}
		else if (pet.isNew() && pet.getOwner().getPet(name, true) != null) {
			errors.rejectValue("name", "duplicate", "already exists");
		}
	}

}
