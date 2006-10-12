package org.springmodules.xt.examples.ajax;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.springmodules.xt.ajax.AbstractAjaxHandler;
import org.springmodules.xt.ajax.AjaxActionEvent;
import org.springmodules.xt.ajax.AjaxResponse;
import org.springmodules.xt.ajax.AjaxResponseImpl;
import org.springmodules.xt.ajax.action.AppendAsFirstContentAction;
import org.springmodules.xt.ajax.action.AppendContentAction;
import org.springmodules.xt.ajax.action.ExecuteJavascriptFunctionAction;
import org.springmodules.xt.ajax.action.InsertContentAfterAction;
import org.springmodules.xt.ajax.action.InsertContentBeforeAction;
import org.springmodules.xt.ajax.action.RemoveContentAction;
import org.springmodules.xt.ajax.action.RemoveElementAction;
import org.springmodules.xt.ajax.action.ReplaceContentAction;
import org.springmodules.xt.ajax.action.ReplaceElementAction;
import org.springmodules.xt.ajax.action.SetAttributeAction;
import org.springmodules.xt.ajax.action.prototype.HideElementAction;
import org.springmodules.xt.ajax.action.prototype.ShowElementAction;
import org.springmodules.xt.ajax.action.prototype.scriptaculous.GrowAction;
import org.springmodules.xt.ajax.action.prototype.scriptaculous.HighlightAction;
import org.springmodules.xt.ajax.action.prototype.scriptaculous.OpacityAction;
import org.springmodules.xt.ajax.action.prototype.scriptaculous.PulsateAction;
import org.springmodules.xt.ajax.action.prototype.scriptaculous.ShrinkAction;
import org.springmodules.xt.ajax.component.InputField;
import org.springmodules.xt.ajax.component.SimpleText;
import org.springmodules.xt.ajax.component.TaggedText;
import org.springmodules.xt.ajax.component.dynamic.JspComponent;

/**
 * Ajax handler for testing actions.
 *
 * @author Sergio Bossa
 */
public class TestActionsHandler extends AbstractAjaxHandler {
    
    public AjaxResponse appendNumber(AjaxActionEvent event) {
        String number = new Integer((new Random()).nextInt()).toString();
        
        // Create the text component holding the number:
        SimpleText text = new SimpleText(number + "&#160;&#160;&#160;");
        // Create an ajax action for appending it: 
        AppendContentAction action = new AppendContentAction("num", text);
        
        // Create a concrete ajax response:
        AjaxResponse response = new AjaxResponseImpl();
        // Add the action:
        response.addAction(action);
        
        return response;
    }

    public AjaxResponse replaceNumbers(AjaxActionEvent event) {
        String number = new Integer((new Random()).nextInt()).toString();
        
        // Create the text component holding the number:
        SimpleText text = new SimpleText(number);
        // Create an ajax action for replacing all previously set numbers: 
        ReplaceContentAction action = new ReplaceContentAction("num", text);
        
        // Create a concrete ajax response:
        AjaxResponse response = new AjaxResponseImpl();
        // Add the action:
        response.addAction(action);
        
        return response;
    }
    
    public AjaxResponse removeNumbers(AjaxActionEvent event) {
        // Create an ajax action for removing all numbers: 
        RemoveContentAction action = new RemoveContentAction("num");
        
        // Create a concrete ajax response:
        AjaxResponse response = new AjaxResponseImpl();
        // Add the action:
        response.addAction(action);
        
        return response;
    }
    
    public AjaxResponse replaceElement(AjaxActionEvent event) {
        // Create the new input field:
        InputField field = new InputField("replaced", "Replaced", InputField.InputType.TEXT);
        // Create an ajax action for replacing the old element: 
        ReplaceElementAction action = new ReplaceElementAction("toReplace", field);
        
        // Create a concrete ajax response:
        AjaxResponse response = new AjaxResponseImpl();
        // Add the action:
        response.addAction(action);
        
        return response;
    }
    
    public AjaxResponse removeElement(AjaxActionEvent event) {
        // Create an ajax action for removing the element: 
        RemoveElementAction action = new RemoveElementAction("toRemove");
        
        // Create a concrete ajax response:
        AjaxResponse response = new AjaxResponseImpl();
        // Add the action:
        response.addAction(action);
        
        return response;
    }
    
    public AjaxResponse insertAfter(AjaxActionEvent event) {
        // Create an ajax action for inserting content after: 
        InsertContentAfterAction action = new InsertContentAfterAction("toInsertAfter", new TaggedText(", Spring Modules user", TaggedText.Tag.SPAN));
        // Disable the event:
        SetAttributeAction disableAction = new SetAttributeAction("insertAfterButton", "onclick", "");
        
        // Create a concrete ajax response:
        AjaxResponse response = new AjaxResponseImpl();
        // Add the actions:
        response.addAction(action);
        response.addAction(disableAction);
        
        return response;
    }
    
