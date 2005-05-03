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

package org.springmodules.samples.lucene.searching.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springmodules.samples.lucene.searching.service.SearchService;

/**
 * @author Thierry Templier
 */
public class SearchController extends SimpleFormController {

	private SearchService searchService;

	public ModelAndView onSubmit(Object command) throws Exception {
		List results=null;
		if( command instanceof WebQuery ) {
			results=searchService.search(((WebQuery)command).getString());
		} else {
			results=new ArrayList();
		}
		Map model=new HashMap();
		model.put("results",results);
		model.put("query",command);
		return new ModelAndView("results",model);
	}

	public SearchService getSearchService() {
		return searchService;
	}

	public void setSearchService(SearchService service) {
		searchService = service;
	}

}
