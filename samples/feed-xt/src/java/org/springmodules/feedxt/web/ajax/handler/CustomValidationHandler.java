package org.springmodules.feedxt.web.ajax.handler;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springmodules.xt.ajax.AjaxAction;
import org.springmodules.xt.ajax.AjaxResponse;
import org.springmodules.xt.ajax.AjaxSubmitEvent;
import org.springmodules.xt.ajax.action.RemoveContentAction;
import org.springmodules.xt.ajax.action.ReplaceContentAction;
import org.springmodules.xt.ajax.action.prototype.scriptaculous.Effect;
import org.springmodules.xt.ajax.component.Component;
import org.springmodules.xt.ajax.component.Image;
import org.springmodules.xt.ajax.component.TaggedText;
import org.springmodules.xt.ajax.validation.DefaultValidationHandler;

/**
 * Custom {@link org.springmodules.xt.ajax.validation.DefaultValidationHandler}.
 *
 * @author Sergio Bossa
 */
public class CustomValidationHandler extends DefaultValidationHandler {
    
    protected void afterValidation(AjaxSubmitEvent event, AjaxResponse response) {
        if (event.getValidationErrors() != null && event.getValidationErrors().hasErrors()) {
            Component errorImage = new Image(event.getHttpRequest().getContextPath() + "/images/error.gif", "error");
            Component errorMessage = new TaggedText(
                    this.messageSource.getMessage("message.error", null, "Error", LocaleContextHolder.getLocale()),
                    TaggedText.Tag.SPAN);
            AjaxAction removeAction = new RemoveContentAction("onSuccessMessage");
            AjaxAction replaceAction = new ReplaceContentAction("onErrorsMessage", errorImage, errorMessage);
            AjaxAction effectAction = new Effect("Shake", "onErrorsMessage");
            response.addAction(removeAction);
            response.addAction(replaceAction);
            response.addAction(effectAction);
        } else {
            AjaxAction removeAction = new RemoveContentAction("onErrorsMessage");
            response.addAction(removeAction);
        }
    }
}
