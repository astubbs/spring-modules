package org.springmodules.xt.examples.mvc.form;

import org.springmodules.xt.examples.domain.Office;

/**
 * Form object for employees list.
 *
 * @author Sergio Bossa
 */
public class EmployeesListForm {
    private Office office;

    public Office getOffice() {
        return office;
    }

    public void setOffice(Office office) {
        this.office = office;
    }
}
