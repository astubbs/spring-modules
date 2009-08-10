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

package org.springmodules.lucene.index.factory;

import org.springframework.beans.factory.InitializingBean;

/**
 * Implementation of {@link IndexFactory} that delegates all calls
 * to a given target {@link IndexFactory}.
 *
 * <p>This class is meant to be subclassed, with subclasses overriding only
 * those methods (such as {@link #getIndexReader()} and {@link #getIndexWriter()})
 * that should not simply delegate to the target IndexFactory.
 *
 * @author Thierry Templier
 * @see #getIndexReader()
 * @see #getIndexWriter()
 */
public class DelegatingIndexFactory implements IndexFactory, InitializingBean {
	private IndexFactory targetIndexFactory;
	
	/**
	 * Create a new DelegatingIndexFactory.
	 * @see #setTargetIndexFactory(IndexFactory)
	 */
	public DelegatingIndexFactory() {
	}
	
	/**
	 * Create a new DelegatingIndexFactory.
	 * @param targetIndexFactory the target IndexFactory
	 */
	public DelegatingIndexFactory(IndexFactory targetIndexFactory) {
		setTargetIndexFactory(targetIndexFactory);
	}
	
	/**
	 * @see IndexFactory#getIndexReader()
	 */
	public LuceneIndexReader getIndexReader() {
		return targetIndexFactory.getIndexReader();
	}

	/**
	 * @see IndexFactory#getIndexWriter()
	 */
	public LuceneIndexWriter getIndexWriter() {
		return targetIndexFactory.getIndexWriter();
	}

	public void afterPropertiesSet() throws Exception {
		if( getTargetIndexFactory()==null ) {
			throw new IllegalArgumentException("The property 'targetIndexFactory' is required");
		}
	}

	/**
	 * Return the target IndexFactory that this IndexFactory should delegate to.
	 */
	public IndexFactory getTargetIndexFactory() {
		return targetIndexFactory;
	}

	/**
	 * Set the target IndexFactory that this IndexFactory should delegate to.
	 */
	public void setTargetIndexFactory(IndexFactory targetIndexFactory) {
		this.targetIndexFactory = targetIndexFactory;
	}

}
