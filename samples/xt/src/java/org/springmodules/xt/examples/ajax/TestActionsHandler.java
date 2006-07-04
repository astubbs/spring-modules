package org.springmodules.xt.examples.ajax;

import java.util.Random;
import org.springmodules.xt.ajax.AbstractAjaxHandler;
import org.springmodules.xt.ajax.AjaxActionEvent;
import org.springmodules.xt.ajax.AjaxResponse;
import org.springmodules.xt.ajax.component.InputField;
import org.springmodules.xt.ajax.component.SimpleText;
import org.springmodules.xt.ajax.taconite.TaconiteAppendContentAction;
import org.springmodules.xt.ajax.taconite.TaconiteRemoveContentAction;
import org.springmodules.xt.ajax.taconite.TaconiteRemoveElementAction;
import org.springmodules.xt.ajax.taconite.TaconiteReplaceContentAction;
import org.springmodules.xt.ajax.taconite.TaconiteReplaceElementAction;
import org.springmodules.xt.ajax.taconite.TaconiteResponse;

/**
 * Ajax handler for testing actions.
 *
 * @author Sergio Bossa
 */
public class TestActionsHandler extends AbstractAjaxHandler {
    
    public AjaxResponse appendNumber(AjaxActionEvent event) {
        String number = new Integer((new Random()).nextInt()).toString();
        // Create a concrete ajax response:
        TaconiteResponse response = new TaconiteResponse();
        
        // Create the text component holding the number:
        SimpleText text = new SimpleText(number + "&#160;&#160;&#160;");
        // Create an ajax action for appending it: 
        TaconiteAppendContentAction action = new TaconiteAppendContentAction("num", text);
        
        // Add the action:
        response.addAction(action);
        
        return response;
    }

    public AjaxResponse replaceNumbers(AjaxActionEvent event) {
        String number = new Integer((new Random()).nextInt()).toString();
        // Create a concrete ajax response:
        TaconiteResponse response = new TaconiteResponse();
        
        // Create the text component holding the number:
        SimpleText text = new SimpleText(number);
        // Create an ajax action for replacing all previously set numbers: 
        TaconiteReplaceContentAction action = new TaconiteReplaceContentAction("num", text);
        
        // Add the action:
        response.addAction(action);
        
        return response;
    }
    
    public AjaxResponse removeNumbers(AjaxActionEvent event) {
        // Create a concrete ajax response:
        TaconiteResponse response = new TaconiteResponse();
        
        // Create an ajax action for removing all numbers: 
        TaconiteRemoveContentAction action = new TaconiteRemoveContentAction("num");
        
        // Add the action:
        response.addAction(action);
        
        return response;
    }
    
    public AjaxResponse replaceElement(AjaxActionEvent event) {
        // Create a concrete ajax response:
        TaconiteResponse response = new TaconiteResponse();
        
        // Create the new input field:
        InputField field = new InputField("replaced", "Replaced", InputField.InputType.TEXT);
        // Create an ajax action for replacing the old element: 
        TaconiteReplaceElementAction action = new TaconiteReplaceElementAction("toReplace", field);
        
        // Add the action:
        response.addAction(action);
        
        return response;
    }
    
    public AjaxResponse removeElement(AjaxActionEvent event) {
        // Create a concrete ajax response:
        TaconiteResponse response = new TaconiteResponse();
        
        // Create an ajax action for removing the element: 
        TaconiteRemoveElementAction action = new TaconiteRemoveElementAction("toRemove");
        
        // Add the action:
        response.addAction(action);
        
        return response;
    }
}
