package org.springmodules.xt.test.domain;

import java.util.Set;

/**
 * Office form view.
 *
 * @author Sergio Bossa
 */
public interface OfficeView extends IOffice {
    
    void setSelectableEmployees(Set employees);
    
    Set getSelectableEmployees();
    
    void setSelected(Boolean selected);
    
    Boolean isSelected();
}
