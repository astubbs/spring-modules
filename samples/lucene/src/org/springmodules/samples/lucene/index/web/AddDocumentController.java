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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springmodules.samples.lucene.index.service.IndexAccessor;
import org.springmodules.samples.lucene.searching.web.WebQuery;

/**
 * @author Thierry Templier
 */
public class AddDocumentController extends SimpleFormController {

	private IndexAccessor indexAccessor;

	public ModelAndView onSubmit(Object command) throws Exception {
		if( command instanceof TextDocumentHolder ) {
			TextDocumentHolder holder=(TextDocumentHolder)command;
			indexAccessor.addDocument(holder.getTitle(),holder.getText());
		}
		return new ModelAndView("documentAdded");
	}

	public IndexAccessor getIndexAccessor() {
		return indexAccessor;
	}

	public void setIndexAccessor(IndexAccessor accessor) {
		indexAccessor = accessor;
	}

}
