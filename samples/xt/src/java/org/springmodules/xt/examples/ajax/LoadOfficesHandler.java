package org.springmodules.xt.examples.ajax;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import org.springmodules.xt.ajax.AbstractAjaxHandler;
import org.springmodules.xt.ajax.AjaxActionEvent;
import org.springmodules.xt.ajax.AjaxResponse;
import org.springmodules.xt.ajax.AjaxResponseImpl;
import org.springmodules.xt.ajax.action.ReplaceContentAction;
import org.springmodules.xt.ajax.component.Option;
import org.springmodules.xt.examples.domain.IOffice;
import org.springmodules.xt.examples.domain.MemoryRepository;

/**
 * Ajax handler for loading offices.
 *
 * @author Sergio Bossa
 */
public class LoadOfficesHandler extends AbstractAjaxHandler {
    
    private MemoryRepository store;
    
    public AjaxResponse loadOffices(AjaxActionEvent event) {
        // Load offices:
        Collection<IOffice> offices = store.getOffices();
        // Create a concrete ajax response:
        AjaxResponse response = new AjaxResponseImpl();
        
        // Create the component to render (a list of html option elements):
        List options = new LinkedList();
        Option first = new Option("-1", "Select one ...");
        options.add(first);
        for(IOffice office : offices) {
            Option option = new Option(office, "officeId", "name");
            options.add(option);
        }
        // Create an ajax action for replacing the content of the "offices" element with the component just created: 
        ReplaceContentAction action = new ReplaceContentAction("offices", options);
        
        // Add the action:
        response.addAction(action);
        
        return response;
    }

    public void setStore(MemoryRepository store) {
        this.store = store;
    }
}
