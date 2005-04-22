
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

import org.easymock.MockControl;
import org.springmodules.workflow.osworkflow.WorkflowContext;

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

	public void setUp() throws Exception {
		WorkflowContext.clear();
		this.request.setSession(this.session);
		this.filter.init(config);
	}

	public void testPickupFromSession() throws Exception {
		Long id = new Long(13);
		this.session.setAttribute(WorkflowContextFilter.DEFAULT_INSTANCE_ID_KEY, id);

		this.filter.doFilter(request, response, getMockFilterChain());

		control.verify();
		assertEquals(id.longValue(), WorkflowContext.getInstanceId());
	}

	public void testAddToSession() throws Exception {
		assertSessionHasNoInstanceId();

		final Long id = new Long(13);
		this.filter.doFilter(request, response, new FilterChain() {
			public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse) throws IOException, ServletException {
				WorkflowContext.setInstanceId(id.longValue());
			}
		});

		assertEquals(id, this.session.getAttribute(WorkflowContextFilter.DEFAULT_INSTANCE_ID_KEY));
	}


	public void testWithoutWorkflowAction() throws Exception {
		assertSessionHasNoInstanceId();
		assertFalse(WorkflowContext.hasInstanceId());

		this.filter.doFilter(request, response, new FilterChain() {
			public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse) throws IOException, ServletException {
				// no-op
			}
		});

		assertFalse(WorkflowContext.hasInstanceId());
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
