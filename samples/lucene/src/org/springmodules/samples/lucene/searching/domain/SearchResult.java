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
	private String source;
	private float score;

	public SearchResult(String source,float score) {
		this.source=source;
		this.score=score;
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

}
