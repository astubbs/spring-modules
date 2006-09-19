package org.springmodules.xt.examples.mvc;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springmodules.xt.examples.domain.BusinessException;
import org.springmodules.xt.examples.domain.Error;
import org.springmodules.xt.examples.domain.IOffice;
import org.springmodules.xt.model.introductor.bean.DynamicBeanIntroductor;
import org.springmodules.xt.examples.mvc.form.EmployeeView;
import org.springmodules.xt.examples.domain.Employee;
import org.springmodules.xt.examples.domain.IEmployee;
import org.springmodules.xt.examples.domain.MemoryRepository;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springmodules.web.servlet.mvc.EnhancedSimpleFormController;
import org.springmodules.xt.examples.domain.codes.EmployeeErrorCodes;
import org.springmodules.xt.examples.domain.codes.OfficeErrorCodes;

/**
 * Form controller for inserting an employee, also adding it to an office.
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
        
        if (store.getEmployee(view.getMatriculationCode()) != null) {
            errors.rejectValue("matriculationCode", EmployeeErrorCodes.DUPLICATED_CODE, "Duplicated Matriculation Code!");
            return this.showForm(request, response, errors);
        }
        if (office == null) {
            errors.rejectValue("office", OfficeErrorCodes.NOT_FOUND, "No office found!");
            return this.showForm(request, response, errors);
        }
        else {
            try {
                IEmployee employee = (IEmployee) this.introductor.getTarget(view);
                office.addEmployee(employee);
                store.addOffice(office);
            }
            catch(BusinessException ex) {
                for (Error error : ex.getErrors()) {
                    errors.reject(error.getCode(), error.getMessage());
                }
                return this.showForm(request, response, errors);
            }

            return new ModelAndView(this.getSuccessView(), errors.getModel());
        }
    }
    
    public void setStore(MemoryRepository store) {
        this.store = store;
    }
}
