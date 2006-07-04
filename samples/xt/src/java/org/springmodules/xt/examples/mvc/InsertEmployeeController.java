package org.springmodules.xt.examples.mvc;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springmodules.xt.examples.domain.BusinessException;
import org.springmodules.xt.examples.domain.Error;
import org.springmodules.xt.examples.domain.IOffice;
import org.springmodules.xt.examples.domain.codes.OfficeErrorCodes;
import org.springmodules.xt.model.introductor.bean.DynamicBeanIntroductor;
import org.springmodules.xt.examples.mvc.form.EmployeeView;
import org.springmodules.xt.utils.mvc.controller.EnhancedSimpleFormController;
import org.springmodules.xt.examples.domain.Employee;
import org.springmodules.xt.examples.domain.IEmployee;
import org.springmodules.xt.examples.domain.MemoryRepository;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

/**
 * Form controller for inserting an employee.
 *
 * @author Sergio Bossa
 */
public class InsertEmployeeController extends EnhancedSimpleFormController {
    
    private DynamicBeanIntroductor introductor =  new DynamicBeanIntroductor();
    private MemoryRepository store;
    
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        return this.introductor.introduceInterfaces(new Employee(), new Class[]{EmployeeView.class}, new Class[]{IEmployee.class});
    }

    protected Map referenceData(HttpServletRequest request) throws Exception {
        Collection offices = store.getOffices();
        Map result = new HashMap();
        
        result.put("offices", offices);
        
        return result;
    }

    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        EmployeeView view = (EmployeeView) command;
        IOffice office = view.getOffice();
        IEmployee employee = (IEmployee) this.introductor.getTarget(view);
        
        try {
            office.addEmployee(employee);
            store.addEmployee(employee);
            store.addOffice(office);
            
            return new ModelAndView(this.getSuccessView());
        }
        catch(BusinessException ex) {
            for (Error error : ex.getErrors()) {
                if (error.getCode().equals(OfficeErrorCodes.FULL)) {
                    errors.rejectValue("office", error.getCode(), error.getMessage());
                }
                else {
                    errors.rejectValue(error.getPropertyName(), error.getCode(), error.getMessage());
                }
            }
            return this.showForm(request, response, errors);
        }
    }
    
    public void setStore(MemoryRepository store) {
        this.store = store;
    }
}
