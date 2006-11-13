package org.springmodules.xt.examples.mvc;

import org.springmodules.xt.examples.domain.codes.EmployeeErrorCodes;
import org.springmodules.xt.examples.domain.IEmployee;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Validate employees.
 *
 * @author Sergio Bossa
 */
public class EmployeeValidator implements Validator {
    
    public static final int PASSWORD_MIN_LENGTH = 5;
    
    public boolean supports(Class aClass) {
        return IEmployee.class.isAssignableFrom(aClass);
    }

    public void validate(Object object, Errors errors) {
        if (this.supports(object.getClass())) {
            IEmployee emp = (IEmployee) object;
            
            // Validate matriculation code:
            if (emp.getMatriculationCode() == null || emp.getMatriculationCode().equals("")) {
                errors.rejectValue("matriculationCode", EmployeeErrorCodes.NULL_CODE, "No Matriculation Code!");
            }
            
            // Validate password:
            if (emp.getPassword().length() < PASSWORD_MIN_LENGTH) {
                errors.rejectValue("password", EmployeeErrorCodes.SHORT_PASSWORD, new Object[]{PASSWORD_MIN_LENGTH}, "Too short password!");
            }
        }
    }
}
