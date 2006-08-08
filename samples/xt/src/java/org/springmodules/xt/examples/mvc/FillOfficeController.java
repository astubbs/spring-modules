package org.springmodules.xt.examples.mvc;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springmodules.web.servlet.mvc.EnhancedSimpleFormController;
import org.springmodules.xt.examples.domain.BusinessException;
import org.springmodules.xt.examples.domain.Error;
import org.springmodules.xt.examples.domain.MemoryRepository;
import org.springmodules.xt.examples.domain.Office;

/**
 * Form controller for filling an offices with a new list of employees.
 *
 * @author Sergio Bossa
 */
public class FillOfficeController extends EnhancedSimpleFormController {
    
    private MemoryRepository store;

    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        String officeId = request.getParameter("officeId");
        if (officeId == null) {
            return new Office();
        }
        else {
            return this.store.getOffice(officeId);
        }
    }
    
    protected Map referenceData(HttpServletRequest request) throws Exception {
        Collection offices = store.getOffices();
        Map result = new HashMap();
        
        result.put("offices", offices);
        
        return result;
    }

    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        Office office = (Office) command;
        
        try {
            this.store.addOffice(office);
        }
        catch(BusinessException ex) {
            for (Error error : ex.getErrors()) {
                errors.rejectValue(error.getPropertyName(), error.getCode(), error.getMessage());
            }
            return this.showForm(request, response, errors);
        }
        
        return new ModelAndView(this.getSuccessView(), errors.getModel());
    }
    
    public void setStore(MemoryRepository store) {
        this.store = store;
    }
}
