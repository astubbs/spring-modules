package org.springmodules.feedxt.web.ajax.support;

import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springmodules.xt.ajax.AjaxAction;
import org.springmodules.xt.ajax.AjaxSubmitEvent;
import org.springmodules.xt.ajax.action.ReplaceContentAction;
import org.springmodules.xt.ajax.action.SetAttributeAction;
import org.springmodules.xt.ajax.action.prototype.scriptaculous.Effect;
import org.springmodules.xt.ajax.component.Image;
import org.springmodules.xt.ajax.component.TaggedText;
import org.springmodules.xt.ajax.validation.SuccessRenderingCallback;

/**
 * @author Sergio Bossa
 */
public class SuccessMessageCallback implements SuccessRenderingCallback, MessageSourceAware {    
    
    private MessageSource messageSource;
    
    public AjaxAction[] getSuccessActions(AjaxSubmitEvent ajaxSubmitEvent) {
        Image img = new Image(ajaxSubmitEvent.getHttpRequest().getContextPath() + "/images/ok.gif", "success");
        TaggedText msg = new TaggedText(
                this.messageSource.getMessage("message.successful", null, "Successful", LocaleContextHolder.getLocale()), 
                TaggedText.Tag.SPAN);
        
        ReplaceContentAction action1 = new ReplaceContentAction("message", img, msg);
        Effect action2 = new Effect("Shake", "message");
        SetAttributeAction action3 = new SetAttributeAction("name.field", "value", "");
        SetAttributeAction action4 = new SetAttributeAction("url.field", "value", "");
        
        return new AjaxAction[]{action1, action2, action3, action4};
    }

    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }
}
