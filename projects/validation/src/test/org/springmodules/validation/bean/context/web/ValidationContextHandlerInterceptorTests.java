package org.springmodules.validation.bean.context.web;
/**
 * 
 * @author Uri Boness
 */

import junit.framework.*;
import org.springmodules.validation.bean.context.web.ValidationContextHandlerInterceptor;
import org.springmodules.validation.bean.context.DefaultValidationContext;
import org.springmodules.validation.bean.context.ValidationContextHolder;
import org.springmodules.validation.bean.context.ValidationContextUtils;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

public class ValidationContextHandlerInterceptorTests extends TestCase {

    private ValidationContextHandlerInterceptor interceptor;

    protected void setUp() throws Exception {

        ValidationContextUrlMapping[] mappings = new ValidationContextUrlMapping[2];
        mappings[0] = new ValidationContextUrlMapping("/user/*", new String[] { "ctx1", "ctx2" });
        mappings[1] = new ValidationContextUrlMapping("/admin/*", new String[] { "ctx3" });

        interceptor = new ValidationContextHandlerInterceptor();
        interceptor.setValidationContextUrlMappings(mappings);
    }

    public void testPreHandle() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/user/test.html");
        MockHttpServletResponse response = new MockHttpServletResponse();
        assertTrue(interceptor.preHandle(request, response, null));
        assertNotNull(ValidationContextHolder.getValidationContext());
        assertTrue(DefaultValidationContext.class.isInstance(ValidationContextHolder.getValidationContext()));
        DefaultValidationContext context = (DefaultValidationContext) ValidationContextHolder.getValidationContext();
        assertEquals(2, context.getTokens().length);
        assertEquals("ctx1", context.getTokens()[0]);
        assertEquals("ctx2", context.getTokens()[1]);
    }

    public void testPostHandle() throws Exception {
        ValidationContextUtils.setContext(new String[] { "ctx1", "ctx2" });
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/user/test.html");
        MockHttpServletResponse response = new MockHttpServletResponse();
        interceptor.postHandle(request, response, null, null);
        assertNull(ValidationContextHolder.getValidationContext());
    }
}