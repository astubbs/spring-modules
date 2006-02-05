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

import org.springmodules.workflow.osworkflow.OsWorkflowContext;
import org.springmodules.workflow.osworkflow.OsWorkflowContextHolder;

import org.springframework.web.bind.RequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * Base <code>HandlerInterceptor</code> used to set the workflow instance ID and caller in the <code>OsWorkflowContext</code>
 * tranparently in a Spring MVC setting.
 * <p/>
 * If session storage is enabled, the workflow instance ID and caller will be transparently bound to and retrieved from the
 * <code>HttpSession</code>. Session storage can be enabled and disabled using the <code>sessionStorageEnabled</code> property.
 * <p/>
 * If request parameter override is enabled, the workflow instance ID can be set by passing in a certain request parameter,
 * by default <code>instanceId</code>. Request parameter override can be enabled and disabled using the
 * <code>allowOverrideWithRequestParameter</code> property.
 * <p/>
 * If both session storage and request parameter override are enabled, then the workflow instance ID specified in the
 * request parameter takes precendence.
 * <p/>
 * Subclasses should implement the <code>getCaller(HttpServletRequest)</code> to retreive the caller identity and return it for
 * it to be bound to the <code>OsWorkflowContext</code>.
 *
 * @author Rob Harrop
 * @see #setSessionStorageEnabled(boolean)
 * @see #setAllowOverrideWithRequestParameter(boolean)
 * @see #setOverrideRequestParameterKey(String)
 */
public abstract class AbstractWorkflowContextHandlerInterceptor extends HandlerInterceptorAdapter {

	/**
	 * Key used to bind the workflow instance ID into the <code>HttpSession</code>.
	 */
	public static final String SESSION_KEY_INSTANCE_ID = "org.springmodules.workflow.osworkflow.instanceId";

	/**
	 * Default request parameter used to set the workflow ID instance in the <code>OsWorkflowContext</code>.
	 */
	private static final String DEFAULT_OVERRIDE_REQUEST_PARAMETER = "instanceId";

	/**
	 * Indicates whether the workflow instance ID should be stored in and retrieved from the <code>HttpSession</code>. Default value
	 * is <code>true</code> indicating that session storage will be used.
	 */
	private boolean sessionStorageEnabled = true;

	/**
	 * Indicates whether a request parameter can be used to manually set the workflow instance ID in the <code>OsWorkflowContext</code>.
	 * Default value is <code>true</code> indicating that a request parameter can be used to set the workflow instance ID.
	 *
	 * @see #overrideRequestParameterKey
	 */
	private boolean allowOverrideWithRequestParameter = true;

	/**
	 * The request parameter used to override the workflow instance ID when <code>allowOverrideWithRequestParameter</code> is
	 * <code>true</code>. The default value is determined by the <code>DEFAULT_OVERRIDE_REQUEST_PARAMETER</code> constant.
	 *
	 * @see DEFAULT_OVERRIDE_REQUEST_PARAMETER
	 */
	private String overrideRequestParameterKey = DEFAULT_OVERRIDE_REQUEST_PARAMETER;

	/**
	 * Set to <code>true</code> to enable session storage for the workflow instance ID or <code>false</code> to disable
	 * session storage. When session storage enabled any workflow instance ID existing in the <code>HttpSession</code>
	 * at the beginning of the request will be bound to the <code>OsWorkflowContext</code>. At the end of a request any
	 * workflow instance ID bound to the <code>OsWorkflowContext</code> will be stored in the <code>HttpSession</code>.
	 */
	public void setSessionStorageEnabled(boolean sessionStorageEnabled) {
		this.sessionStorageEnabled = sessionStorageEnabled;
	}

	/**
	 * Set to <code>true</code> to allow for the workflow instance ID to be overridden using a request parameter.
	 *
	 * @see #getOverrideRequestParameterKey()
	 * @see #setOverrideRequestParameterKey(String)
	 */
	public void setAllowOverrideWithRequestParameter(boolean allowOverrideWithRequestParameter) {
		this.allowOverrideWithRequestParameter = allowOverrideWithRequestParameter;
	}

	/**
	 * Sets the name of the request parameter used to override the workflow instance ID.
	 */
	public void setOverrideRequestParameterKey(String overrideRequestParameterKey) {
		this.overrideRequestParameterKey = overrideRequestParameterKey;
	}

	/**
	 * Indicates whether session storage is enabled and whether the workflow instance ID can be stored in and retrieved from
	 * the <code>HttpSession</code>.
	 */
	protected boolean isSessionStorageEnabled() {
		return sessionStorageEnabled;
	}

	/**
	 * Indicates whether or not the workflow instance ID can be overridden using a request parameter.
	 */
	protected boolean isAllowOverrideWithRequestParameter() {
		return allowOverrideWithRequestParameter;
	}

	/**
	 * Gets the name of the request parameter used to override the workflow instance ID.
	 */
	protected String getOverrideRequestParameterKey() {
		return overrideRequestParameterKey;
	}

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		OsWorkflowContext context = OsWorkflowContextHolder.getWorkflowContext();
		context.setCaller(getCaller(request));

		if (isAllowOverrideWithRequestParameter()) {
			long instanceIdFromRequest = RequestUtils.getLongParameter(request, getOverrideRequestParameterKey(), Long.MIN_VALUE);
			if (instanceIdFromRequest != Long.MIN_VALUE) {
				System.out.println("Setting instance id");
				context.setInstanceId(instanceIdFromRequest);
			}
			else if (isSessionStorageEnabled()) {
				HttpSession session = request.getSession();

				Object instanceId = session.getAttribute(SESSION_KEY_INSTANCE_ID);

				if ((instanceId != null) && (instanceId instanceof Long)) {
					context.setInstanceId(((Long) instanceId).longValue());
				}
			}
		}


		return true;
	}

	/**
	 * If session storage is enabled and the current <code>OsWorkflowContext</code> has a workflow instance ID bound to it
	 * then this workflow instance ID will be stored in the <code>HttpSession</code>.
	 *
	 * @see #setSessionStorageEnabled(boolean)
	 */
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
			throws Exception {

		OsWorkflowContext context = OsWorkflowContextHolder.getWorkflowContext();

		if (isSessionStorageEnabled() && context.hasInstanceId()) {

			HttpSession session = request.getSession();

			session.setAttribute(SESSION_KEY_INSTANCE_ID, new Long(context.getInstanceId()));
		}
	}

	/**
	 * Subclasses should implement this method to return the caller identity for it to be bound to the <code>OsWorkflowContext</code>.
	 */
	protected abstract String getCaller(HttpServletRequest request);
}
