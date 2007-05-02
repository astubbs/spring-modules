package org.springmodules.xt.ajax.validation;

import java.util.Locale;
import org.springframework.context.MessageSource;
import org.springframework.context.support.DelegatingMessageSource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.validation.BindException;
import org.springmodules.xt.ajax.AjaxResponse;
import org.springmodules.xt.ajax.AjaxSubmitEvent;
import org.springframework.validation.ObjectError;
import org.springmodules.xt.ajax.AjaxSubmitEventImpl;
import org.springmodules.xt.ajax.component.Component;
import org.springmodules.xt.ajax.component.TaggedText;
import org.springmodules.xt.ajax.validation.support.DefaultErrorRenderingCallback;
import org.springmodules.xt.test.domain.Employee;
import org.springmodules.xt.test.domain.IEmployee;
import org.springmodules.xt.test.xml.XMLEnhancedTestCase;

/**
 * @author Sergio Bossa
 */
public class DefaultValidationHandlerTest extends XMLEnhancedTestCase {
    
    private AjaxSubmitEvent submitEvent;
    
    public DefaultValidationHandlerTest(String testName) {
        super(testName);
    }
    
    protected void setUp() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        IEmployee target = new Employee();
        BindException errors = new BindException(target, "command");
        errors.addError(new ObjectError("command", new String[]{"ErrorCode1"}, null, "Default Message 1"));
        
        this.submitEvent = new AjaxSubmitEventImpl("submitEvent", request);
        this.submitEvent.setCommandObject(target);
        this.submitEvent.setValidationErrors(errors);
    }
    
    public void testValidatePart1() throws Exception {
        AjaxResponse response = null;
        String rendering = null;
        DefaultValidationHandler handler = new DefaultValidationHandler();
        handler.setMessageSource(new DelegatingMessageSource());
        
        response = handler.validate(submitEvent);
        rendering = response.getResponse();
        System.out.println(rendering);
        
        assertXpathEvaluatesTo("Default Message 1", "//taconite-append-as-children/div", rendering);
        assertXpathEvaluatesTo("wildcard", "//taconite-append-as-children/@matchMode", rendering);
        assertXpathEvaluatesTo("new Effect.Highlight(\"ErrorCode1\",{\"startcolor\":\"#FF0A0A\"});", "//taconite-execute-javascript/script", rendering);
    }
    
    public void testValidatePart2() throws Exception {
        AjaxResponse response = null;
        String rendering = null;
        DefaultValidationHandler handler = new DefaultValidationHandler();
        handler.setMessageSource(new DelegatingMessageSource());
        handler.setErrorRenderingCallback(new DefaultErrorRenderingCallback() {
            public Component getErrorComponent(AjaxSubmitEvent event, ObjectError error, MessageSource messageSource, Locale locale) {
                return new TaggedText(messageSource.getMessage(error.getCode(), null, error.getDefaultMessage() + " for event : " + event.getEventId(), locale), TaggedText.Tag.SPAN);
            }
        });
        
        response = handler.validate(submitEvent);
        rendering = response.getResponse();
        System.out.println(rendering);
        
        assertXpathEvaluatesTo("Default Message 1 for event : submitEvent", "//taconite-append-as-children/span", rendering);
        assertXpathEvaluatesTo("wildcard", "//taconite-append-as-children/@matchMode", rendering);
        assertXpathEvaluatesTo("new Effect.Highlight(\"ErrorCode1\",{\"startcolor\":\"#FF0A0A\"});", "//taconite-execute-javascript/script", rendering);
    }
}
