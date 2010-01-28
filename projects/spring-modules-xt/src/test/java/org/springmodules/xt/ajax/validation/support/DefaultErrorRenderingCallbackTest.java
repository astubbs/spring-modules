package org.springmodules.xt.ajax.validation.support;

import java.util.Locale;
import org.springframework.context.support.DelegatingMessageSource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.validation.BindException;
import org.springmodules.xt.ajax.AjaxAction;
import org.springmodules.xt.ajax.AjaxSubmitEventImpl;
import org.springmodules.xt.ajax.component.Component;
import org.springframework.context.MessageSource;
import org.springframework.validation.ObjectError;
import org.springmodules.xt.test.domain.Employee;
import org.springmodules.xt.test.domain.IEmployee;
import org.springmodules.xt.test.xml.XMLEnhancedTestCase;

/**
 * @author Sergio Bossa
 */
public class DefaultErrorRenderingCallbackTest extends XMLEnhancedTestCase {
    
    private DefaultErrorRenderingCallback callback;
    private BindException errors;
    private MessageSource messageSource;
    
    public DefaultErrorRenderingCallbackTest(String testName) {
        super(testName);
    }
    
    protected void setUp() throws Exception {
        IEmployee target = new Employee();
        
        this.callback = new DefaultErrorRenderingCallback();    
        this.errors = new BindException(target, "command");
        this.errors.addError(new ObjectError("command", new String[]{"ErrorCode1"}, null, "Default Message 1"));
        this.messageSource = new DelegatingMessageSource();
    }

    public void testGetRenderingComponent() throws Exception {
        Component component = this.callback.getRenderingComponent(this.errors.getGlobalError(), this.messageSource, new Locale("it"));
        assertXpathEvaluatesTo("Default Message 1", "//div", component.render());
    }

    public void testGetRenderingAction() throws Exception {
        AjaxAction action = this.callback.getRenderingAction(this.errors.getGlobalError());
        String rendering = action.render();
        assertXpathExists("//script", rendering);
        assertTrue(rendering.indexOf("new Effect.Highlight(\"ErrorCode1\",{\"startcolor\":\"#FF0A0A\"});") != -1);
    }   
    
    public void testGetErrorComponent() throws Exception {
        Component component = this.callback.getErrorComponent(new AjaxSubmitEventImpl("submit", new MockHttpServletRequest()), this.errors.getGlobalError(), this.messageSource, new Locale("it"));
        assertXpathEvaluatesTo("Default Message 1", "//div", component.render());
    }

    public void testGetErrosActions() throws Exception {
        AjaxAction[] actions = this.callback.getErrorActions(new AjaxSubmitEventImpl("submit", new MockHttpServletRequest()), this.errors.getGlobalError());
        assertEquals(1, actions.length);
        String rendering = actions[0].render();
        assertXpathExists("//script", rendering);
        assertTrue(rendering.indexOf("new Effect.Highlight(\"ErrorCode1\",{\"startcolor\":\"#FF0A0A\"});") != -1);
    }   
}
