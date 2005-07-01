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

package org.springmodules.samples.lucene.searching.domain;

/**
 * @author Thiery Templier
 */
public class SearchResult {
	private String id;
	private String source;
	private String category;
	private float score;

	public SearchResult(String id,String source,
						float score,String category) {
		this.id=id;
		this.source=source;
		this.score=score;
		this.category=category;
	}

	public float getScore() {
		return score;
	}

	public String getSource() {
		return source;
	}

	public void setScore(float f) {
		score = f;
	}

	public void setSource(String string) {
		source = string;
	}

	public String getCategory() {
		return category;
	}

	public String getId() {
		return id;
	}

	public void setCategory(String string) {
		category = string;
	}

	public void setId(String string) {
		id = string;
	}

}
