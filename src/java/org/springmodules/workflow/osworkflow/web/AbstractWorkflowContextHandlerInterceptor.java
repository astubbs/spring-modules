/*
* Copyright 2002-2005 the original author or authors.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package org.springmodules.workflow.osworkflow.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springmodules.workflow.osworkflow.OsWorkflowContextHolder;
import org.springmodules.workflow.osworkflow.OsWorkflowContext;

import org.springframework.web.bind.RequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * @author Rob Harrop
 */
public abstract class AbstractWorkflowContextHandlerInterceptor extends HandlerInterceptorAdapter {

	public static final String SESSION_KEY_INSTANCE_ID = "org.springmodules.workflow.osworkflow.instanceId";

	private static final String DEFAULT_OVERRIDE_REQUEST_PARAMETER = "instanceId";

	private boolean sessionStorageEnabled = true;

	private boolean allowOverrideWithRequestParameter = true;

	private String overrideRequestParameterKey = DEFAULT_OVERRIDE_REQUEST_PARAMETER;

	public void setSessionStorageEnabled(boolean sessionStorageEnabled) {
		this.sessionStorageEnabled = sessionStorageEnabled;
	}

	public void setAllowOverrideWithRequestParameter(boolean allowOverrideWithRequestParameter) {
		this.allowOverrideWithRequestParameter = allowOverrideWithRequestParameter;
	}

	public void setOverrideRequestParameterKey(String overrideRequestParameterKey) {
		this.overrideRequestParameterKey = overrideRequestParameterKey;
	}

	protected boolean isSessionStorageEnabled() {
		return sessionStorageEnabled;
	}

	protected boolean isAllowOverrideWithRequestParameter() {
		return allowOverrideWithRequestParameter;
	}

	protected String getOverrideRequestParameterKey() {
		return overrideRequestParameterKey;
	}

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		OsWorkflowContext context = OsWorkflowContextHolder.getWorkflowContext();
		context.setCaller(getCaller(request));

		if (isAllowOverrideWithRequestParameter()) {
			long instanceId = RequestUtils.getLongParameter(request, getOverrideRequestParameterKey(), Long.MIN_VALUE);
			boolean set = (instanceId != Long.MIN_VALUE);
			if (set) {
				context.setInstanceId(instanceId);
			}
		}
		else if (isSessionStorageEnabled()) {
			HttpSession session = request.getSession();

			Object instanceId = session.getAttribute(SESSION_KEY_INSTANCE_ID);

			if ((instanceId != null) && (instanceId instanceof Long)) {
				context.setInstanceId(((Long) instanceId).longValue());
			}
		}

		return true;
	}

	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
			throws Exception {

		OsWorkflowContext context = OsWorkflowContextHolder.getWorkflowContext();

		if (isSessionStorageEnabled() && context.hasInstanceId()) {

			HttpSession session = request.getSession();

			session.setAttribute(SESSION_KEY_INSTANCE_ID, new Long(context.getInstanceId()));
		}
	}

	protected abstract String getCaller(HttpServletRequest request);
}
