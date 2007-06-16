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

package org.springmodules.orm.orbroker;

import net.sourceforge.orbroker.Broker;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

/**
 * FactoryBean that creates a local O/R Broker's Broker instance.
 * Behaves like a Broker instance when used as bean reference, e.g.
 * for BrokerTemplate's "broker" property.
 * <p/>
 * The typical usage will be to register this as singleton factory
 * (for a certain underlying JDBC DataSource) in an application context,
 * and give bean references to application services that need it.
 * <p/>
 * As of 0.9, BrokerFactoryBean can be configured without a
 * {@link #setConfigLocation(org.springframework.core.io.Resource) broker
 * config file}, this scenario is mainly used for testing.
 *
 * @author Omar Irbouh
 * @see BrokerTemplate#setBroker
 * @since 0.7
 */
public class BrokerFactoryBean implements FactoryBean, InitializingBean {

	private Resource configLocation;

	private DataSource dataSource;

	private Properties textReplacements = null;

	private Broker broker;

	/**
	 * Set the location of the O/R Broker config file.
	 */
	public void setConfigLocation(Resource configLocation) {
		this.configLocation = configLocation;
	}

	/**
	 * Set the DataSource to be used by O/R Broker.
	 * A typical value is "WEB-INF/broker.xml".
	 */
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * Set text replacement values. This will replace all <code>{{key}}</code>
	 * type properties in an sql-statement with the values.
	 */
	public void setTextReplacements(Properties textReplacements) {
		this.textReplacements = textReplacements;
	}

	public void afterPropertiesSet() throws IOException {
		// some assertions
		Assert.notNull(dataSource, "dataSource can not be null");

		// create and initialize the broker
		if (this.configLocation != null) {
			this.broker = new Broker(configLocation.getInputStream(), dataSource);
		} else {
			this.broker = new Broker(dataSource);
		}

		// register text replacements
		if (this.textReplacements != null && !this.textReplacements.isEmpty())
			this.broker.setTextReplacements(this.textReplacements);
	}

	public Object getObject() throws Exception {
		return this.broker;
	}

	public Class getObjectType() {
		return (this.broker != null ? this.broker.getClass() : Broker.class);
	}

	public boolean isSingleton() {
		return true;
	}

}