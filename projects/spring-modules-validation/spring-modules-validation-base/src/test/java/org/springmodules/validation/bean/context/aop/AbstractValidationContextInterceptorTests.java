package org.springmodules.validation.bean.context.aop;
/**
 * 
 * @author Uri Boness
 */

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;

import junit.framework.TestCase;
import org.aopalliance.intercept.MethodInvocation;
import org.springmodules.validation.bean.context.DefaultValidationContext;
import org.springmodules.validation.bean.context.ValidationContextHolder;
import org.springmodules.validation.util.Switch;

public class AbstractValidationContextInterceptorTests extends TestCase {

    public void testInvoke() throws Throwable {
        AbstractValidationContextInterceptor interceptor = new AbstractValidationContextInterceptor() {
            protected String[] getValidationContextTokens(MethodInvocation methodInvocation) {
                return new String[] { "ctx1", "ctx2" };
            }
        };

        final Switch methodCalledSwitch = new Switch();

        MethodInvocation invocation = new MethodInvocation() {
            public Method getMethod() {
                return null;
            }

            public Object[] getArguments() {
                return new Object[0];
            }

            public Object proceed() throws Throwable {
                methodCalledSwitch.turnOn();
                assertNotNull(ValidationContextHolder.getValidationContext());
                assertTrue(DefaultValidationContext.class.isInstance(ValidationContextHolder.getValidationContext()));
                DefaultValidationContext context = (DefaultValidationContext)ValidationContextHolder.getValidationContext();
                String[] tokens = context.getTokens();
                assertNotNull(tokens);
                assertEquals(2, tokens.length);
                assertEquals("ctx1", tokens[0]);
                assertEquals("ctx2", tokens[1]);
                return "result";
            }

            public Object getThis() {
                return null;
            }

            public AccessibleObject getStaticPart() {
                return null;
            }
        };

        Object result = interceptor.invoke(invocation);

        assertTrue(methodCalledSwitch.isOn());
        assertEquals("result", result);
    }

}