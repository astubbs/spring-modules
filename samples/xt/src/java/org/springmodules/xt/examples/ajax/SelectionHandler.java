package org.springmodules.xt.examples.ajax;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import org.springmodules.xt.ajax.AbstractAjaxHandler;
import org.springmodules.xt.ajax.AjaxActionEvent;
import org.springmodules.xt.ajax.AjaxResponse;
import org.springmodules.xt.ajax.AjaxResponseImpl;
import org.springmodules.xt.ajax.action.ReplaceContentAction;
import org.springmodules.xt.ajax.action.SetAttributeAction;
import org.springmodules.xt.ajax.component.Option;
import org.springmodules.xt.examples.domain.IEmployee;
import org.springmodules.xt.examples.domain.MemoryRepository;

/**
 * Ajax handler for handling offices and employees selections.
 *
 * @author Sergio Bossa
 */
public class SelectionHandler extends AbstractAjaxHandler {
    
    private MemoryRepository store;
    
    public AjaxResponse officeSelection(AjaxActionEvent event) {
        String officeId = event.getHttpRequest().getParameter(event.getElementName());
        // Upon office selection, get employees related to the selected office:
        Collection<IEmployee> employees = store.getEmployeesByOffice(store.getOffice(officeId));
        
        // Create the component to render (a list of html option element):
        List options = new LinkedList();
        Option first = new Option("-1", "Select one ...");
        options.add(first);
        for(IEmployee emp : employees) {
            Option option = new Option(emp, "matriculationCode", "surname");
            options.add(option);
        }
        // Create an ajax action for replacing the content of the "employees" element: 
        ReplaceContentAction action = new ReplaceContentAction("employees", options);
        
        // Create the ajax response:
        AjaxResponse response = new AjaxResponseImpl();
        // Add the action:
        response.addAction(action);
        
        return response;
    }

    public AjaxResponse employeeSelection(AjaxActionEvent event) 
    throws Exception {
        String matriculationCode = event.getHttpRequest().getParameter(event.getElementName());
        // Get the selected employee:
        IEmployee e = store.getEmployee(matriculationCode);
        
        // Create an ajax action for setting the "value" attribute of the "firstname" html element:
        SetAttributeAction setFirstname = new SetAttributeAction("firstname", "value", e.getFirstname());
        // Create an ajax action for setting the "value" attribute of the "surname" html element:
        SetAttributeAction setSurname = new SetAttributeAction("surname", "value", e.getSurname());
        
        // Create the ajax response:
        AjaxResponse response = new AjaxResponseImpl();
        // Add the actions to the response:
        response.addAction(setFirstname);
        response.addAction(setSurname);
        
        return response;
    }

    public void setStore(MemoryRepository store) {
        this.store = store;
    }
}
