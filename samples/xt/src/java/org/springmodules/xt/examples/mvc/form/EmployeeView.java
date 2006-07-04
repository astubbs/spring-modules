package org.springmodules.xt.examples.mvc.form;

import org.springmodules.xt.examples.domain.IEmployee;
import org.springmodules.xt.examples.domain.Office;

/**
 * Employee form view.
 *
 * @author Sergio Bossa
 */
public interface EmployeeView extends IEmployee {
    
    void setOffice(Office office);
    
    Office getOffice();
    
    void setSelected(Boolean selected);
    
    Boolean getSelected();
}
