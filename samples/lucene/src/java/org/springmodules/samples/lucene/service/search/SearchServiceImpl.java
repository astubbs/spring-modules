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

package org.springmodules.samples.lucene.service.search;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.springmodules.lucene.search.core.HitExtractor;
import org.springmodules.lucene.search.core.ParsedQueryCreator;
import org.springmodules.lucene.search.core.QueryCreator;
import org.springmodules.lucene.search.support.LuceneSearchSupport;
import org.springmodules.samples.lucene.bean.indexing.DocumentField;
import org.springmodules.samples.lucene.bean.search.SearchResult;

/**
 * @author Thierry Templier
 */
public class SearchServiceImpl extends LuceneSearchSupport implements SearchService {

	public List search(final String fieldName,final String textToSearch) {
		List results=getTemplate().search(new ParsedQueryCreator() {
			public QueryParams configureQuery() {
				return new QueryParams(fieldName,textToSearch);
			}
		},new HitExtractor() {
			public Object mapHit(int id, Document document, float score) {
				if( document.get("request")!=null ) {
					return new SearchResult(document.get("id"),document.get("request"),
											score,document.get("category"));
				} else {
					return new SearchResult(document.get("id"),document.get("filename"),
											score,document.get("category"));
				}
			}
		});
		return results;
	}

	public List getDocumentFields(final String fieldIdentifier,final String fieldValue) {
		List fieldsDocuments=getTemplate().search(new QueryCreator() {
			public Query createQuery(Analyzer analyzer) throws ParseException {
				return new TermQuery(new Term(fieldIdentifier,fieldValue));
			}
		},new HitExtractor() {
			public Object mapHit(int id, Document document, float score) {
				List fields=new ArrayList();
				for(Enumeration e=document.fields();e.hasMoreElements();) {
					Field field=(Field)e.nextElement();
					DocumentField documentField=new DocumentField(field.name(),field.stringValue(),
														field.isIndexed(),field.isStored());
					fields.add(documentField);
				}
				return fields;
			}
		});

		if( fieldsDocuments.size()==1 ) {
			List fields=(List)fieldsDocuments.get(0);
			return fields;
		}
		return null;
	}

}
