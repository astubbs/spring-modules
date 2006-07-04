package org.springmodules.xt.examples.mvc;

import org.springmodules.xt.examples.domain.codes.EmployeeErrorCodes;
import org.springmodules.xt.examples.domain.codes.OfficeErrorCodes;
import org.springmodules.xt.examples.mvc.form.EmployeeView;
import org.springmodules.xt.examples.domain.IEmployee;
import org.springmodules.xt.examples.domain.MemoryRepository;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Validates employee insertion.
 *
 * @author Sergio Bossa
 */
public class InsertEmployeeValidator implements Validator {
    
    private MemoryRepository store;
  
    public boolean supports(Class aClass) {
        return IEmployee.class.isAssignableFrom(aClass);
    }

    public void validate(Object object, Errors errors) {
        if (this.supports(object.getClass())) {
            
            EmployeeView emp = (EmployeeView) object;
            
            // Validate matriculation code:
            if (emp.getMatriculationCode() == null || emp.getMatriculationCode().equals("")) {
                errors.rejectValue("matriculationCode", EmployeeErrorCodes.NULL_CODE, "No Matriculation Code!");
            }
            else if (store.getEmployee(emp.getMatriculationCode()) != null) {
                errors.rejectValue("matriculationCode", EmployeeErrorCodes.DUPLICATED_CODE, "Duplicated Matriculation Code!");
            }
            
            // Validate office not null:
            if (emp.getOffice() == null) {
                errors.rejectValue("office", OfficeErrorCodes.NOT_FOUND, "No office found!");
            }
        }
    }
  
    public void setStore(MemoryRepository store) {
        this.store = store;
    }
}
