package org.springmodules.feedxt.web.ajax.handler;

import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springmodules.feedxt.service.UserService;
import org.springmodules.xt.ajax.AbstractAjaxHandler;
import org.springmodules.xt.ajax.AjaxActionEvent;
import org.springmodules.xt.ajax.AjaxResponse;
import org.springmodules.xt.ajax.AjaxResponseImpl;
import org.springmodules.xt.ajax.action.ReplaceContentAction;
import org.springmodules.xt.ajax.component.TaggedText;

/**
 * Ajax handler for validating user names on the fly, while they're entered into the text field.
 *
 * @author Sergio Bossa
 */
public class UsernameValidationHandler extends AbstractAjaxHandler implements MessageSourceAware {
    
    private UserService userService;
    private MessageSource messageSource;
    
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }
    
    public AjaxResponse validateUsername(AjaxActionEvent event) {
        String username = event.getParameters().get("username");
        boolean exists = this.userService.checkUserAccount(username);
        AjaxResponse response = new AjaxResponseImpl("UTF-8");
        if (!exists) {
            TaggedText msg = new TaggedText(
                    this.messageSource.getMessage("user.available.username", null, "Available", LocaleContextHolder.getLocale()), 
                    TaggedText.Tag.SPAN);
            msg.addAttribute("class", "okMessage");
            
            ReplaceContentAction action = new ReplaceContentAction("username.validation", msg);
            
            response.addAction(action);
        } else {
            TaggedText msg = new TaggedText(
                    this.messageSource.getMessage("user.unavailable.username", null, "Not Available", LocaleContextHolder.getLocale()), 
                    TaggedText.Tag.SPAN);
            msg.addAttribute("class", "warnMessage");
            
            ReplaceContentAction action = new ReplaceContentAction("username.validation", msg);
            
            response.addAction(action);
        }
        return response;
    }
}
