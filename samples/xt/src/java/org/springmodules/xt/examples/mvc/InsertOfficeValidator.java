package org.springmodules.xt.examples.mvc;

import org.springmodules.xt.examples.domain.IOffice;
import org.springmodules.xt.examples.domain.MemoryRepository;
import org.springmodules.xt.examples.domain.codes.OfficeErrorCodes;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Validate office insertion.
 *
 * @author Sergio Bossa
 */
public class InsertOfficeValidator implements Validator {
    
    private MemoryRepository store;
    
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
            if (store.getOffice(office.getOfficeId()) != null) {
                errors.rejectValue("officeId", OfficeErrorCodes.DUPLICATED_ID, "Duplicated Office Id!");
            }
            
            // Validate office name:
            if (office.getName() == null || office.getName().equals("")) {
                errors.rejectValue("name", OfficeErrorCodes.NULL_NAME, "No office name!");
            }
        }
    }
    
    public void setStore(MemoryRepository store) {
        this.store = store;
    }
}
