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
package org.springmodules.validation.functions;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

/**
 * <p>Function taking the value from a bean property. 
 * 
 * @author Steven Devijver
 * @since Apr 23, 2005
 */
public class BeanPropertyFunction implements Function {

	private String field = null;
	
	public BeanPropertyFunction(String field) {
		super();
		this.field = field;
	}
	
	private void setField(String field) {
		if (field == null) {
			throw new IllegalArgumentException("Field parameter should not be null!");
		}
		this.field = field;
	}

	private String getField() {
		return this.field;
	}
	
	public Object getResult(Object target) {
		BeanWrapper beanWrapper = null;
		if (target instanceof BeanWrapper) {
			beanWrapper = (BeanWrapper)target;
		} else {
			beanWrapper = new BeanWrapperImpl(target);
		}
		return beanWrapper.getPropertyValue(getField());
	}

}
