package org.springmodules.xt.examples.mvc;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springmodules.xt.examples.domain.BusinessException;
import org.springmodules.xt.examples.domain.Error;
import org.springmodules.xt.examples.domain.IOffice;
import org.springmodules.xt.examples.domain.Office;
import org.springmodules.xt.examples.domain.MemoryRepository;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springmodules.web.servlet.mvc.EnhancedSimpleFormController;
import org.springmodules.xt.ajax.web.servlet.AjaxModelAndView;
import org.springmodules.xt.examples.domain.codes.OfficeErrorCodes;

/**
 * Form controller for inserting an office.
 *
 * @author Sergio Bossa
 */
public class InsertOfficeController extends EnhancedSimpleFormController {
    
    private MemoryRepository store;
    
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        return new Office();
    }
    
    protected Map referenceData(HttpServletRequest request) throws Exception {
        Collection emps = store.getEmployees();
        Map result = new HashMap();
        
        result.put("employees", emps);
        
        return result;
    }
    
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        IOffice office = (IOffice) command;
        
        if (store.getOffice(office.getOfficeId()) != null) {
            errors.rejectValue("officeId", OfficeErrorCodes.DUPLICATED_ID, "Duplicated Office Id!");
            return this.showForm(request, response, errors);
        }
        else {
            try {
                this.store.addOffice(office);
            }
            catch(BusinessException ex) {
                for (Error error : ex.getErrors()) {
                    errors.rejectValue(error.getPropertyName(), error.getCode(), error.getMessage());
                }
                return this.showForm(request, response, errors);
            }

            return new AjaxModelAndView(this.getSuccessView(), errors);
        }
    }
    
    public void setStore(MemoryRepository store) {
        this.store = store;
    }
}
