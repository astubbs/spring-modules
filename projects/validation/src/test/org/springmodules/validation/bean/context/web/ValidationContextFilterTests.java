package org.springmodules.validation.bean.context.web;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import junit.framework.TestCase;
import org.springframework.mock.web.MockFilterConfig;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springmodules.validation.bean.context.ValidationContextUtils;
import org.springmodules.validation.bean.context.ValidationContextHolder;
import org.springmodules.validation.bean.context.DefaultValidationContext;
import org.springmodules.validation.util.Switch;

/**
 * @author Uri Boness
 */
public class ValidationContextFilterTests extends TestCase {

    public void testInit() throws Exception {
        MockFilterConfig filterConfig = new MockFilterConfig();
        filterConfig.addInitParameter(
                "validationContextUrlMappings",
                "/user/*=ctx1,ctx2\n" +
                "/admin/*=ctx3"
        );
        ValidationContextFilter filter = new ValidationContextFilter();
        filter.init(filterConfig);
        ValidationContextUrlMapping[] mappings = filter.getValidationContextUrlMappings();

        assertNotNull(mappings);
        assertEquals(2, mappings.length);

        assertEquals("/user/*", mappings[0].getUrlPattern());
        assertEquals(2, mappings[0].getContextTokens().length);
        assertEquals("ctx1", mappings[0].getContextTokens()[0]);
        assertEquals("ctx2", mappings[0].getContextTokens()[1]);

        assertEquals("/admin/*", mappings[1].getUrlPattern());
        assertEquals(1, mappings[1].getContextTokens().length);
        assertEquals("ctx3", mappings[1].getContextTokens()[0]);
    }

    public void testConfigurationWithApplicationContext() throws Exception {
        ClassPathXmlApplicationContext appCxt = new ClassPathXmlApplicationContext("testApplicationContext.xml", getClass());
        ValidationContextFilter filter = (ValidationContextFilter)appCxt.getBean("filter");

        ValidationContextUrlMapping[] mappings = filter.getValidationContextUrlMappings();

        assertNotNull(mappings);
        assertEquals(2, mappings.length);

        assertEquals("/user/*", mappings[0].getUrlPattern());
        assertEquals(2, mappings[0].getContextTokens().length);
        assertEquals("ctx1", mappings[0].getContextTokens()[0]);
        assertEquals("ctx2", mappings[0].getContextTokens()[1]);

        assertEquals("/admin/*", mappings[1].getUrlPattern());
        assertEquals(1, mappings[1].getContextTokens().length);
        assertEquals("ctx3", mappings[1].getContextTokens()[0]);
    }

    public void testDoFilterInternal() throws Exception {

        ValidationContextUrlMapping[] mappings = new ValidationContextUrlMapping[2];
        mappings[0] = new ValidationContextUrlMapping("/user/*", new String[] { "ctx1", "ctx2" });
        mappings[1] = new ValidationContextUrlMapping("/admin/*", new String[] { "ctx3" });

        ValidationContextFilter filter = new ValidationContextFilter();
        filter.setValidationContextUrlMappings(mappings);

        filter.afterPropertiesSet();

        final Switch filterCallSwitch = new Switch();
        FilterChain filterChain = new FilterChain() {
            public void doFilter(ServletRequest req, ServletResponse res) {
                filterCallSwitch.turnOn();
                assertTrue(ValidationContextUtils.tokensSupportedByCurrentContext(new String[] { "ctx1", "ctx2" }));
                assertFalse(ValidationContextUtils.tokensSupportedByCurrentContext(new String[] { "ctx3" }));
            }
        };

        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/user/test.html");
        MockHttpServletResponse response = new MockHttpServletResponse();
        filter.doFilterInternal(request, response, filterChain);

        assertTrue(filterCallSwitch.isOn()); // making sure the filter was called
    }

    public void testDoFilterInternal_WithNoUrlMatch() throws Exception {

        ValidationContextUrlMapping[] mappings = new ValidationContextUrlMapping[1];
        mappings[0] = new ValidationContextUrlMapping("/bla/*", new String[] { "ctx1", "ctx2" });

        ValidationContextFilter filter = new ValidationContextFilter();
        filter.setValidationContextUrlMappings(mappings);

        filter.afterPropertiesSet();

        final Switch filterCallSwitch = new Switch();
        FilterChain filterChain = new FilterChain() {
            public void doFilter(ServletRequest req, ServletResponse res) {
                filterCallSwitch.turnOn();
                assertFalse(ValidationContextUtils.tokensSupportedByCurrentContext(new String[] { "ctx1", "ctx2" }));
                assertTrue(DefaultValidationContext.class.isInstance(ValidationContextHolder.getValidationContext()));
                DefaultValidationContext context = (DefaultValidationContext)ValidationContextHolder.getValidationContext();
                assertEquals(0, context.getTokens().length);
            }
        };

        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/user/test.html");
        MockHttpServletResponse response = new MockHttpServletResponse();
        filter.doFilterInternal(request, response, filterChain);

        assertTrue(filterCallSwitch.isOn()); // making sure the filter was called
    }

}
