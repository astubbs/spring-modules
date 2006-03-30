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
import org.springmodules.lucene.LuceneException;

/**
 * Root of the hierarchy of Lucene search exceptions. It extends
 * the Lucene exception.
 *
 * <p>This exception hierarchy aims to let user code find and handle the
 * kind of error encountered without knowing the details of the particular
 * index access API in use.
 *
 * <p>As this class is a runtime exception, there is no need for user code
 * to catch it or subclasses if any error is to be considered fatal
 * (the usual case).
 *
 * @author Thierry Templier
 */
public class LuceneSearchException extends LuceneException {

	/**
	 * Constructor for LuceneSearchException.
	 * @param msg message
	 */
	public LuceneSearchException(String msg) {
		super(msg);
	}

	/**
	 * Constructor for LuceneSearchException.
	 * @param msg message
	 * @param ex root cause
	 */
	public LuceneSearchException(String msg,IOException ex) {
		super(msg,ex);
	}

	/**
	 * Constructor for LuceneSearchException.
	 * @param msg message
	 * @param ex root cause
	 */
	public LuceneSearchException(String msg,ParseException ex) {
		super(msg,ex);
	}

}
