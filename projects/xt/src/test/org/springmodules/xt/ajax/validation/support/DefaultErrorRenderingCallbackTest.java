package org.springmodules.xt.ajax.validation.support;

import java.util.Locale;
import org.springframework.context.support.DelegatingMessageSource;
import org.springframework.validation.BindException;
import org.springmodules.xt.ajax.AjaxAction;
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

    public void testGetRenderingComponent() {
        Component component = this.callback.getRenderingComponent(this.errors.getGlobalError(), this.messageSource, new Locale("it"));
        assertEquals("Default Message 1", component.render());
    }

    public void testGetRenderingAction() throws Exception {
        AjaxAction action = this.callback.getRenderingAction(this.errors.getGlobalError());
        assertXpathEvaluatesTo("new Effect.Highlight(\"ErrorCode1\",{\"startcolor\":\"#FF0A0A\"});", "//taconite-execute-javascript/script", action.execute());
    }   
}
