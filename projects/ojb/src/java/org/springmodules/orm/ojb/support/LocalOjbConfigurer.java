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

package org.springmodules.orm.ojb.support;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

/**
 * This configurer needs to be defined as Spring bean when using
 * LocalDataSourceConnectionFactory, to expose the Spring BeanFactory
 * to the corresponding static field of the connection factory.
 * See LocalDataSourceConnectionFactory's javadoc for usage details.
 *
 * <p>It is clearly not ideal to use a static field here, but unfortunately
 * OJB creates an instance of the configured ConnectionFactory at broker
 * creation time. OJB 1.0 does <i>not</i> have an explicit instance-based
 * configuration model, therefore we have to resort to such workarounds.
 *
 * @author Juergen Hoeller
 * @since 1.1
 * @see LocalDataSourceConnectionFactory
 */
public class LocalOjbConfigurer implements BeanFactoryAware {

	public void setBeanFactory(BeanFactory beanFactory) {
		LocalDataSourceConnectionFactory.beanFactory = beanFactory;
	}

}
