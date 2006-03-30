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
 * The class wraps the request of the call of a method using
 * the channel.
 * 
 * @author Thierry Templier
 */
public class LuceneChannelRequest {

	private String methodName;
	private Class[] methodParameterClasses;
	private Class returnType;
	private Object[] methodParameters;

	/**
	 * This constructor is used to create a request instance based on
	 * the name of the called method. It is commonly used to send
	 * messages to the listener thread.
	 * @param methodName the name of the message
	 */
	public LuceneChannelRequest(String methodName) {
		this(methodName,null,null,null);
	}

	/**
	 * This constructor is used to create a request instance based on
	 * the informations of the method to call.
	 * @param methodName the name of the method to call
	 * @param methodParameterClasses the different parameter classes of the method to call
	 * @param returnType the return type of the method to call
	 * @param methodParameters the different parameters of the method to call
	 */
	public LuceneChannelRequest(String methodName,Class[] methodParameterClasses,
								Class returnType,Object[] methodParameters) {
		this.methodName=methodName;
		this.methodParameterClasses=methodParameterClasses;
		this.returnType=returnType;
		this.methodParameters=methodParameters;
	}

	/**
	 * This method returns the name of the method to call.
	 * @return the name of the method to call
	 */
	public String getMethodName() {
		return methodName;
	}

	/**
	 * This method returns the parameters of the method to call.
	 * @return the parameters of the method to call
	 */
	public Object[] getMethodParameters() {
		return methodParameters;
	}

	/**
	 * This method returns the parameter classes of the method to call.
	 * @return the parameter classes of the method to call
	 */
	public Class[] getMethodParameterClasses() {
		return methodParameterClasses;
	}

	/**
	 * This method returns the return class of the method to call.
	 * @return the return class of the method to call
	 */
	public Class getReturnType() {
		return returnType;
	}

}
