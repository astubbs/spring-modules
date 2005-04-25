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

import org.springmodules.workflow.osworkflow.WorkflowContextManager;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * @author Rob Harrop
 */
public abstract class AbstractWorkflowContextHandlerInterceptor extends HandlerInterceptorAdapter {

	private static final String INSTANCE_ID_KEY = "org.springmodules.workflow.osworkflow.instanceId";

	private WorkflowContextManager contextManager;

	private boolean sessionStorageEnabled = true;

	public void setContextManager(WorkflowContextManager contextManager) {
		this.contextManager = contextManager;
	}

	public void setSessionStorageEnabled(boolean sessionStorageEnabled) {
		this.sessionStorageEnabled = sessionStorageEnabled;
	}

	protected boolean isSessionStorageEnabled() {
		return sessionStorageEnabled;
	}

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		this.contextManager.setCaller(getCaller(request));

		if (isSessionStorageEnabled()) {
			HttpSession session = request.getSession();

			Object instanceId = session.getAttribute(INSTANCE_ID_KEY);

			if ((instanceId != null) && (instanceId instanceof Long)) {
				this.contextManager.setInstanceId(((Long) instanceId).longValue());
			}
		}

		return true;
	}

	public void postHandle(
			HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
			throws Exception {


		if (isSessionStorageEnabled()) {
			HttpSession session = request.getSession();

			session.setAttribute(INSTANCE_ID_KEY, new Long(this.contextManager.getInstanceId()));
		}
	}

	protected abstract String getCaller(HttpServletRequest request);
}
