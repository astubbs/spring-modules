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

package org.springmodules.samples.hivemind.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Sample dao implementation
 * 
 * @author Thierry Templier
 */
public class SampleDao implements ISampleDao {

	private String configurationFile;

	private static final Log log = LogFactory.getLog(SampleDao.class);

	public SampleDao() {
		if (log.isDebugEnabled()) {
			System.out.println("Creation of a new instance of SampleDao (" + toString() + ")");
		}
	}

	/**
	 * @see dao.IMonDao#executeDao(java.lang.String)
	 */
	public void executeDao(String param) {
		if (log.isDebugEnabled()) {
			System.out.println("The instance of the service is " + toString());
			System.out.println("The value of configurationFile is " + configurationFile);
			System.out.println("Execution of the executeService method with the param " + param);
		}
	}

	/**
	 * @return
	 */
	public String getConfigurationFile() {
		return configurationFile;
	}

	/**
	 * @param string
	 */
	public void setConfigurationFile(String configurationFile) {
		this.configurationFile = configurationFile;
	}

}
