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

package org.springmodules.orm.orbroker.support;

import net.sourceforge.orbroker.Broker;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.support.DaoSupport;
import org.springframework.util.Assert;
import org.springmodules.orm.orbroker.BrokerTemplate;

/**
 * Convenient super class for O/R Broker data access objects.
 * Requires a Broker to be set, providing a BrokerTemplate
 * based on it to subclasses.
 * <p/>
 * Instead of a plain Broker, you can also pass a preconfigured
 * BrokerTemplate instance in. This allows you to share your
 * BrokerTemplate configuration for all your DAOs, for example
 * a custom SQLExceptionTranslator to use.
 *
 * @author Omar Irbouh
 * @see #setBroker
 * @see #setBrokerTemplate
 * @see org.springmodules.orm.orbroker.BrokerTemplate
 * @see org.springmodules.orm.orbroker.BrokerTemplate#setExceptionTranslator
 * @since 0.7
 */
public abstract class BrokerDaoSupport extends DaoSupport {

	protected final Log logger = LogFactory.getLog(getClass());

	protected BrokerTemplate brokerTemplate = new BrokerTemplate();

	private boolean externalTemplate = false;

	public final void setBrokerTemplate(BrokerTemplate brokerTemplate) {
		Assert.notNull(brokerTemplate, "Cannot set brokerTemplate to null");
		this.brokerTemplate = brokerTemplate;
		this.externalTemplate = true;
	}

	public BrokerTemplate getBrokerTemplate() {
		return brokerTemplate;
	}

	public void setBroker(Broker broker) {
		this.brokerTemplate.setBroker(broker);
	}

	protected final void checkDaoConfig() {
		Assert.notNull(this.brokerTemplate, "broker or brokerTemplate is required");
		if (externalTemplate) {
			this.brokerTemplate.afterPropertiesSet();
		}
	}

}