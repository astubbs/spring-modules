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

import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springmodules.lucene.index.core.LuceneIndexTemplate;

/**
 * This class is the dedicated channel listener implementation. It
 * then delegates the execution of the requests to the injected
 * template.
 * @author Thierry Templier
 * @see org.springmodules.lucene.index.core.LuceneIndexTemplate
 * @see org.springmodules.lucene.index.core.concurrent.LuceneChannel
 */
public class ConcurrentLuceneIndexTemplateListener implements InitializingBean,DisposableBean {

	public final static String STOP_LISTENER_KEYWORD="stop";

	private LuceneIndexTemplate template;
	private LuceneChannel channel;

	private static final Log logger = LogFactory.getLog(ConcurrentLuceneIndexTemplateListener.class);

	/**
	 * Set the channel used to manage concurrent calls on the index.
	 */
	public void setChannel(LuceneChannel channel) {
		this.channel = channel;
	}

	/**
	 * Return the channel used to manage concurrent calls on the index.
	 */
	public LuceneChannel getChannel() {
		return channel;
	}

	/**
	 * Set a configured template.
	 */
	public void setTemplate(LuceneIndexTemplate template) {
		this.template = template;
	}

	/**
	 * Return the LuceneIndexTemplate to use.
	 */
	public LuceneIndexTemplate getTemplate() {
		return template;
	}

	/**
	 * This method initializes the listener thread to manage the
	 * requests sent on the channel. It is responsible to execute
	 * the corresponding method on the injected template.
	 * 
	 * This thread is stopped when a request with the method name
	 * 'stop' is received. This request is sent by the destroy method
	 * at the closing of the context.
	 */
	public void afterPropertiesSet() throws Exception {
		if( template==null ) {
			throw new IllegalArgumentException("template is required.");
		}

		Runnable manager=new Runnable() {
			public void run() {
				try {
					for(;;) {
						LuceneChannelRequest request=(LuceneChannelRequest)channel.getRequest();

						// Manage the stop of the thread
						if( request.getMethodName().equals(STOP_LISTENER_KEYWORD) ) {
							break;
						}

						// Delegate operations on the Lucene template
						Object returnObject=invokeMethod(request);
						if( returnObject!=null ) {
							LuceneChannelResponse response=new LuceneChannelResponse(returnObject);
							channel.putResponse(response);
						}
					}
				} catch(InterruptedException ex) {
					logger.error("An error occured on the channel.",ex);
					logger.info("Concurent Lucene worker stopped.");
				}
			}
		};
		(new Thread(manager)).start();
		logger.info("Concurent Lucene worker started.");
	}

	/**
	 * This method is used to get the Method object corresponding to the
	 * method based on the parameters contained in the request object. 
	 * @param request the request representing the method to execute
	 * @return the Method instance representing the method of the request
	 */
	private Method getMethod(LuceneChannelRequest request) throws SecurityException, NoSuchMethodException {
		Object[] parameters=request.getMethodParameters();
		Class[] parameterClasses=request.getMethodParameterClasses();
		Class returnType=request.getReturnType();
		return LuceneIndexTemplate.class.getDeclaredMethod(
							request.getMethodName(),parameterClasses);
	}

	/**
	 * This method is used to invoke the target method based on the
	 * parameters contained in the request object. 
	 * @param request the request representing the method to execute
	 * @return the object returned to the call of the method
	 */
	private Object invokeMethod(LuceneChannelRequest request) {
		try {
			Method method=getMethod(request);
			return method.invoke(template,request.getMethodParameters());
		} catch(Exception ex) {
			// TODO Manage exception
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * This method stops the thread to manage the requests of the
	 * channel. A request with the method name 'stop' is sent.
	 */
	public void destroy() throws Exception {
		// Stopping the listener
		channel.executeWithoutReturn(new LuceneChannelRequest(STOP_LISTENER_KEYWORD,null,null,null));
		logger.info("Concurent Lucene worker stopped.");
	}

}
