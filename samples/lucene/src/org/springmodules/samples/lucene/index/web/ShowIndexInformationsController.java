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

package org.springmodules.samples.lucene.index.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.springmodules.samples.lucene.index.domain.IndexInformations;
import org.springmodules.samples.lucene.index.service.IndexAccessor;

/**
 * @author Thierry Templier
 */
public class ShowIndexInformationsController implements Controller {

	private IndexAccessor indexAccessor;

	/**
	 * @see org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ModelAndView handleRequest(HttpServletRequest arg0, HttpServletResponse arg1) throws Exception {
		IndexInformations infos=indexAccessor.getIndexInformations();
		return new ModelAndView("indexInfos","infos",infos);
	}

	public IndexAccessor getIndexAccessor() {
		return indexAccessor;
	}

	public void setIndexAccessor(IndexAccessor accessor) {
		indexAccessor = accessor;
	}

}
