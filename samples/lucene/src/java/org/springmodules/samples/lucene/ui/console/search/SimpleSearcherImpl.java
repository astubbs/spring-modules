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

package org.springmodules.samples.lucene.ui.console.search;

import java.util.Iterator;
import java.util.List;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springmodules.samples.lucene.service.search.SearchService;

/**
 * @author Thierry Templier
 */
public class SimpleSearcherImpl implements Searcher {

	private SearchService searchService;

	public SimpleSearcherImpl() {
	}

	/**
	 * @param hits
	 */
	private void displayResults(String textToSearch,List hits) {
		System.out.println("Results of the search on the query \""+textToSearch+"\"");
		for(Iterator i=hits.iterator();i.hasNext();) {
			String source=(String)i.next();
			System.out.println(" -- source : "+source);
		}
	}

	public void search() {
		final String textToSearch="references";
		List results=searchService.search("contents",textToSearch);
		displayResults(textToSearch,results);
	}

	public static void main(String[] args) {
		ClassPathXmlApplicationContext ctx=new ClassPathXmlApplicationContext("/applicationContext.xml");
		Searcher searcher=(Searcher)ctx.getBean("searcher");
		searcher.search();
	}

	public SearchService getSearchService() {
		return searchService;
	}

	public void setSearchService(SearchService service) {
		searchService = service;
	}

}
