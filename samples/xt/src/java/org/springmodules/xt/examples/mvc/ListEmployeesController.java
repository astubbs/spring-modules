package org.springmodules.xt.examples.mvc;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springmodules.xt.examples.mvc.form.EmployeesListForm;
import org.springmodules.xt.utils.mvc.controller.EnhancedSimpleFormController;
import org.springmodules.xt.examples.domain.MemoryRepository;

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

    public void setStore(MemoryRepository store) {
        this.store = store;
    }
}
