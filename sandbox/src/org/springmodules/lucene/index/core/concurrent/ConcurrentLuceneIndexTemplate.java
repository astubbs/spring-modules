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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.springframework.beans.factory.FactoryBean;
import org.springmodules.lucene.index.core.LuceneIndexTemplate;

/**
 * This class is the dedicated proxy implementation of the LuceneIndexTemplate
 * to manage concurrent calls on the index transparently.
 * 
 * @author Thierry Templier
 */
public class ConcurrentLuceneIndexTemplate implements FactoryBean {

	private LuceneChannel channel;

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
	 * This method returns the dynamic proxy for the LuceneIndexTemplate
	 * interface.
	 */
	public Object getObject() throws Exception {
		return (LuceneIndexTemplate) Proxy.newProxyInstance(
				LuceneIndexTemplate.class.getClassLoader(),
				new Class[] {LuceneIndexTemplate.class},
				new ConcurentLuceneIndexTemplateInvocationHandler(channel));
	}

	public Class getObjectType() {
		return LuceneIndexTemplate.class;
	}

	public boolean isSingleton() {
		return true;
	}

	/**
	 * Invocation handler that creates a request from the called method,
	 * addes it in the channel and eventually waits for a response.
	 */
	private class ConcurentLuceneIndexTemplateInvocationHandler implements InvocationHandler {
		private LuceneChannel channel;

		public ConcurentLuceneIndexTemplateInvocationHandler(LuceneChannel channel) {
			this.channel=channel;
		}

		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			LuceneChannelRequest element=new LuceneChannelRequest(
					method.getName(),method.getParameterTypes(),method.getReturnType(),args);
			if( method.getReturnType()==void.class ) {
				channel.executeWithoutReturn(element);
				return null;
			} else {
				return channel.execute(element);
			}
		}

	}  

}
