package org.springmodules.xt.examples.ajax;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.springmodules.xt.ajax.AbstractAjaxHandler;
import org.springmodules.xt.ajax.AjaxResponse;
import org.springmodules.xt.ajax.AjaxResponseImpl;
import org.springmodules.xt.ajax.AjaxSubmitEvent;
import org.springmodules.xt.ajax.action.ReplaceContentAction;
import org.springmodules.xt.ajax.action.prototype.scriptaculous.HighlightAction;
import org.springmodules.xt.ajax.component.SimpleText;
import org.springmodules.xt.ajax.component.TableRow;
import org.springmodules.xt.examples.domain.IEmployee;
import org.springmodules.xt.examples.domain.IOffice;
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
        Map model = event.getModel();
        IOffice office = form.getOffice();
        Collection<IEmployee> employees = (Collection) model.get("employees");
        
        // Create the simple text message:
        SimpleText message = new SimpleText(new StringBuilder("Selected office: ").append(office.getName()).toString());
        // Create an ajax action for setting the message and hi:
        ReplaceContentAction setMessageAction = new ReplaceContentAction("message", message);
        // Create an highlighting effect action for the appearing message:
        HighlightAction highlightAction = new HighlightAction("message", (float) 0.5);
        
        // Create the components to render (a list of html table rows):
        List rows = new LinkedList();
        for(IEmployee emp : employees) {
            TableRow row = new TableRow(emp, new String[]{"firstname", "surname", "matriculationCode"}, null);
            rows.add(row);
        }
        // Create an ajax action for replacing the old table body content, inserting these new rows:
        ReplaceContentAction replaceRowsAction = new ReplaceContentAction("employees", rows);
        
        // Create a concrete ajax response:
        AjaxResponse response = new AjaxResponseImpl();
        // Add actions:
        response.addAction(setMessageAction);
        response.addAction(highlightAction);
        response.addAction(replaceRowsAction);
        
        return response;
    }
    
    public void setStore(MemoryRepository store) {
        this.store = store;
    }
}
