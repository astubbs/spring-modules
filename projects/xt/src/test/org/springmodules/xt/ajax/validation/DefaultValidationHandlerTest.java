package org.springmodules.xt.ajax.validation;

import java.util.Locale;
import org.springframework.context.MessageSource;
import org.springframework.context.support.DelegatingMessageSource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.validation.BindException;
import org.springmodules.xt.ajax.AjaxAction;
import org.springmodules.xt.ajax.AjaxResponse;
import org.springmodules.xt.ajax.AjaxSubmitEvent;
import org.springframework.validation.ObjectError;
import org.springmodules.xt.ajax.AjaxSubmitEventImpl;
import org.springmodules.xt.ajax.action.ReplaceContentAction;
import org.springmodules.xt.ajax.component.Component;
import org.springmodules.xt.ajax.component.SimpleText;
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
    private BindException errors;
    
    public DefaultValidationHandlerTest(String testName) {
        super(testName);
    }
    
    protected void setUp() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        IEmployee target = new Employee();
        
        this.errors = new BindException(target, "command");
        this.errors.addError(new ObjectError("command", new String[]{"ErrorCode1"}, null, "Default Message 1"));
        
        this.submitEvent = new AjaxSubmitEventImpl("submitEvent", request);
        this.submitEvent.setCommandObject(target);
    }
    
    public void testValidateWithErrorsPart1() throws Exception {
        AjaxResponse response = null;
        String rendering = null;
        DefaultValidationHandler handler = new DefaultValidationHandler();
        handler.setMessageSource(new DelegatingMessageSource());
        
        // Errors:
        this.submitEvent.setValidationErrors(this.errors);
        
        response = handler.validate(submitEvent);
        rendering = response.render();
        System.out.println(rendering);
        
        assertXpathEvaluatesTo("Default Message 1", "//append-as-children/content/div", rendering);
        assertXpathEvaluatesTo("wildcard", "//append-as-children/context/matcher/@matchMode", rendering);
        assertXpathExists("//execute-javascript/content/script", rendering);
        assertTrue(rendering.indexOf("new Effect.Highlight(\"ErrorCode1\",{\"startcolor\":\"#FF0A0A\"});") != -1);
    }
    
    public void testValidateWithErrorsPart2() throws Exception {
        AjaxResponse response = null;
        String rendering = null;
        DefaultValidationHandler handler = new DefaultValidationHandler();
        handler.setMessageSource(new DelegatingMessageSource());
        handler.setErrorRenderingCallback(new DefaultErrorRenderingCallback() {
            public Component getErrorComponent(AjaxSubmitEvent event, ObjectError error, MessageSource messageSource, Locale locale) {
                return new TaggedText(messageSource.getMessage(error.getCode(), null, error.getDefaultMessage() + " for event : " + event.getEventId(), locale), TaggedText.Tag.SPAN);
            }
        });
        
        // Errors:
        this.submitEvent.setValidationErrors(this.errors);
        
        response = handler.validate(submitEvent);
        rendering = response.render();
        System.out.println(rendering);
        
        assertXpathEvaluatesTo("Default Message 1 for event : submitEvent", "//append-as-children/content/span", rendering);
        assertXpathEvaluatesTo("wildcard", "//append-as-children/context/matcher/@matchMode", rendering);
        assertXpathExists("//execute-javascript/content/script", rendering);
        assertTrue(rendering.indexOf("new Effect.Highlight(\"ErrorCode1\",{\"startcolor\":\"#FF0A0A\"});") != -1);
    }
    
    public void testValidateWithNoErrorsPart1() throws Exception {
        AjaxResponse response = null;
        String rendering = null;
        DefaultValidationHandler handler = new DefaultValidationHandler();
        handler.setMessageSource(new DelegatingMessageSource());
        
        response = handler.validate(submitEvent);
        rendering = response.render();
        System.out.println(rendering);
        
        assertXpathNotExists("/ajax-response/*", rendering);
    }
    
    public void testValidateWithNoErrorsPart2() throws Exception {
        AjaxResponse response = null;
        String rendering = null;
        DefaultValidationHandler handler = new DefaultValidationHandler();
        handler.setMessageSource(new DelegatingMessageSource());
        handler.setSuccessRenderingCallback(new SuccessRenderingCallback() {
            public AjaxAction[] getSuccessActions(AjaxSubmitEvent event) {
                AjaxAction action = new ReplaceContentAction("test", new SimpleText("Default message"));
                return new AjaxAction[]{action};
            }
        });
        
        response = handler.validate(submitEvent);
        rendering = response.render();
        System.out.println(rendering);
        
        assertXpathEvaluatesTo("Default message", "//replace-children/content", rendering);
    }
    
    public void testEncoding() throws Exception {
        DefaultValidationHandler handler = new DefaultValidationHandler();
        AjaxResponse response = handler.validate(submitEvent);
        String rendering = response.render();
        
        System.out.println(rendering);
        
        assertTrue(rendering.indexOf("encoding=\"ISO-8859-1\"") != -1);
        
        handler = new DefaultValidationHandler();
        
        handler.setAjaxResponseEncoding("UTF-8");
        
        response = handler.validate(submitEvent);
        rendering = response.render();
        
        System.out.println(rendering);
        
        assertTrue(rendering.indexOf("encoding=\"UTF-8\"") != -1);
    }
}
