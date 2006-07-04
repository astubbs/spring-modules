package org.springmodules.xt.examples.ajax;

import java.util.Collection;
import org.springmodules.xt.ajax.AbstractAjaxHandler;
import org.springmodules.xt.ajax.AjaxActionEvent;
import org.springmodules.xt.ajax.AjaxResponse;
import org.springmodules.xt.ajax.component.InputField;
import org.springmodules.xt.ajax.component.OptionList;
import org.springmodules.xt.ajax.taconite.TaconiteReplaceContentAction;
import org.springmodules.xt.ajax.taconite.TaconiteReplaceElementAction;
import org.springmodules.xt.ajax.taconite.TaconiteResponse;
import org.springmodules.xt.ajax.taconite.TaconiteSetAttributeAction;
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
        // Load employees related to the selected office:
        Collection employees = store.getEmployeesByOffice(store.getOffice(officeId));
        // Create the ajax response:
        TaconiteResponse response = new TaconiteResponse();
        
        // Create the component to render (a list of html option element):
        OptionList options = new OptionList(employees.toArray(), null, "matriculationCode", "surname");
        options.setFirstTextOption("-1", "Select one ...");
        // Create an ajax action for replacing the content of the "employees" element: 
        TaconiteReplaceContentAction action = new TaconiteReplaceContentAction("employees", options);
        
        // Add the action:
        response.addAction(action);
        
        return response;
    }

    public AjaxResponse employeeSelection(AjaxActionEvent event) 
    throws Exception {
        String matriculationCode = event.getHttpRequest().getParameter(event.getElementName());
        // Get the selected employee:
        IEmployee e = store.getEmployee(matriculationCode);
        // Create the ajax response:
        TaconiteResponse response = new TaconiteResponse();
        
        // Create the component to render:
        InputField field = new InputField("name", e.getSurname(), InputField.InputType.TEXT);
        field.addAttribute("id", "surname");
        
        // Create an ajax action for setting the "value" attribute of the "firstname" html element:
        TaconiteSetAttributeAction action1 = new TaconiteSetAttributeAction("firstname", "value", e.getFirstname());
        
        // Create an ajax action for rendering the field component, replacing the "surname" element: 
        TaconiteReplaceElementAction action2 = new TaconiteReplaceElementAction("surname", field);
        
        // Add the actions to the response:
        response.addAction(action1);
        response.addAction(action2);
        
        return response;
    }

    public void setStore(MemoryRepository store) {
        this.store = store;
    }
}
