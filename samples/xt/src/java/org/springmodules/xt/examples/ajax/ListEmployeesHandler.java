package org.springmodules.xt.examples.ajax;

import java.util.Collection;
import org.springmodules.xt.ajax.AbstractAjaxHandler;
import org.springmodules.xt.ajax.AjaxActionEvent;
import org.springmodules.xt.ajax.AjaxResponse;
import org.springmodules.xt.ajax.component.Component;
import org.springmodules.xt.ajax.component.RowList;
import org.springmodules.xt.ajax.component.SimpleText;
import org.springmodules.xt.ajax.taconite.TaconiteReplaceContentAction;
import org.springmodules.xt.ajax.taconite.TaconiteResponse;
import org.springmodules.xt.examples.domain.IEmployee;
import org.springmodules.xt.examples.domain.IOffice;
import org.springmodules.xt.examples.domain.MemoryRepository;

/**
 * Ajax handler for listing employees.
 *
 * @author Sergio Bossa
 */
public class ListEmployeesHandler extends AbstractAjaxHandler {
    
    private MemoryRepository store;
    
    public AjaxResponse listEmployees(AjaxActionEvent event) {
        IOffice o = store.getOffice(event.getHttpRequest().getParameter("office"));
        // Load employees:
        Collection<IEmployee> employees = store.getEmployeesByOffice(o);
        
        // Create the component to render (a list of html table rows):
        RowList rows = new RowList(employees.toArray(), new String[]{"firstname", "surname"});
        int i = 0;
        for(IEmployee emp : employees) {
            SimpleText code = new SimpleText(emp.getMatriculationCode());
            rows.appendComponentsToRow(new Component[]{code}, i++);
        }
        // Create an ajax action for replacing the old table body content, inserting these new rows:
        TaconiteReplaceContentAction replaceRows = new TaconiteReplaceContentAction("employees", rows);
        
        // Create a concrete ajax response:
        TaconiteResponse response = new TaconiteResponse();
        
        // Add the actions:
        response.addAction(replaceRows);
        
        return response;
    }

    public void setStore(MemoryRepository store) {
        this.store = store;
    }
}
