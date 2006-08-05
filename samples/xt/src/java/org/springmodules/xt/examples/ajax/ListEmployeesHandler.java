package org.springmodules.xt.examples.ajax;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import org.springmodules.xt.ajax.AbstractAjaxHandler;
import org.springmodules.xt.ajax.AjaxResponse;
import org.springmodules.xt.ajax.AjaxResponseImpl;
import org.springmodules.xt.ajax.AjaxSubmitEvent;
import org.springmodules.xt.ajax.action.ReplaceContentAction;
import org.springmodules.xt.ajax.component.SimpleText;
import org.springmodules.xt.ajax.component.TableRow;
import org.springmodules.xt.examples.domain.IEmployee;
import org.springmodules.xt.examples.domain.MemoryRepository;
import org.springmodules.xt.examples.mvc.form.EmployeesListForm;

/**
 * Ajax handler for listing employees.
 *
 * @author Sergio Bossa
 */
public class ListEmployeesHandler extends AbstractAjaxHandler {
    
    private MemoryRepository store;
    
    public AjaxResponse listEmployees(AjaxSubmitEvent event) {
        EmployeesListForm form = (EmployeesListForm) event.getCommandObject();
        Collection<IEmployee> employees = (Collection<IEmployee>) event.getModel().get("employees");
        
        // Create the simple text message:
        SimpleText message = new SimpleText(new StringBuilder("Selected office: ").append(form.getOffice().getName()).toString());
        // Create an ajax action for setting the message:
        ReplaceContentAction setMessage = new ReplaceContentAction("message", message);
        
        // Create the components to render (a list of html table rows):
        List rows = new LinkedList();
        for(IEmployee emp : employees) {
            TableRow row = new TableRow(emp, new String[]{"firstname", "surname", "matriculationCode"}, null);
            rows.add(row);
        }
        // Create an ajax action for replacing the old table body content, inserting these new rows:
        ReplaceContentAction replaceRows = new ReplaceContentAction("employees", rows);
        
        // Create a concrete ajax response:
       AjaxResponse response = new AjaxResponseImpl();
        // Add actions:
        response.addAction(setMessage);
        response.addAction(replaceRows);
        
        return response;
    }

    public void setStore(MemoryRepository store) {
        this.store = store;
    }
}
