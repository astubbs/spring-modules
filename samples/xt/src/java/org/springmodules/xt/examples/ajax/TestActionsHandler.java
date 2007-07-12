package org.springmodules.xt.examples.ajax;

import java.util.Arrays;
import java.util.Date;
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
import org.springmodules.xt.ajax.action.matcher.SelectorMatcher;
import org.springmodules.xt.ajax.action.prototype.HideElement;
import org.springmodules.xt.ajax.action.prototype.ShowElement;
import org.springmodules.xt.ajax.action.prototype.scriptaculous.AddDroppable;
import org.springmodules.xt.ajax.action.prototype.scriptaculous.Draggable;
import org.springmodules.xt.ajax.action.prototype.scriptaculous.Effect;
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
        event.getHttpRequest().setAttribute("date", new Date());
        event.getHttpRequest().setAttribute("msg", event.getParameters().get("msg"));
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
        Map<String, Object> options = new HashMap<String, Object>();
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
        ShowElement action = new ShowElement("toShow");
        
        // Create a concrete ajax response:
        AjaxResponse response = new AjaxResponseImpl();
        // Add the action:
        response.addAction(action);
        
        return response;
    }
    
    public AjaxResponse hideElement(AjaxActionEvent event) {
        // Create an ajax action for hiding an element: 
        HideElement action = new HideElement("toHide");
        
        // Create a concrete ajax response:
        AjaxResponse response = new AjaxResponseImpl();
        // Add the action:
        response.addAction(action);
        
        return response;
    }
    
    public AjaxResponse highlightElement(AjaxActionEvent event) {
        // Create an ajax action for highlighting an element: 
        Effect action = new Effect("Highlight", "toApplyEffect");
        
        // Create a concrete ajax response:
        AjaxResponse response = new AjaxResponseImpl();
        // Add the action:
        response.addAction(action);
        
        return response;
    }
    
    public AjaxResponse puffElement(AjaxActionEvent event) {
        // Create an ajax action for making an element puff: 
        Effect action = new Effect("Puff", "toApplyEffect");
        action.addOption("duration", "3");
        
        // Create a concrete ajax response:
        AjaxResponse response = new AjaxResponseImpl();
        // Add the action:
        response.addAction(action);
        
        return response;
    }
    
    public AjaxResponse pulsateElement(AjaxActionEvent event) {
        // Create an ajax action for making pulsate an element: 
        Effect action = new Effect("Pulsate", "toApplyEffect");
        
        // Create a concrete ajax response:
        AjaxResponse response = new AjaxResponseImpl();
        // Add the action:
        response.addAction(action);
        
        return response;
    }
    
    public AjaxResponse shrinkElement(AjaxActionEvent event) {
        // Create an ajax action for shrinking an element: 
        Effect action = new Effect("Shrink", "toApplyEffect");
        
        // Create a concrete ajax response:
        AjaxResponse response = new AjaxResponseImpl();
        // Add the action:
        response.addAction(action);
        
        return response;
    }
    
    public AjaxResponse growElement(AjaxActionEvent event) {
        // Create an ajax action for making grow an element: 
        Effect action = new Effect("Grow", "toApplyEffect");
        
        // Create a concrete ajax response:
        AjaxResponse response = new AjaxResponseImpl();
        // Add the action:
        response.addAction(action);
        
        return response;
    }
    
    public AjaxResponse enableDnD(AjaxActionEvent event) {
        // Make the draggable:
        Draggable action1 = new Draggable("draggable");
        action1.addOption("revert", true);
        // Add the droppable:
        AddDroppable action2 = new AddDroppable("droppable");
        action2.addOption("onDrop", "function(draggable, droppable) { showAlertOnDrop(draggable, droppable); }");
        
        // Create a concrete ajax response:
        AjaxResponse response = new AjaxResponseImpl();
        // Add the actions:
        response.addAction(action1);
        response.addAction(action2);
        
        return response;
    }
    
    public AjaxResponse changeColor(AjaxActionEvent event) {
        // CSS Selector matcher for selecting list item elements descending by the element with changeColorDataRow id:
        SelectorMatcher matcher = new SelectorMatcher(Arrays.asList("#changeColorDataRow li"));
        // Action for changing the style attribute to matching elements:
        SetAttributeAction action = new SetAttributeAction(matcher, "style", "color : red");
        
        // Create a concrete ajax response:
        AjaxResponse response = new AjaxResponseImpl();
        // Add the actions:
        response.addAction(action);
        
        return response;
    }
}
