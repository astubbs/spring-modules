package org.springmodules.xt.examples.ajax;

import java.util.Collection;
import java.util.Date;
import org.springmodules.xt.ajax.AbstractAjaxHandler;
import org.springmodules.xt.ajax.AjaxActionEvent;
import org.springmodules.xt.ajax.AjaxCallEnum;
import org.springmodules.xt.ajax.AjaxResponse;
import org.springmodules.xt.ajax.AjaxResponseImpl;
import org.springmodules.xt.ajax.action.AppendContentAction;
import org.springmodules.xt.ajax.action.ReplaceContentAction;
import org.springmodules.xt.ajax.action.SetAttributeAction;
import org.springmodules.xt.ajax.component.Option;
import org.springmodules.xt.ajax.component.Select;
import org.springmodules.xt.ajax.component.SimpleText;
import org.springmodules.xt.ajax.component.TableData;
import org.springmodules.xt.ajax.component.TableRow;
import org.springmodules.xt.examples.domain.IEmployee;
import org.springmodules.xt.examples.domain.MemoryRepository;

/**
 * Ajax handler for listing employees.
 *
 * @author Sergio Bossa
 */
public class FillOfficeHandler extends AbstractAjaxHandler {
    
    private int maxEmployees = 3;
    private MemoryRepository store;
    
    public AjaxResponse addEmployee(AjaxActionEvent event) {
        int employeeCounter = Integer.parseInt(event.getHttpRequest().getParameter("counter"), 10);
        
        if (employeeCounter == this.maxEmployees) {
            SimpleText message = new SimpleText("You cannot add more than " + this.maxEmployees + " employees.");
            
            // Create an ajax action for showing a message:
            ReplaceContentAction messageAction = new ReplaceContentAction("message", message);

            // Create a concrete ajax response:
            AjaxResponse response = new AjaxResponseImpl();
            // Add actions:
            response.addAction(messageAction);
            
            return response;
        }
        else {
            Collection<IEmployee> employees = this.store.getEmployees();

            Select selectionList = new Select("employees");
            for (IEmployee emp : employees) {
                Option o = new Option(emp, "matriculationCode", "surname");
                selectionList.addOption(o);
            }

            TableRow row = new TableRow();
            TableData td1 = new TableData(selectionList);
            td1.addAttribute("onchange", AjaxCallEnum.AJAX_ACTION.getCall("showEmployee"));
            TableData td2 = new TableData(new SimpleText(""));
            TableData td3 = new TableData(new SimpleText(""));
            TableData td4 = new TableData(new SimpleText(""));
            row.addTableData(td1);
            row.addTableData(td2);
            row.addTableData(td3);
            row.addTableData(td4);

            // Create an ajax action for replacing the old table body content, inserting these new rows:
            AppendContentAction appendRowAction = new AppendContentAction("employees", row);
            
            // Create an ajax action for updating the employees counter:
            SetAttributeAction updateCounterAction = new SetAttributeAction("counter", "value", "" + (employeeCounter+1));

            // Create a concrete ajax response:
            AjaxResponse response = new AjaxResponseImpl();
            // Add actions:
            response.addAction(appendRowAction);
            response.addAction(updateCounterAction);

            return response;
        }
    }
    
    /*public AjaxResponse showEmployee(AjaxActionEvent event) {
        
    }*/
    
    public void setStore(MemoryRepository store) {
        this.store = store;
    }

    public int getMaxEmployees() {
        return this.maxEmployees;
    }

    public void setMaxEmployees(int maxEmployees) {
        this.maxEmployees = maxEmployees;
    }
}
