package org.springmodules.xt.examples.ajax;

import org.springmodules.xt.ajax.AjaxAction;
import org.springmodules.xt.ajax.AjaxSubmitEvent;
import org.springmodules.xt.ajax.action.ReplaceContentAction;
import org.springmodules.xt.ajax.action.prototype.scriptaculous.Effect;
import org.springmodules.xt.ajax.component.TaggedText;
import org.springmodules.xt.ajax.validation.SuccessRenderingCallback;

/**
 * Callback for rendering a success message.
 *
 * @author Sergio Bossa
 */
public class FormSuccessRenderingCallback implements SuccessRenderingCallback { 

    public AjaxAction[] getSuccessActions(AjaxSubmitEvent event) {
        TaggedText message = new TaggedText("Success!");
        message.addAttribute("style", "font-size: 125%;");
        
        AjaxAction renderingAction = new ReplaceContentAction("success", message);
        AjaxAction fadeAction = new Effect("Fade", "buttons");
        AjaxAction appearAction = new Effect("Appear", "success");
        
        return new AjaxAction[]{renderingAction, fadeAction, appearAction};
    }
}
