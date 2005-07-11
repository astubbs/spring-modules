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

import EDU.oswego.cs.dl.util.concurrent.Channel;
import EDU.oswego.cs.dl.util.concurrent.LinkedQueue;

/**
 * This class represents an implementation of the channel pattern
 * based of the concurrent library of Doug Lea.
 * It manages a channel for the requests and the responses.
 *  
 * @author Thierry Templier
 * @see org.springmodules.lucene.index.core.concurrent.LuceneChannelRequest
 * @see org.springmodules.lucene.index.core.concurrent.LuceneChannelResponse
 */
public class LuceneChannel {

	private Channel channelRequest;
	private Channel channelResponse;

	/**
	 * The default constructor of the call which initializes
	 * both the request and response channels.
	 */
	public LuceneChannel() {
		channelRequest=new LinkedQueue();
		channelResponse=new LinkedQueue();
	}

	/**
	 * The method puts an element in the dedicated request channel.
	 * @param element the element to put
	 * @throws InterruptedException if an error occurs on the underlying channel
	 */
	private void putRequest(LuceneChannelRequest element) throws InterruptedException {
		channelRequest.put(element);
	}

	/**
	 * The method gets an element from the dedicated request channel.
	 * @return the element
	 * @throws InterruptedException if an error occurs on the underlying channel
	 */
	public LuceneChannelRequest getRequest() throws InterruptedException {
		return (LuceneChannelRequest)channelRequest.take();
	}

	/**
	 * The method gets an element from the dedicated response channel.
	 * @return the element
	 * @throws InterruptedException if an error occurs on the underlying channel
	 */
	private LuceneChannelResponse getResponse() throws InterruptedException {
		return (LuceneChannelResponse)channelResponse.take();
	}

	/**
	 * The method puts an element in the dedicated response channel.
	 * @param element the element to put
	 * @throws InterruptedException if an error occurs on the underlying channel
	 */
	public void putResponse(LuceneChannelResponse element) throws InterruptedException {
		channelResponse.put(element);
	}

	/**
	 * The method posts a request in the channel to execute a method. This
	 * method doesn't wait from a response on the response channel.
	 * @param request the request representing the method to call
	 * @throws InterruptedException if an error occurs on the underlying channel
	 */
	public void executeWithoutReturn(LuceneChannelRequest request) throws InterruptedException {
		putRequest(request);
	}

	/**
	 * The method posts a request in the channel to execute a method. This
	 * method wait from a response on the response channel.
	 * @param request the request representing the method to call
	 * @return the response of the call of the corresponding method
	 * @throws InterruptedException if an error occurs on the underlying channel
	 */
	public synchronized LuceneChannelResponse execute(LuceneChannelRequest request) throws InterruptedException {
		putRequest(request);
		return getResponse();
	}

}
