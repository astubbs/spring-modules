package org.springmodules.xt.test.domain;

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
