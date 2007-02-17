package org.springmodules.xt.examples.mvc;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springmodules.web.servlet.XTModelAndView;
import org.springmodules.web.servlet.mvc.EnhancedSimpleFormController;
import org.springmodules.xt.examples.domain.IEmployee;
import org.springmodules.xt.examples.mvc.form.EmployeesListForm;
import org.springmodules.xt.examples.domain.MemoryRepository;
import org.springmodules.xt.examples.domain.Office;

/**
 * Form controller for listing employees.
 *
 * @author Sergio Bossa
 */
public class ListEmployeesController extends EnhancedSimpleFormController {
    
    private MemoryRepository store;

    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        return new EmployeesListForm();
    }
    
    protected Map referenceData(HttpServletRequest request) throws Exception {
        Collection offices = store.getOffices();
        Map result = new HashMap();
        
        result.put("offices", offices);
        
        return result;
    }

    protected ModelAndView onSubmit(Object command, BindException errors) throws Exception {
        EmployeesListForm form = (EmployeesListForm) command;
        Office office = form.getOffice();
        Collection<IEmployee> employees = store.getEmployeesByOffice(office);
        Map model = new HashMap(3);
        
        model.put("employees", employees);
        
        return new XTModelAndView(this.getSuccessView(), errors, model);
    }
    
    public void setStore(MemoryRepository store) {
        this.store = store;
    }
}
