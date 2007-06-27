package org.springmodules.feedxt.web.ajax.handler;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springmodules.feedxt.domain.Feed;
import org.springmodules.feedxt.domain.FeedSubscription;
import org.springmodules.feedxt.domain.User;
import org.springmodules.feedxt.domain.support.CannotAccessFeedException;
import org.springmodules.feedxt.domain.support.UserNotExistentException;
import org.springmodules.feedxt.web.controller.support.UserHolder;
import org.springmodules.feedxt.domain.Entry;
import org.springmodules.feedxt.service.UserService;
import org.springmodules.xt.ajax.AbstractAjaxHandler;
import org.springmodules.xt.ajax.AjaxActionEvent;
import org.springmodules.xt.ajax.AjaxResponse;
import org.springmodules.xt.ajax.AjaxResponseImpl;
import org.springmodules.xt.ajax.ElementMatcher;
import org.springmodules.xt.ajax.action.ExecuteJavascriptFunctionAction;
import org.springmodules.xt.ajax.action.ReplaceContentAction;
import org.springmodules.xt.ajax.action.SetAttributeAction;
import org.springmodules.xt.ajax.action.behaviour.ApplyBehaviour;
import org.springmodules.xt.ajax.action.matcher.SelectorMatcher;
import org.springmodules.xt.ajax.action.prototype.scriptaculous.Effect;
import org.springmodules.xt.ajax.component.Image;
import org.springmodules.xt.ajax.component.TaggedText;
import org.springmodules.xt.ajax.component.dynamic.JspComponent;

/**
 * Ajax Handler for viewing feeds.
 *
 * @author Sergio Bossa
 */
public class FeedViewerHandler extends AbstractAjaxHandler implements MessageSourceAware {
    
    private static final Logger logger = Logger.getLogger(FeedViewerHandler.class);
    
    private UserHolder userHolder;
    private UserService userService;
    private MessageSource messageSource;
    
    public void setUserHolder(UserHolder userHolder) {
        this.userHolder = userHolder;
    }
    
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }
    
    public AjaxResponse viewFeed(AjaxActionEvent event) {
        AjaxResponse response = new AjaxResponseImpl("UTF-8");
        String feedName = event.getParameters().get("feed");
        if (feedName != null) {
            try {
                // Get the feed from user's subscriptions:
                User user = this.userHolder.getUser();
                FeedSubscription subscription = this.userService.getUserSubscriptionByName(user, feedName);
                if (subscription != null) {
                    // Put the actual feed object into HTTP session:
                    Feed feed = subscription.getFeed();
                    HttpSession session = event.getHttpRequest().getSession();
                    session.setAttribute("feed", feed);
                    
                    // Render the feed via external JSP content:
                    JspComponent jsp = new JspComponent(event.getHttpRequest(), "/personal/includes/feedPanel.page");
                    // Replace the content of the "viewer" page part:
                    ReplaceContentAction action1 = new ReplaceContentAction("viewer", jsp);
                    // Re-apply javascript Behaviour rules:
                    ApplyBehaviour action2 = new ApplyBehaviour();
                    
                    // Add actions to response:
                    response.addAction(action1);
                    response.addAction(action2);
                } else {
                    this.renderErrorMessage(event, response, "message.error.feed.not.found", "No feed found.");
                }
            } catch (UserNotExistentException ex) {
                logger.warn(ex.getMessage(), ex.getCause());
                this.renderErrorMessage(event, response, "message.error.reading.feed", "Error while reading the feed.");
            } catch (CannotAccessFeedException ex) {
                logger.warn(ex.getMessage(), ex.getCause());
                this.renderErrorMessage(event, response, "message.error.reading.feed", "Error while reading the feed.");
            }
        }
        return response;
    }
    
    public AjaxResponse toggleEntry(AjaxActionEvent event) {
        AjaxResponse response = new AjaxResponseImpl("UTF-8");
        String currentStatus = event.getParameters().get("status");
        if (currentStatus != null && currentStatus.equals("closed")) {
            response = this.showEntry(event);
        } else if (currentStatus != null && currentStatus.equals("expanded")) {
            response = this.hideEntry(event);
        }
        return response;
    }
    
    /**
     * Show a feed entry.
     */
    private AjaxResponse showEntry(AjaxActionEvent event) {
        AjaxResponse response = new AjaxResponseImpl("UTF-8");
        String sourceElementId = event.getElementId();
        Feed feed = (Feed) event.getHttpRequest().getSession().getAttribute("feed");
        if (feed != null) {
            // Get the feed entry at the corresponding index:
            int entryIndex = Integer.parseInt(event.getParameters().get("entryIndex"));
            Entry entry = (Entry) feed.getEntries().get(entryIndex);
            
            // Set the entry object in the request:
            event.getHttpRequest().setAttribute("entry", entry);
            // Render the entry via external JSP content:
            JspComponent jsp = new JspComponent(event.getHttpRequest(), "/personal/includes/entryPanel.page");
            
            // Change the class of the web element that fired the event:
            SetAttributeAction action1 = new SetAttributeAction(sourceElementId, "class", "expanded");
            
            // Construct the CSS selector identifying the web page part that will be updated with the JSP content:
            String selector = new StringBuilder("#").append(sourceElementId).append("~").append("div.entryBody").toString();
            ElementMatcher matcher = new SelectorMatcher(Arrays.asList(selector));
            // Replace the content of the web page part identified by the selector:
            ReplaceContentAction action2 = new ReplaceContentAction(matcher, jsp);
            // Call a client-side javascript function:
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("selector", selector);
            ExecuteJavascriptFunctionAction action3 = new ExecuteJavascriptFunctionAction("showEntryEffect", params);
            
            // Add actions to response:
            response.addAction(action1);
            response.addAction(action2);
            response.addAction(action3);
        }
        return response;
    }
    
    /**
     * Hide the feed entry: this could be completely done via pure client side javascript, but
     * this is a sample application, so let us play a bit ;) 
     */
    private AjaxResponse hideEntry(AjaxActionEvent event) {
        AjaxResponse response = new AjaxResponseImpl("UTF-8");
        String sourceElementId = event.getElementId();
        
        // Change the class of the web element that fired the event:
        SetAttributeAction action1 = new SetAttributeAction(sourceElementId, "class", "closed");
        
        // Construct the CSS selector identifying the web page part that will be updated :
        String selector = new StringBuilder("#").append(sourceElementId).append("~").append("div.entryBody").toString();
        ElementMatcher matcher = new SelectorMatcher(Arrays.asList(selector));
        // Call a client-side javascript function for hiding the entry:
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("selector", selector);
        ExecuteJavascriptFunctionAction action2 = new ExecuteJavascriptFunctionAction("hideEntryEffect", params);
        
        // Add actions to response:
        response.addAction(action1);
        response.addAction(action2);
        
        return response;
    }
    
    private void renderErrorMessage(AjaxActionEvent event, AjaxResponse response, String messageCode, String defaultMessage) {
        // If errors occur, render an error message with a proper icon:
        String message = this.messageSource.getMessage(messageCode, null, defaultMessage, LocaleContextHolder.getLocale());
        Image img = new Image(event.getHttpRequest().getContextPath() + "/images/error.gif", "error");
        TaggedText txt = new TaggedText(message, TaggedText.Tag.SPAN);
        // Put the message in its proper web page space:
        ReplaceContentAction action1 = new ReplaceContentAction("message", img, txt);
        // Shake the message:
        Effect action2 = new Effect("Shake", "message");
        // Add actions to response:
        response.addAction(action1);
        response.addAction(action2);
    }
}
