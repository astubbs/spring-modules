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

package org.springmodules.lucene.search;

import java.io.IOException;

import org.apache.lucene.queryParser.ParseException;
import org.springframework.core.NestedRuntimeException;

/**
 * @author Thierry Templier
 */
public class LuceneCannotGetSearcherException extends NestedRuntimeException {

	public LuceneCannotGetSearcherException(String msg,IOException ex) {
		super(msg,ex);
	}

	public LuceneCannotGetSearcherException(String msg,ParseException ex) {
		super(msg,ex);
	}

}
