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

package org.springmodules.lucene.index.core.concurrent;

/**
 * The class wraps the response of the call of a method using
 * the channel.
 * 
 * @author Thierry Templier
 */
public class LuceneChannelResponse {

	private Object methodReturn;

	/**
	 * This constructor is used to create a response instance based on
	 * the the return of the called method.
	 * @param methodReturn the return of the called method
	 */
	public LuceneChannelResponse(Object methodReturn) {
		this.methodReturn=methodReturn;
	}

	/**
	 * This method returns the object by the called method.
	 * @return the return of the called method
	 */
	public Object getMethodReturn() {
		return methodReturn;
	}

}
