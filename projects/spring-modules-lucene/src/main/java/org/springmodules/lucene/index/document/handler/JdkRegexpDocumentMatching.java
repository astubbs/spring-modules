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

package org.springmodules.lucene.index.document.handler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The class is the implementation of DocumentMatching using the
 * JDK regular expressions to determine if a DocumentHandler matches
 * to a class.
 *   
 * @author Thierry Templier
 * @see java.util.regex.Pattern
 * @see java.util.regex.Matcher
 */
public class JdkRegexpDocumentMatching extends AbstractRegexpDocumentMatching {

	private Pattern compiledRegularExpression;
	
	public JdkRegexpDocumentMatching(String regularExpression) {
		super(regularExpression);
	}

	protected void initRegExpr(String regularExpression) {
		this.compiledRegularExpression=Pattern.compile(regularExpression);
	}

	protected boolean matchRegularExpression(String name) {
		Matcher matcher = this.compiledRegularExpression.matcher(name);
		return matcher.matches();
	}

}
