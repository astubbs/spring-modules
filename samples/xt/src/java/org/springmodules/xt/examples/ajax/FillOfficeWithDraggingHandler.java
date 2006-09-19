package org.springmodules.xt.examples.ajax;

import org.springmodules.xt.ajax.AbstractAjaxHandler;
import org.springmodules.xt.ajax.AjaxActionEvent;
import org.springmodules.xt.ajax.AjaxResponse;
import org.springmodules.xt.ajax.AjaxResponseImpl;
import org.springmodules.xt.ajax.action.AppendContentAction;
import org.springmodules.xt.ajax.component.InputField;
import org.springmodules.xt.ajax.component.ListItem;
import org.springmodules.xt.ajax.component.SimpleText;
import org.springmodules.xt.examples.domain.IEmployee;
import org.springmodules.xt.examples.domain.IOffice;
import org.springmodules.xt.examples.domain.MemoryRepository;

/**
 * Ajax handler for filling an office using dragging.
 *
 * @author Sergio Bossa
 */
public class FillOfficeWithDraggingHandler extends AbstractAjaxHandler {
    
    private static final String EMPLOYEE_ID = "employeeId";
    private static final String OFFICE_ID = "officeId";
    
    private MemoryRepository store;
    
    public AjaxResponse dragEmployee(AjaxActionEvent event) {
        IEmployee draggedEmployee = store.getEmployee(event.getParameters().get(EMPLOYEE_ID));
        IOffice droppableOffice = store.getOffice(event.getHttpRequest().getParameter(OFFICE_ID));
        
        if (! droppableOffice.getEmployees().contains(draggedEmployee)) {
            ListItem item = new ListItem(new SimpleText(draggedEmployee.getFirstname() + " " + draggedEmployee.getSurname()));
            InputField hidden = new InputField("employees", draggedEmployee.getMatriculationCode(), InputField.InputType.HIDDEN);
            AppendContentAction appendAction1 = new AppendContentAction("employees", item);
            AppendContentAction appendAction2 = new AppendContentAction("employees", hidden);
            AjaxResponse response = new AjaxResponseImpl();
            response.addAction(appendAction1);
            response.addAction(appendAction2);
            
            return response;
        } 
        else {
            return null;
        }
    }
    
    public void setStore(MemoryRepository store) {
        this.store = store;
    }
}
