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

package org.springmodules.samples.lucene.ui.web.indexing;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springmodules.samples.lucene.service.indexing.IndexAccessorService;

/**
 * @author Thierry Templier
 */
public class AddDocumentController extends SimpleFormController {

	private IndexAccessorService indexAccessorService;

	public ModelAndView onSubmit(Object command) throws Exception {
		if( command instanceof TextDocumentHolder ) {
			TextDocumentHolder holder=(TextDocumentHolder)command;
			indexAccessorService.addDocument(holder.getId(),holder.getTitle(),
									holder.getText(),holder.getCategory());
		}
		return new ModelAndView("indexing/documentAdded");
	}

	public IndexAccessorService getIndexAccessorService() {
		return indexAccessorService;
	}

	public void setIndexAccessorService(IndexAccessorService service) {
		indexAccessorService = service;
	}

}
