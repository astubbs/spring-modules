package org.springmodules.xt.examples.ajax;

import java.util.Collection;
import java.util.Date;
import org.springmodules.xt.ajax.AbstractAjaxHandler;
import org.springmodules.xt.ajax.AjaxActionEvent;
import org.springmodules.xt.ajax.AjaxResponse;
import org.springmodules.xt.ajax.AjaxResponseImpl;
import org.springmodules.xt.ajax.action.AppendContentAction;
import org.springmodules.xt.ajax.component.InputField;
import org.springmodules.xt.ajax.component.Option;
import org.springmodules.xt.ajax.component.Select;
import org.springmodules.xt.ajax.component.TableData;
import org.springmodules.xt.ajax.component.TableRow;
import org.springmodules.xt.ajax.component.support.BindStatusHelper;
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
        Collection<IEmployee> employees = this.store.getEmployees();

        // Create the row, with a given rowId:
        TableRow row = new TableRow();
        String rowId = Long.toString(new Date().getTime());
        row.addAttribute("id", rowId);

        // Create the employee selection box (binding to employees) and add it to a column:
        BindStatusHelper helper = new BindStatusHelper("command.employees");
        Select selectionList = new Select(helper.getStatusExpression());
        for (IEmployee emp : employees) {
            Option o = new Option(emp, "matriculationCode", "surname");
            selectionList.addOption(o);
        }
        TableData td1 = new TableData(selectionList);

        // Create the remove button and add it to another column:
        InputField removeButton = new InputField("toRemove", "Remove", InputField.InputType.BUTTON);
        TableData td2 = new TableData(removeButton);

        // Add the two columns to the row:
        row.addTableData(td1);
        row.addTableData(td2);
        
        // Add an onclick event to the remove button, which executes a client side javascript function for actually
        // removing the row:
        removeButton.addAttribute("onclick", new StringBuilder("removeRow('").append(rowId).append("');").toString());

        // Create an ajax action for appending the new row:
        AppendContentAction appendRowAction = new AppendContentAction("employees", row);

        // Create a concrete ajax response:
        AjaxResponse response = new AjaxResponseImpl();
        // Add actions:
        response.addAction(appendRowAction);

        return response;
    }
    
    public void setStore(MemoryRepository store) {
        this.store = store;
    }
}
