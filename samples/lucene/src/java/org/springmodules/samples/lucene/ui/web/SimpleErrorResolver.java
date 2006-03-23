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

package org.springmodules.samples.lucene.ui.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.Ordered;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springmodules.samples.lucene.util.ExceptionFormater;

/**
 * @author Thierry Templier
 */
public class SimpleErrorResolver implements HandlerExceptionResolver,Ordered {

	private int order; 

	public ModelAndView resolveException(HttpServletRequest request, 
										 HttpServletResponse response, 
										 Object handler,Exception ex) { 
		ExceptionFormater exceptionFormater=new ExceptionFormater(ex);
		return new ModelAndView("error","exception",exceptionFormater);
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int i) {
		order = i;
	}

}
