package org.springmodules.xt.examples.ajax;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.springmodules.xt.ajax.AbstractAjaxHandler;
import org.springmodules.xt.ajax.AjaxActionEvent;
import org.springmodules.xt.ajax.AjaxResponse;
import org.springmodules.xt.ajax.AjaxResponseImpl;
import org.springmodules.xt.ajax.action.AppendContentAction;
import org.springmodules.xt.ajax.action.RemoveContentAction;
import org.springmodules.xt.ajax.action.RemoveElementAction;
import org.springmodules.xt.ajax.action.ReplaceContentAction;
import org.springmodules.xt.ajax.action.SetAttributeAction;
import org.springmodules.xt.ajax.action.prototype.scriptaculous.HighlightAction;
import org.springmodules.xt.ajax.component.InputField;
import org.springmodules.xt.ajax.component.Option;
import org.springmodules.xt.ajax.component.Select;
import org.springmodules.xt.ajax.component.SimpleText;
import org.springmodules.xt.ajax.component.TableData;
import org.springmodules.xt.ajax.component.TableRow;
import org.springmodules.xt.ajax.support.AjaxCall;
import org.springmodules.xt.examples.domain.IEmployee;
import org.springmodules.xt.examples.domain.MemoryRepository;

/**
 * Ajax handler for filling an office with a new set of employees.
 *
 * @author Sergio Bossa
 */
public class FillOfficeHandler extends AbstractAjaxHandler {
    
    private MemoryRepository store;
    
    public AjaxResponse addEmployee(AjaxActionEvent event) {
        int maxEmployees = MemoryRepository.MAX_EMPLOYEES;
        int employeeCounter = Integer.parseInt(event.getHttpRequest().getParameter("counter"), 10);
        
        // If the employees to add are too much, display a message:
        if (employeeCounter == maxEmployees) {
            SimpleText message = new SimpleText("You cannot add more than " + maxEmployees + " employees.");
            
            // Create ajax action for showing a message and highlighting it:
            ReplaceContentAction messageAction = new ReplaceContentAction("message", message);
            HighlightAction highlightMessageAction = new HighlightAction("message");

            // Create a concrete ajax response:
            AjaxResponse response = new AjaxResponseImpl();
            // Add actions:
            response.addAction(messageAction);
            response.addAction(highlightMessageAction);
            
            return response;
        }
        // Else, add a row containing a selection box with employees and a "remove" button:
        else {
            Collection<IEmployee> employees = this.store.getEmployees();

            // Create the row:
            TableRow row = new TableRow();
            row.addAttribute("id", "row-" + employeeCounter);
            
            // Create the employee selection box and add it to a column:
            Select selectionList = new Select("employees");
            for (IEmployee emp : employees) {
                Option o = new Option(emp, "matriculationCode", "surname");
                selectionList.addOption(o);
            }
            TableData td1 = new TableData(selectionList);
            
            // Create the remove button and add it to another column:
            InputField removeButton = new InputField("toRemove", "Remove", InputField.InputType.BUTTON);
            TableData td2 = new TableData(removeButton);
            Map actionParams = new HashMap();
            actionParams.put("row", "row-" + employeeCounter);
            td2.addAttribute("onclick", AjaxCall.AJAX_ACTION.getCall("removeRow", actionParams));

            // Add the two columns to the row:
            row.addTableData(td1);
            row.addTableData(td2);

            // Create an ajax action for appending the new row:
            AppendContentAction appendRowAction = new AppendContentAction("employees", row);
            // Create an ajax action for updating the employees counter:
            SetAttributeAction updateCounterAction = new SetAttributeAction("counter", "value", "" + (employeeCounter + 1));

            // Create a concrete ajax response:
            AjaxResponse response = new AjaxResponseImpl();
            // Add actions:
            response.addAction(appendRowAction);
            response.addAction(updateCounterAction);

            return response;
        }
    }
    
    public AjaxResponse removeRow(AjaxActionEvent event) {
        int employeeCounter = Integer.parseInt(event.getHttpRequest().getParameter("counter"), 10);
        String row = event.getParameters().get("row");
        
        // Remove the row passed as paramater in the ajax request:
        RemoveElementAction removeRowAction = new RemoveElementAction(row);
        // Remove the message (if any):
        RemoveContentAction removeMessageContentAction = new RemoveContentAction("message");
        // Decrement the employee counter:
        SetAttributeAction updateCounterAction = new SetAttributeAction("counter", "value", "" + (employeeCounter - 1));
        
        // Create a concrete ajax response:
        AjaxResponse response = new AjaxResponseImpl();
        // Add actions:
        response.addAction(removeRowAction);
        response.addAction(removeMessageContentAction);
        response.addAction(updateCounterAction);

        return response;
    }
    
    public void setStore(MemoryRepository store) {
        this.store = store;
    }
}
