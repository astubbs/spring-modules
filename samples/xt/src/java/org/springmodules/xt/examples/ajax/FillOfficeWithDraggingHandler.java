package org.springmodules.xt.examples.ajax;

import java.util.Arrays;
import org.springmodules.xt.ajax.AbstractAjaxHandler;
import org.springmodules.xt.ajax.AjaxActionEvent;
import org.springmodules.xt.ajax.AjaxResponse;
import org.springmodules.xt.ajax.AjaxResponseImpl;
import org.springmodules.xt.ajax.action.AppendContentAction;
import org.springmodules.xt.ajax.component.InputField;
import org.springmodules.xt.ajax.component.ListItem;
import org.springmodules.xt.ajax.component.SimpleText;
import org.springmodules.xt.ajax.component.support.BindStatusHelper;
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
            BindStatusHelper helper = new BindStatusHelper("command.employees");
            
            ListItem item = new ListItem(new SimpleText(draggedEmployee.getFirstname() + " " + draggedEmployee.getSurname()));
            InputField hidden = new InputField(helper.getStatusExpression(), draggedEmployee.getMatriculationCode(), InputField.InputType.HIDDEN);
            
            AppendContentAction appendAction = new AppendContentAction("employees", Arrays.asList(item, hidden));
            
            AjaxResponse response = new AjaxResponseImpl();
            response.addAction(appendAction);
            
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
