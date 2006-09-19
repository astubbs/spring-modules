package org.springmodules.xt.examples.mvc;

import java.util.Collection;
import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springmodules.xt.examples.domain.IEmployee;
import org.springmodules.xt.model.introductor.bean.DynamicBeanIntroductor;
import org.springmodules.xt.examples.mvc.form.EmployeeView;
import org.springmodules.xt.model.introductor.collections.IntroductorSet;
import org.springmodules.xt.examples.domain.IOffice;
import org.springmodules.xt.examples.domain.MemoryRepository;
import org.springmodules.xt.examples.mvc.form.OfficeView;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springmodules.web.servlet.mvc.EnhancedSimpleFormController;
import org.springmodules.xt.examples.domain.BusinessException;

/**
 * Form controller for removing employees from offices.
 *
 * @author Sergio Bossa
 */
public class RemoveEmployeeController extends EnhancedSimpleFormController {
    
    private DynamicBeanIntroductor introductor =  new DynamicBeanIntroductor();
    private MemoryRepository store;
    
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        IOffice office = store.getOffice(request.getParameter("officeId"));
        OfficeView view = null;
        if (office != null) {
            IntroductorSet set = new IntroductorSet(office.getEmployees(), new Class[]{EmployeeView.class}, new Class[]{IEmployee.class}, this.introductor);
            view = (OfficeView) this.introductor.introduceInterfaces(office, new Class[]{OfficeView.class}, new Class[]{IOffice.class});
            view.setSelectableEmployees(set);
        } else {
            throw new RuntimeException("No office.");
        }
        return view;
    }
    
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        OfficeView view = (OfficeView) command;
        Collection<EmployeeView> empsList = view.getSelectableEmployees();
        IOffice office = (IOffice) this.introductor.getTarget(view);
        
        for (Iterator it = empsList.iterator(); it.hasNext();) {
            EmployeeView emp = (EmployeeView) it.next();
            if (emp.getSelected() != null && emp.getSelected().booleanValue() == true) {
                it.remove();
            }
        }
        try {
            store.addOffice(office);
        } 
        catch(BusinessException ex) {
            for (org.springmodules.xt.examples.domain.Error error : ex.getErrors()) {
                errors.reject(error.getCode(), error.getMessage());
            }
            return this.showForm(request, response, errors);
        }
        
        return new ModelAndView(this.getSuccessView());
    }
    
    public void setStore(MemoryRepository store) {
        this.store = store;
    }
}
