/*
 * Copyright 2004-2005 the original author or authors.
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
package org.springmodules.validation;

public class LifeCycleBean {

	private boolean beanFactorySet = false;
	private boolean applicationContextSet = false;
	private boolean resourceLoaderSet = false;
	private boolean messageSourceSet = false;
	private boolean applicationEventPublisher = false;
	private boolean servletContextSet = false;
	private boolean initCalled = false;
	private int patternSetCount = 0;
	
	public LifeCycleBean() {
		super();
	}

	public boolean isApplicationContextSet() {
		return applicationContextSet;
	}

	public void setApplicationContextSet(boolean applicationContextSet) {
		this.applicationContextSet = applicationContextSet;
	}

	public boolean isApplicationEventPublisher() {
		return applicationEventPublisher;
	}

	public void setApplicationEventPublisher(boolean applicationEventPublisher) {
		this.applicationEventPublisher = applicationEventPublisher;
	}

	public boolean isBeanFactorySet() {
		return beanFactorySet;
	}

	public void setBeanFactorySet(boolean beanFactorySet) {
		this.beanFactorySet = beanFactorySet;
	}

	public boolean isMessageSourceSet() {
		return messageSourceSet;
	}

	public void setMessageSourceSet(boolean messageSourceSet) {
		this.messageSourceSet = messageSourceSet;
	}

	public boolean isResourceLoaderSet() {
		return resourceLoaderSet;
	}

	public void setResourceLoaderSet(boolean resourceLoaderSet) {
		this.resourceLoaderSet = resourceLoaderSet;
	}

	public boolean isServletContextSet() {
		return servletContextSet;
	}

	public void setServletContextSet(boolean servletContextSet) {
		this.servletContextSet = servletContextSet;
	}

	public boolean isInitCalled() {
		return initCalled;
	}

	public void setInitCalled(boolean initCalled) {
		this.initCalled = initCalled;
	}

	public int getPatternSetCount() {
		return patternSetCount;
	}

	public void setPatternSetCount(int patternSetCount) {
		this.patternSetCount = patternSetCount;
	}

	
}
