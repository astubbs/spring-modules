package org.springmodules.xt.examples.mvc;

import org.springmodules.xt.examples.domain.FullOfficeSpecification;
import org.springmodules.xt.examples.domain.IOffice;
import org.springmodules.xt.examples.domain.OfficeIdSpecification;
import org.springmodules.xt.examples.domain.codes.OfficeErrorCodes;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Validate offices.
 *
 * @author Sergio Bossa
 */
public class OfficeValidator implements Validator {
    
    private FullOfficeSpecification fullOfficeSpecification = new FullOfficeSpecification();
    private OfficeIdSpecification officeIdSpecification = new OfficeIdSpecification();
    
    public boolean supports(Class aClass) {
        return IOffice.class.isAssignableFrom(aClass);
    }

    public void validate(Object object, Errors errors) {
        if (this.supports(object.getClass())) {
            IOffice office = (IOffice) object;
            
            // Validate office id:
            if (office.getOfficeId() == null || office.getOfficeId().equals("")) {
                errors.rejectValue("officeId", OfficeErrorCodes.NULL_ID, "No Office Id!");
            }
            if (! this.officeIdSpecification.isSatisfiedBy(office)) {
                errors.rejectValue("officeId", OfficeErrorCodes.WRONG_ID, "Wrong Office Id!");
            }
            
            // Validate office name:
            if (office.getName() == null || office.getName().equals("")) {
                errors.rejectValue("name", OfficeErrorCodes.NULL_NAME, "No office name!");
            }
            
            // Is office full?
            if (this.fullOfficeSpecification.isSatisfiedBy(office)) {
                errors.rejectValue("employees", OfficeErrorCodes.FULL, "Full Office!");
            }
        }
    }
}
