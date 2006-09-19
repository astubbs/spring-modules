package org.springmodules.xt.examples.mvc.form;

import java.util.Set;
import org.springmodules.xt.examples.domain.IOffice;

/**
 * Office form view.
 *
 * @author Sergio Bossa
 */
public interface OfficeView extends IOffice {
    
    void setSelectableEmployees(Set<EmployeeView> employees);
    
    Set<EmployeeView> getSelectableEmployees();
}
