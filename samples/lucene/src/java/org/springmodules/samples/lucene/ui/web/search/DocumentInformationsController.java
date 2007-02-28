/*
 * Copyright 2002-2007 the original author or authors.
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

package org.springmodules.samples.lucene.ui.web.search;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.springmodules.samples.lucene.service.search.SearchService;

/**
 * @author Thierry Templier
 */
public class DocumentInformationsController implements Controller {

	private SearchService searchService;

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String identifierName = request.getParameter("identifierName");
		String identifierValue = request.getParameter("identifierValue");
		List fields = searchService.getDocumentFields(identifierName,identifierValue);
		Map model = new HashMap();
		model.put("identifierName", identifierName);
		model.put("identifierValue", identifierValue);
		model.put("fields", fields);
		return new ModelAndView("search/documentInformations",model);
	}

	public SearchService getSearchService() {
		return searchService;
	}

	public void setSearchService(SearchService service) {
		searchService = service;
	}

}
