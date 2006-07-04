package org.springmodules.xt.examples.mvc;

import java.util.Collection;
import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;
import org.springmodules.xt.examples.domain.IEmployee;
import org.springmodules.xt.model.introductor.bean.DynamicBeanIntroductor;
import org.springmodules.xt.examples.mvc.form.EmployeeView;
import org.springmodules.xt.model.introductor.collections.IntroductorSet;
import org.springmodules.xt.utils.mvc.controller.EnhancedSimpleFormController;
import org.springmodules.xt.examples.domain.IOffice;
import org.springmodules.xt.examples.domain.MemoryRepository;
import org.springmodules.xt.examples.mvc.form.OfficeView;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

/**
 * Form controller for removing employees from offices.
 *
 * @author Sergio Bossa
 */
public class RemoveEmployeeController extends EnhancedSimpleFormController {
    
    private DynamicBeanIntroductor introductor =  new DynamicBeanIntroductor();
    private MemoryRepository store;

    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        IOffice office = store.getOffice(request.getParameter("office"));
        OfficeView view = null;
        if (office != null) {
            IntroductorSet set = new IntroductorSet(office.getEmployees(), new Class[]{EmployeeView.class}, new Class[]{IEmployee.class}, this.introductor);
            view = (OfficeView) this.introductor.introduceInterfaces(office, new Class[]{OfficeView.class}, new Class[]{IOffice.class});
            view.setSelectableEmployees(set);
        }
        else {
            throw new RuntimeException();
        }
        return view;
    }

    protected ModelAndView onSubmit(Object command, BindException errors) throws Exception {
        OfficeView office = (OfficeView) command;
        Collection empsList = office.getSelectableEmployees();
        
        for (Iterator it = empsList.iterator(); it.hasNext();) {
            EmployeeView emp = (EmployeeView) it.next();
            if (emp.getSelected() != null && emp.getSelected().booleanValue() == true) {
                it.remove();
            }
        }
        
        return new ModelAndView(this.getSuccessView());
    }
    
    public void setStore(MemoryRepository store) {
        this.store = store;
    }
}
