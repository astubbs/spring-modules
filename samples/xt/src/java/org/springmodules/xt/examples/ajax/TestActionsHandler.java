package org.springmodules.xt.examples.ajax;

import java.util.Random;
import org.springmodules.xt.ajax.AbstractAjaxHandler;
import org.springmodules.xt.ajax.AjaxActionEvent;
import org.springmodules.xt.ajax.AjaxResponse;
import org.springmodules.xt.ajax.AjaxResponseImpl;
import org.springmodules.xt.ajax.action.AppendContentAction;
import org.springmodules.xt.ajax.action.HideElementAction;
import org.springmodules.xt.ajax.action.RemoveContentAction;
import org.springmodules.xt.ajax.action.RemoveElementAction;
import org.springmodules.xt.ajax.action.ReplaceContentAction;
import org.springmodules.xt.ajax.action.ReplaceElementAction;
import org.springmodules.xt.ajax.action.ShowElementAction;
import org.springmodules.xt.ajax.component.InputField;
import org.springmodules.xt.ajax.component.SimpleText;

/**
 * Ajax handler for testing actions.
 *
 * @author Sergio Bossa
 */
public class TestActionsHandler extends AbstractAjaxHandler {
    
    public AjaxResponse appendNumber(AjaxActionEvent event) {
        String number = new Integer((new Random()).nextInt()).toString();
        // Create a concrete ajax response:
        AjaxResponse response = new AjaxResponseImpl();
        
        // Create the text component holding the number:
        SimpleText text = new SimpleText(number + "&#160;&#160;&#160;");
        // Create an ajax action for appending it: 
        AppendContentAction action = new AppendContentAction("num", text);
        
        // Add the action:
        response.addAction(action);
        
        return response;
    }

    public AjaxResponse replaceNumbers(AjaxActionEvent event) {
        String number = new Integer((new Random()).nextInt()).toString();
        // Create a concrete ajax response:
        AjaxResponse response = new AjaxResponseImpl();
        
        // Create the text component holding the number:
        SimpleText text = new SimpleText(number);
        // Create an ajax action for replacing all previously set numbers: 
        ReplaceContentAction action = new ReplaceContentAction("num", text);
        
        // Add the action:
        response.addAction(action);
        
        return response;
    }
    
    public AjaxResponse removeNumbers(AjaxActionEvent event) {
        // Create a concrete ajax response:
        AjaxResponse response = new AjaxResponseImpl();
        
        // Create an ajax action for removing all numbers: 
        RemoveContentAction action = new RemoveContentAction("num");
        
        // Add the action:
        response.addAction(action);
        
        return response;
    }
    
    public AjaxResponse replaceElement(AjaxActionEvent event) {
        // Create a concrete ajax response:
        AjaxResponse response = new AjaxResponseImpl();
        
        // Create the new input field:
        InputField field = new InputField("replaced", "Replaced", InputField.InputType.TEXT);
        // Create an ajax action for replacing the old element: 
        ReplaceElementAction action = new ReplaceElementAction("toReplace", field);
        
        // Add the action:
        response.addAction(action);
        
        return response;
    }
    
    public AjaxResponse removeElement(AjaxActionEvent event) {
        // Create a concrete ajax response:
        AjaxResponse response = new AjaxResponseImpl();
        
        // Create an ajax action for removing the element: 
        RemoveElementAction action = new RemoveElementAction("toRemove");
        
        // Add the action:
        response.addAction(action);
        
        return response;
    }
    
    public AjaxResponse showElement(AjaxActionEvent event) {
        // Create a concrete ajax response:
        AjaxResponse response = new AjaxResponseImpl();
        
        // Create an ajax action for showing an element: 
        ShowElementAction action = new ShowElementAction("toShow");
        
        // Add the action:
        response.addAction(action);
        
        return response;
    }
    
    public AjaxResponse hideElement(AjaxActionEvent event) {
        // Create a concrete ajax response:
        AjaxResponse response = new AjaxResponseImpl();
        
        // Create an ajax action for hiding an element: 
        HideElementAction action = new HideElementAction("toHide");
        
        // Add the action:
        response.addAction(action);
        
        return response;
    }
}