    public AjaxResponse insertBefore(AjaxActionEvent event) {
        // Create an ajax action for inserting content before: 
        InsertContentBeforeAction action = new InsertContentBeforeAction("toInsertBefore", new TaggedText("Hello, ", TaggedText.Tag.SPAN));
        // Disable the event:
        SetAttributeAction disableAction = new SetAttributeAction("insertBeforeButton", "onclick", "");
        
        // Create a concrete ajax response:
        AjaxResponse response = new AjaxResponseImpl();
        // Add the actions:
        response.addAction(action);
        response.addAction(disableAction);
        
        return response;
    }
    
    public AjaxResponse appendAsFirst(AjaxActionEvent event) {
        // Create an ajax action for appending content as first child: 
        AppendAsFirstContentAction action = new AppendAsFirstContentAction("toAppendAsFirst", new TaggedText("Hello, ", TaggedText.Tag.SPAN));
        // Disable the event:
        SetAttributeAction disableAction = new SetAttributeAction("appendAsFirstButton", "onclick", "");
        
        // Create a concrete ajax response:
        AjaxResponse response = new AjaxResponseImpl();
        // Add the actions:
        response.addAction(action);
        response.addAction(disableAction);
        
        return response;
    }
    
    public AjaxResponse includeJsp(AjaxActionEvent event) {
        // Create the component for including jsp content:
        JspComponent jsp = new JspComponent(event.getHttpRequest(), "/includes/include.jsp");
        // Create an ajax action for appending it: 
        AppendContentAction action = new AppendContentAction("jsp", jsp);
        
        // Create a concrete ajax response:
        AjaxResponse response = new AjaxResponseImpl();
        // Add the action:
        response.addAction(action);
        
        return response;
    }
    
    public AjaxResponse executeFunction(AjaxActionEvent event) {
        Map<String, String> options = new HashMap<String, String>();
        options.put("message", "Greetings!");
        
        // Create an ajax action for executing a Javascript function: 
        ExecuteJavascriptFunctionAction action = new ExecuteJavascriptFunctionAction("showAlert", options);
        
        // Create a concrete ajax response:
        AjaxResponse response = new AjaxResponseImpl();
        // Add the action:
        response.addAction(action);
        
        return response;
    }
    
    public AjaxResponse showElement(AjaxActionEvent event) {
        // Create an ajax action for showing an element: 
        ShowElementAction action = new ShowElementAction("toShow");
        
        // Create a concrete ajax response:
        AjaxResponse response = new AjaxResponseImpl();
        // Add the action:
        response.addAction(action);
        
        return response;
    }
    
    public AjaxResponse hideElement(AjaxActionEvent event) {
        // Create an ajax action for hiding an element: 
        HideElementAction action = new HideElementAction("toHide");
        
        // Create a concrete ajax response:
        AjaxResponse response = new AjaxResponseImpl();
        // Add the action:
        response.addAction(action);
        
        return response;
    }
    
    public AjaxResponse highlightElement(AjaxActionEvent event) {
        // Create an ajax action for highlighting an element: 
        HighlightAction action = new HighlightAction("toApplyEffect");
        
        // Create a concrete ajax response:
        AjaxResponse response = new AjaxResponseImpl();
        // Add the action:
        response.addAction(action);
        
        return response;
    }
    
    public AjaxResponse setOpacity(AjaxActionEvent event) {
        // Create an ajax action for setting the opacity of an element: 
        OpacityAction action = new OpacityAction("toApplyEffect", (float) 0.8, (float) 1.0, (float) 0.2);
        
        // Create a concrete ajax response:
        AjaxResponse response = new AjaxResponseImpl();
        // Add the action:
        response.addAction(action);
        
        return response;
    }
    
    public AjaxResponse pulsateElement(AjaxActionEvent event) {
        // Create an ajax action for making pulsate an element: 
        PulsateAction action = new PulsateAction("toApplyEffect");
        
        // Create a concrete ajax response:
        AjaxResponse response = new AjaxResponseImpl();
        // Add the action:
        response.addAction(action);
        
        return response;
    }
    
    public AjaxResponse shrinkElement(AjaxActionEvent event) {
        // Create an ajax action for shrinking an element: 
        ShrinkAction action = new ShrinkAction("toApplyEffect");
        
        // Create a concrete ajax response:
        AjaxResponse response = new AjaxResponseImpl();
        // Add the action:
        response.addAction(action);
        
        return response;
    }
    
    public AjaxResponse growElement(AjaxActionEvent event) {
        // Create an ajax action for making grow an element: 
        GrowAction action = new GrowAction("toApplyEffect");
        
        // Create a concrete ajax response:
        AjaxResponse response = new AjaxResponseImpl();
        // Add the action:
        response.addAction(action);
        
        return response;
    }
}
