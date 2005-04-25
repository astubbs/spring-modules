
package org.springmodules.workflow.osworkflow.support;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.ServletException;

import junit.framework.TestCase;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockServletContext;
import org.springframework.mock.web.MockFilterConfig;
import org.springframework.web.context.support.StaticWebApplicationContext;
import org.springframework.web.context.WebApplicationContext;

import org.easymock.MockControl;
import org.springmodules.workflow.osworkflow.WorkflowContextManager;
import org.springmodules.workflow.osworkflow.ThreadLocalWorkflowContextManager;

/**
 * @author robh
 */
public class WorkflowContextFilterTests extends TestCase {

	private MockServletContext context = new MockServletContext();

	private MockHttpServletRequest request = new MockHttpServletRequest(this.context);

	private MockHttpServletResponse response = new MockHttpServletResponse();

	private MockHttpSession session = new MockHttpSession();

	private WorkflowContextFilter filter = new WorkflowContextFilter();

	private MockFilterConfig config = new MockFilterConfig(this.context);

	private MockControl control = MockControl.createControl(FilterChain.class);

	private WorkflowContextManager manager = new ThreadLocalWorkflowContextManager();

	public void setUp() throws Exception {
		this.manager.clear();
		this.request.setSession(this.session);
		this.filter.init(config);

		StaticWebApplicationContext wac = new StaticWebApplicationContext();
		wac.setServletContext(this.context);
		wac.getDefaultListableBeanFactory().registerSingleton("workflowContextManager", this.manager);
		wac.refresh();
		this.context.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, wac);
	}

	public void testPickupFromSession() throws Exception {
		Long id = new Long(13);
		this.session.setAttribute(WorkflowContextFilter.DEFAULT_INSTANCE_ID_KEY, id);

		this.filter.doFilter(request, response, getMockFilterChain());

		control.verify();
		assertEquals(id.longValue(), this.manager.getInstanceId());
	}

	public void testAddToSession() throws Exception {
		assertSessionHasNoInstanceId();

		final Long id = new Long(13);
		this.filter.doFilter(request, response, new FilterChain() {
			public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse) throws IOException, ServletException {
				WorkflowContextFilterTests.this.manager.setInstanceId(id.longValue());
			}
		});

		assertEquals(id, this.session.getAttribute(WorkflowContextFilter.DEFAULT_INSTANCE_ID_KEY));
	}


	public void testWithoutWorkflowAction() throws Exception {
		assertSessionHasNoInstanceId();
		assertFalse(this.manager.isInstanceIdBound());

		this.filter.doFilter(request, response, new FilterChain() {
			public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse) throws IOException, ServletException {
				// no-op
			}
		});

		assertFalse(this.manager.isInstanceIdBound());
		assertSessionHasNoInstanceId();
	}

	private void assertSessionHasNoInstanceId() {
		assertNull(this.session.getAttribute(WorkflowContextFilter.DEFAULT_INSTANCE_ID_KEY));
	}

	private FilterChain getMockFilterChain() throws Exception {
		FilterChain chain = (FilterChain) control.getMock();
		chain.doFilter(this.request, this.response);
		control.setVoidCallable();
		control.replay();
		return chain;
	}
}
