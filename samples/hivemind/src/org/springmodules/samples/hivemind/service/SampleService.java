/*
 * Copyright 2002-2004 the original author or authors.
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

package org.springmodules.samples.hivemind.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springmodules.samples.hivemind.dao.ISampleDao;

/**
 * Sample service implementation
 * 
 * @author Thierry Templier
 */
public class SampleService implements ISampleService {
	private ISampleDao dao;

	private static final Log log = LogFactory.getLog(SampleService.class);

	public SampleService() {
		if( log.isDebugEnabled() ) {
			System.out.println("Creation of a new instance of SampleService ("+toString()+")");
		}
	}

	/**
	 * @see service.IMonService#executeService(java.lang.String)
	 */
	public void executeService(String param) {
		if( log.isDebugEnabled() ) {
			System.out.println("The instance of the service is "+toString());
			System.out.println("Execution of the executeService method with the param "+param);
		}
		dao.executeDao(param);
	}

	/**
	 * @return
	 */
	public ISampleDao getDao() {
		return dao;
	}

	/**
	 * @param dao
	 */
	public void setDao(ISampleDao dao) {
		this.dao = dao;
	}

}
