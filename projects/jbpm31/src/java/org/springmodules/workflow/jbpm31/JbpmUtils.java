/*
 * Copyright 2002-2006 the original author or authors.
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
package org.springmodules.workflow.jbpm31;

import org.jbpm.JbpmConfiguration;
import org.jbpm.JbpmContext;
import org.jbpm.svc.Services;
import org.springframework.util.Assert;

/**
 * Small utility class for Jbpm 3.1
 * 
 * @author Costin Leau
 * 
 */
public class JbpmUtils {

	/**
	 * Return true if the given context has persistence service.
	 * 
	 * @param context the not-null context to check
	 * @return
	 */
	public static boolean hasPersistenceService(JbpmContext context) {
		Assert.notNull(context);
		return (context.getServices() != null && context.getServiceFactory(Services.SERVICENAME_PERSISTENCE) != null);
	}

	/**
	 * Return true if the given configuration has a persistence service.
	 * 
	 * @param configuration the jBPM configuration object
	 * @param contextName the context name to check (for null the default jBPM
	 *            context name will be used)
	 * @return
	 */
	public static boolean hasPersistenceService(JbpmConfiguration configuration, String contextName) {
		Assert.notNull(configuration);
		String ctxName = (contextName == null ? JbpmContext.DEFAULT_JBPM_CONTEXT_NAME : contextName);

		JbpmContext context = configuration.createJbpmContext(ctxName);
		try {
			return hasPersistenceService(context);

		}
		finally {
			context.close();
		}
	}
}
